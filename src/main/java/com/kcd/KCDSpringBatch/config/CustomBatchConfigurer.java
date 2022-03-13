package com.kcd.KCDSpringBatch.config;

import lombok.SneakyThrows;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Batch Configuration
 */

//@Configuration
public class CustomBatchConfigurer extends BasicBatchConfigurer  {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * Create a new {@link BasicBatchConfigurer} instance.
     *
     * @param properties                    the batch properties
     * @param dataSource                    the underlying data source
     * @param transactionManagerCustomizers transaction manager customizers (or
     *                                      {@code null})
     */

    // set constructor
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
