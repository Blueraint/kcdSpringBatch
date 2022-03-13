package com.kcd.KCDSpringBatch.batchJob;

import com.kcd.KCDSpringBatch.config.MyBeanWrapperFieldExtractor;
import com.kcd.KCDSpringBatch.creditinfo.domain.Customer;
import com.kcd.KCDSpringBatch.dto.CustomerOutputDto;
import com.kcd.KCDSpringBatch.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomerInfoPullBatchJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Resource(name = "creditinfoEntityManagerFactory")
//    @Qualifier("creditinfoEntityManagerFactory")
    private final EntityManagerFactory creditinfoEntityManagerFactory;

    private final int chunkSize = 100;

    @Bean
    @Qualifier("customerInfoPullBatchJob")
    public Job customerInfoPullBatchJob() {
        return jobBuilderFactory.get("customerInfoPullBatchJob")
                .start(customerInfoPullStep1()) // .jobBuilder.start(Step) create SimpleJobBuilder
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        jobExecution.getExecutionContext().putString("jobName",jobExecution.getJobInstance().getJobName());
                    }
                    @Override
                    public void afterJob(JobExecution jobExecution) {
                    }
                })
                .incrementer(new InitializationJobConfig.CustomRunIdIncrementer())
                .build();
    }

    @Bean
    public Step customerInfoPullStep1() {
        // inputDto to Entity
        return stepBuilderFactory.get("customerInformationPullStep1")
                .<Customer, CustomerOutputDto>chunk(chunkSize)
                .reader(customerInfoJpaPagingItemReader(null))
                .processor(customerOutputDtoProcessor())
                .writer(customerInfoOutputDtoItemWriter(null))
                .build();
    }

    // Bean으로 등록할 경우에는 interface (구현X)로 받으면 하위 정의들이 되지 않아 문제발생...
    @Bean
    @StepScope
    public JpaPagingItemReader<Customer> customerInfoJpaPagingItemReader(@Value("#{jobParameters[firmCode]}") String firmCode) {
        log.debug("### Param(firmCode) : " + firmCode);
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("firmCode",firmCode);

        return new JpaPagingItemReaderBuilder<Customer>()
                .name("customerInfoJpaPagingItemReader")
                .entityManagerFactory(creditinfoEntityManagerFactory)
                .pageSize(chunkSize)
//              WARN) 모든 데이터를 다 가져와서 페이징한다 -> firm 에 해당하는 customer 만 가져와서 in절로 가져와야 메모리 낭비를 해결할 수 있다
                .queryString("select c from Customer c " +
                        "left join fetch c.firmCustomers fc " +
                        "left join fetch fc.firm f " +
                        "where f.firmCode = :firmCode " +
                        "order by c.id asc ")
                .parameterValues(queryParameters)
                .build();
    }

    /*
     * Processor for Entity to OutputDto
     * use Mapstruct
     */
    @Bean
    public ItemProcessor<Customer, CustomerOutputDto> customerOutputDtoProcessor() {
        return customer -> CustomerMapper.INSTANCE.entityToOutputDto(customer);
    }

    /* specific*/
    @Bean
    @StepScope
    public FlatFileItemWriter<CustomerOutputDto> customerInfoOutputDtoItemWriter(@Value("#{jobExecutionContext['jobName']}") String jobName) {

        // file writer using customized fieldExtractor(implements or extends)
        MyBeanWrapperFieldExtractor<CustomerOutputDto> myBeanWrapperFieldExtractor = new MyBeanWrapperFieldExtractor<>();
        myBeanWrapperFieldExtractor.setNames(Arrays.stream(CustomerOutputDto.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new));
        myBeanWrapperFieldExtractor.afterPropertiesSet();

        // 각 line 을 합칠 수 있는 aggregator 생성
        DelimitedLineAggregator<CustomerOutputDto> delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter("|");
        delimitedLineAggregator.setFieldExtractor(myBeanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<CustomerOutputDto>().name("customerInfoOutputDtoItemWriter")
                .resource(new FileSystemResource("output" + File.separator + jobName + "_data_output.csv"))
                .lineAggregator(delimitedLineAggregator)
                .build();
    }
}
