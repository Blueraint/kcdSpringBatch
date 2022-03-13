package com.kcd.KCDSpringBatch.batchJob;

import com.kcd.KCDSpringBatch.config.MyBeanWrapperFieldExtractor;
import com.kcd.KCDSpringBatch.creditinfo.domain.CardDetailInfo;
import com.kcd.KCDSpringBatch.dto.CardDetailInfoOutputDto;
import com.kcd.KCDSpringBatch.mapper.CardDetailInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CardDetailPullBatchJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Resource(name = "creditinfoEntityManagerFactory")
//    @Qualifier("creditinfoEntityManagerFactory")
    private final EntityManagerFactory creditinfoEntityManagerFactory;

    @Resource(name = "springbatchEntityManagerFactory")
//    @Qualifier("springbatchEntityManagerFactory")
    private final EntityManagerFactory springbatchEntityManagerFactory;

    private final int chunkSize = 100;

    /*
     * Definition Job
     */

    @Bean
    @Qualifier("cardDetailPullBatchJob")
    public Job cardDetailPullBatchJob() {
        return jobBuilderFactory.get("cardDetailPullBatchJob")
                .start(startStep()) // .jobBuilder.start(Step) create SimpleJobBuilder
                .next(fileTypeDecider())
                    .from(fileTypeDecider()).on("csv").to(cardDetailInfoCreateCsvStep())
                    .from(fileTypeDecider()).on("fixed").to(cardDetailInfoCreateFixedLengthStep())
                    .from(fileTypeDecider()).on("json").to(cardDetailInfoCreateJsonStep())
                    .from(fileTypeDecider()).on("*").fail()
                .end()
                .preventRestart() // currentDate -> always different jobInstance
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        // use jobname
                        jobExecution.getExecutionContext().putString("jobName",jobExecution.getJobInstance().getJobName());
                    }
                    @Override
                    public void afterJob(JobExecution jobExecution) {
                    }
                })
                .incrementer(new InitializationJobConfig.CustomRunIdIncrementer())
                .build();
    }


    /*
     * Definition Decider
     */

    @Bean
    public JobExecutionDecider fileTypeDecider() {
        return new JobExecutionDecider() {

            @Override
            public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
                String fileType = jobExecution.getJobParameters().getString("fileType");

                if(fileType == null) return new FlowExecutionStatus("err");
                else if(fileType.equals("csv")) return new FlowExecutionStatus("csv");
                else if(fileType.equals("fixed")) return new FlowExecutionStatus("fixed");
                else if(fileType.equals("json")) return new FlowExecutionStatus("json");
                else return new FlowExecutionStatus("err");
            }
        };
    }

    /*
     * Definition Step
     */
    @Bean
    public Step startStep() {
        return stepBuilderFactory.get("startStep")
                .tasklet((contribution, chunkContext) -> {
                    log.debug("### job [" + contribution.getStepExecution().getJobExecution().getJobInstance().getJobName() +"]Start ###");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step cardDetailInfoCreateCsvStep() {
        return stepBuilderFactory.get("cardDetailInfoCreateCsvStep")
                .<CardDetailInfo, CardDetailInfoOutputDto>chunk(chunkSize)
                .reader(cardDetailInfoJpaPagingItemReader(null))
                .processor(CardDetailInfoOutputDtoProcessor())
                .writer(cardDetailInfoOutputDtoCsvItemWriter(null))
                .build();
    }

    @Bean
    public Step cardDetailInfoCreateFixedLengthStep() {
        return stepBuilderFactory.get("cardDetailInfoCreateFixedLengthStep")
                .<CardDetailInfo, CardDetailInfoOutputDto>chunk(chunkSize)
                .reader(cardDetailInfoJpaPagingItemReader(null))
                .processor(CardDetailInfoOutputDtoProcessor())
                .writer(cardDetailInfoOutputDtoFixedLengthFileItemWriter(null))
                .build();
    }

    @Bean
    public Step cardDetailInfoCreateJsonStep() {
        return stepBuilderFactory.get("cardDetailInfoCreateJsonStep")
                .<CardDetailInfo, CardDetailInfoOutputDto>chunk(chunkSize)
                .reader(cardDetailInfoJpaPagingItemReader(null))
                .processor(CardDetailInfoOutputDtoProcessor())
                .writer(cardDetailInfoOutputDtoJsonFileItemWriter(null))
                .build();
    }


    /*
     * Definition ItemReader
     */

    /*
     *Bean으로 등록할 경우에는 interface (구현X)로 받으면 하위 정의들이 되지 않아 문제발생...
     * Thread-safe Problem : use JpaPagingItemReader
     * Chunk-Based Batch : order by 로 순서를 유지해야 chunk 이후 처리 가능 -> Customized ItemReader 로 재구현하여 문제 최적화
     */
    @Bean
    @StepScope
    public JpaPagingItemReader<CardDetailInfo> cardDetailInfoJpaPagingItemReader(@Value("#{jobParameters[firmCode]}") String firmCode) {
        log.debug("### Param(firmCode) : " + firmCode);
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("firmCode",firmCode);

        return new JpaPagingItemReaderBuilder<CardDetailInfo>()
                .name("cardDetailInfoJpaPagingItemReader")
                .entityManagerFactory(creditinfoEntityManagerFactory)
                .pageSize(chunkSize)
//              WARN) 모든 데이터를 다 가져와서 페이징한다 -> firm 에 해당하는 customer 만 가져와서 in절로 가져와야 메모리 낭비를 해결할 수 있다
                .queryString("select i from CardDetailInfo i left join fetch i.customer c "+
                            "left join fetch c.firmCustomers fc  " +
                            "left join fetch fc.firm f " +
                            "where f.firmCode = :firmCode " +
                            "order by i.id asc")
                .parameterValues(queryParameters)
                .build();
    }

    /*
     * Definition ItemProcessor
     */

    /*
     * Processor for Entity to OutputDto
     * use Mapstruct
     */
    @Bean
    public ItemProcessor<CardDetailInfo, CardDetailInfoOutputDto> CardDetailInfoOutputDtoProcessor() {
        return cardDetailInfo -> CardDetailInfoMapper.INSTANCE.cardDetailInfoEntityToOutputDto(cardDetailInfo);
    }


    /*
     * Definition ItemWriter
     */

    /* specific*/
    @Bean
    @StepScope
    public FlatFileItemWriter<CardDetailInfoOutputDto> cardDetailInfoOutputDtoCsvItemWriter(@Value("#{jobExecutionContext['jobName']}") String jobName) {
        // file writer using customized fieldExtractor(implements or extends)
        MyBeanWrapperFieldExtractor<CardDetailInfoOutputDto> myBeanWrapperFieldExtractor = new MyBeanWrapperFieldExtractor<>();
        myBeanWrapperFieldExtractor.setNames(Arrays.stream(CardDetailInfoOutputDto.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new));
        myBeanWrapperFieldExtractor.afterPropertiesSet();

        // 각 line 을 합칠 수 있는 aggregator 생성
        DelimitedLineAggregator<CardDetailInfoOutputDto> delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter("|");
        delimitedLineAggregator.setFieldExtractor(myBeanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<CardDetailInfoOutputDto>().name("cardDetailInfoOutputDtoItemWriter")
                .resource(new FileSystemResource("output" + File.separator + jobName + "_data_output.csv"))
                .lineAggregator(delimitedLineAggregator)
                .build();
    }

    /* general*/
    /*
    public FlatFileItemWriter generalCardDetailInfoOutputDtoItemWriter(@Value("#{jobExecutionContext['jobName']}") String jobName, Class clazz) {
        // file writer using customized fieldExtractor(implements or extends)

        MyBeanWrapperFieldExtractor myBeanWrapperFieldExtractor = new MyBeanWrapperFieldExtractor<>();
        myBeanWrapperFieldExtractor.setNames(Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).toArray(String[]::new));
        myBeanWrapperFieldExtractor.afterPropertiesSet();

        // 각 line 을 합칠 수 있는 aggregator 생성
        DelimitedLineAggregator delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter("|");
        delimitedLineAggregator.setFieldExtractor(myBeanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<CardDetailInfoOutputDto>().name(clazz.getName() + "ItemWriter")
                .resource(new FileSystemResource("output" + File.separator + jobName + "_data_output.csv"))
                .lineAggregator(delimitedLineAggregator)
                .build();
    }
     */

    /* FixedLength Type */
    @Bean
    @StepScope
    public FlatFileItemWriter<CardDetailInfoOutputDto> cardDetailInfoOutputDtoFixedLengthFileItemWriter(@Value("#{jobExecutionContext['jobName']}") String jobName) {
        BeanWrapperFieldExtractor<CardDetailInfoOutputDto> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(Arrays.stream(CardDetailInfoOutputDto.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new));
        beanWrapperFieldExtractor.afterPropertiesSet();

        // create formatter(if want to different fixed type, use map(column , length) then make line
        StringBuilder stringBuilder = new StringBuilder();
        IntStream.rangeClosed(1,CardDetailInfoOutputDto.class.getDeclaredFields().length).forEach(i -> stringBuilder.append("%20s"));
        String formatterStr = stringBuilder.toString();

        log.info("### Formatter : " + formatterStr);

        // 각 line을 합칠 수 있는 aggregator 생성
        FormatterLineAggregator<CardDetailInfoOutputDto> lineAggregator = new FormatterLineAggregator<>();
        // line 의 output format을 지정하여 준다(C language style formatter)
        lineAggregator.setFormat(formatterStr);
        lineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<CardDetailInfoOutputDto>().name("cardDetailInfoOutputDtoFixedLengthFileItemWriter")
                .resource(new FileSystemResource("output" + File.separator + jobName + "_data_output.txt"))
                .formatted()
                .names()
                .lineAggregator(lineAggregator)
                .build();
    }

    /* Json Type */
    @Bean
    @StepScope
    public JsonFileItemWriter<CardDetailInfoOutputDto> cardDetailInfoOutputDtoJsonFileItemWriter(@Value("#{jobExecutionContext['jobName']}") String jobName) {
        return new JsonFileItemWriterBuilder<CardDetailInfoOutputDto>()
                .name("cardDetailInfoOutputDtoJsonFileItemWriter")
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>()) // object 를  json 으로 돌려주는 변환기 (marshaller) 생성
                .resource(new FileSystemResource("output" + File.separator + jobName + "_data_output.json"))
                .build();
    }
}
