package com.kcd.KCDSpringBatch.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Batch Configuration
 */

//@Configuration
public class CustomBatchConfigurer extends BasicBatchConfigurer  {
    @Autowired
    @Resource(name = "springbatchDataSource")
    private DataSource dataSource;

    @Autowired
    @Resource(name = "springbatchEntityManagerFactory")
    private EntityManagerFactory entityManagerFactory;

    /**
     * Create a new {@link BasicBatchConfigurer} instance.
     *
     * @param properties                    the batch properties
     * @param dataSource                    the underlying data source
     * @param transactionManagerCustomizers transaction manager customizers (or
     *                                      {@code null})
     */
    protected CustomBatchConfigurer(BatchProperties properties, DataSource dataSource, TransactionManagerCustomizers transactionManagerCustomizers) {
        super(properties, dataSource, transactionManagerCustomizers);
    }

    // set batchjob base repository(JobRepository)
    @SneakyThrows
    @Override
    public JobRepository getJobRepository() {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();

        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setTransactionManager(new JpaTransactionManager(entityManagerFactory));

//        jobRepositoryFactoryBean.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
//        jobRepositoryFactoryBean.setTablePrefix("KCD_");

        return jobRepositoryFactoryBean.getObject();
    }
}
