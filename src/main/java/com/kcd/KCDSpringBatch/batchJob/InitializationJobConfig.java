package com.kcd.KCDSpringBatch.batchJob;

import com.kcd.KCDSpringBatch.creditinfo.domain.CardDetailInfo;
import com.kcd.KCDSpringBatch.creditinfo.domain.Customer;
import com.kcd.KCDSpringBatch.dto.CardDetailInfoInputDto;
import com.kcd.KCDSpringBatch.mapper.CardDetailInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
public class InitializationJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Resource(name = "creditinfoEntityManagerFactory")
//    @Qualifier("creditinfoEntityManagerFactory")
    private final EntityManagerFactory creditinfoEntityManagerFactory;

    @Qualifier("creditinfoTransactionManager")
    private final PlatformTransactionManager creditinfoTransactionManager;

    // 단일 Thread 에서는 chunksize 를 늘린다고 해서 속도가 비약적으로 상승하지 않는다.
    @Value("${spring.batch.job.chunkSize}")
    private int chunkSize;

    private String firmCode;

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    /* Must seperate API Call Job | Java(Spring) Automatically Run Job */
    /*Step -> SimpleJob*/
    @Bean
    @Qualifier("initializationJob")
    @Transactional
    public Job initializationJob() {
        return jobBuilderFactory.get("initializationJob")
                .start(initializationStep1()) // .jobBuilder.start(Step) create SimpleJobBuilder
                .incrementer(new CustomRunIdIncrementer())
                .build();
    }

    @Bean
    @JobScope
    public Step initializationStep1() {
        // inputDto to Entity
        return stepBuilderFactory.get("initializationStep1")
                .<CardDetailInfoInputDto, CardDetailInfo>chunk(chunkSize)
                .reader(cardDetailFlatFileItemInfoReader())
                .processor(cardDetailInfoInputDtoProcessor())
                .writer(cardDetailInfoItemWriter())
                .transactionManager(creditinfoTransactionManager)
                .build();
    }


    /*
     * Processor for InputDto to Entity
     * use Mapstruct
     */
    @Bean
    public ItemProcessor<CardDetailInfoInputDto, CardDetailInfo> cardDetailInfoInputDtoProcessor() {

        return cardDetailInfoInputDto -> CardDetailInfoMapper.INSTANCE.cardDetailInfoInputDtoToEntity(cardDetailInfoInputDto,new Customer(cardDetailInfoInputDto.getCustomerNumber(), cardDetailInfoInputDto.getCodeGender()));
    }

    @Bean
    public JpaItemWriter<CardDetailInfo> cardDetailInfoItemWriter() {
        JpaItemWriter<CardDetailInfo> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(creditinfoEntityManagerFactory);

        return jpaItemWriter;
    }

    @Bean
    public FlatFileItemReader<CardDetailInfoInputDto> cardDetailFlatFileItemInfoReader() {
        FlatFileItemReader<CardDetailInfoInputDto> flatFileItemReader = new FlatFileItemReader<>();

        flatFileItemReader.setEncoding(StandardCharsets.UTF_8.name());
        flatFileItemReader.setResource(new ClassPathResource("input" + File.separator + "application_data_sample.csv"));
        flatFileItemReader.setLinesToSkip(1); // CSV 의 1번째 line을 skip

        // item(instance, object) 정의대로 매핑하여주는 fieldset 생성. 1 line 이 token으로 인식된다
        DefaultLineMapper<CardDetailInfoInputDto> defaultLineMapper = new DefaultLineMapper<>();
        /*
         * linetokenizer 생성
         * tokenizer 를 생성할 대상을 Entity 에서 직접 Field 값을 불러와서 세팅하였다.
         * !!! FlatItemReader에서는 Entity를 사용하기 어렵다(Item과 Entity가 1:1 대응하지 않는다) -> Dto 처럼 별도 객체를 만들어서 변환하여야 한다
         */
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(); // delimiter(splitter. /0(enter)포함) 로 구분된 string file 을 구분하여 주는 tokenizer 생성(token 생성기)
        delimitedLineTokenizer.setNames(Arrays.stream(CardDetailInfoInputDto.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new)); // field(column) set
        delimitedLineTokenizer.setDelimiter(","); // splitter set


        BeanWrapperFieldSetMapper<CardDetailInfoInputDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(CardDetailInfoInputDto.class);

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer); // line tokenizer 생성
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper); // field set 을 매핑시켜 줄 class file(또는 object)를 지정하여 준다.
        flatFileItemReader.setLineMapper(defaultLineMapper); // tokenizer 로 parse, then insert into class defined by fieldsetmapper

        return flatFileItemReader;
    }

    /*general*/
//    @Bean
    public FlatFileItemReader generalFlatFileItemInfoReader(Class clazz) {
        FlatFileItemReader flatFileItemReader = new FlatFileItemReader<>();

        flatFileItemReader.setEncoding(StandardCharsets.UTF_8.name());
        flatFileItemReader.setResource(new ClassPathResource("input" + File.separator + clazz.getName() + "_data_sample.csv"));
        flatFileItemReader.setLinesToSkip(1); // CSV 의 1번째 line을 skip

        // item(instance, object) 정의대로 매핑하여주는 fieldset 생성. 1 line 이 token으로 인식된다
        DefaultLineMapper defaultLineMapper = new DefaultLineMapper<>();
        /*
         * linetokenizer 생성
         * tokenizer 를 생성할 대상을 Entity 에서 직접 Field 값을 불러와서 세팅하였다.
         * !!! FlatItemReader에서는 Entity를 사용하기 어렵다(Item과 Entity가 1:1 대응하지 않는다) -> Dto 처럼 별도 객체를 만들어서 변환하여야 한다
         */
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(); // delimiter(splitter. /0(enter)포함) 로 구분된 string file 을 구분하여 주는 tokenizer 생성(token 생성기)
        delimitedLineTokenizer.setNames(Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).toArray(String[]::new)); // field(column) set
        delimitedLineTokenizer.setDelimiter(","); // splitter set


        BeanWrapperFieldSetMapper beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(clazz);

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer); // line tokenizer 생성
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper); // field set 을 매핑시켜 줄 class file(또는 object)를 지정하여 준다.
        flatFileItemReader.setLineMapper(defaultLineMapper); // tokenizer 로 parse, then insert into class defined by fieldsetmapper

        return flatFileItemReader;
    }


    /*
     * Async Step
     * Async Processor for InputDto to Entity
     * use Mapstruct
     * Delegate(위임) from AsyncItemProcessor -> use cardDetailinfoInputDtoProcessor(definited by InitializationJobConfig)
     */
    /*
    @Bean
    @JobScope
    public Step asyncInitializationStep1() {
        // inputDto to Entity

        return stepBuilderFactory.get("asyncInitializationStep1")
                .repository(jobRepository)
                .transactionManager(creditinfoTransactionManager)
                .<CardDetailInfoInputDto, CardDetailInfo>chunk(chunkSize)
                .reader(cardDetailFlatFileItemInfoReader())
                .processor(asyncCardDetailInfoInputDtoProcessor())
                .writer(asyncCardDetailInfoItemWriter())
                .build()
                ;

    }

    @Bean
    public AsyncItemProcessor asyncCardDetailInfoInputDtoProcessor() {
        System.out.println("%%%%%Async Processor%%%%%");

        AsyncItemProcessor<CardDetailInfoInputDto, CardDetailInfo> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(cardDetailInfoInputDtoProcessor());
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());


        return asyncItemProcessor;
    }

    @Bean
    public AsyncItemWriter asyncCardDetailInfoItemWriter() {
        System.out.println("@@@@@Async Writer@@@@@");
        AsyncItemWriter<CardDetailInfo> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(cardDetailInfoItemWriter());

        return asyncItemWriter;
    }
    */



    static class CustomRunIdIncrementer implements JobParametersIncrementer {

        static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-hhmmss");
        @Override
        public JobParameters getNext(JobParameters parameters) {
            String id = format.format(new Date());

            return new JobParametersBuilder().addString("run.id",id).toJobParameters();
        }
    }
}
