package com.kcd.KCDSpringBatch.config;

import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * SpringBoot Datasource Configuration(Bean) - Creditinfo Database
 * ConfigurationProperties is pulled by springboot properties(in case, resources/application.yml -> spring -> creditinfo Database -> datasource -> prifix (k,v))
 * Set JpaRepositories for @EnableJpaRepositories (entity manager factory, transaction manager, ...)
 */

@Configuration
@ConfigurationProperties(prefix = "spring.creditinfo.datasource")
@EnableJpaRepositories(
        entityManagerFactoryRef = "creditinfoEntityManagerFactory",
        transactionManagerRef = "creditinfoTransactionManager",
        basePackages = {"com.kcd.KCDSpringBatch.creditinfo"}
)
public class CreditinfoDataSourceConfiguration extends HikariConfig {
    /**
     * HikariDataSource constructure 반환
     * Multi Resource 체계에서 Start하는 Datasource Bean 이 최상위 bean으로 등록될 수 있도록 @Primary 지정
     * Batch Schema가 저장되어 있는 Database를 @Primary 로 참조
     */
    @Bean(name = "creditinfoDataSource")
//    @Primary
    public DataSource creditinfoDataSource() {
        return new LazyConnectionDataSourceProxy(new HikariDataSource(this));
    }

    /*
     * JPA Entity Manager Factory 구현체 반환
     */
    @Bean(name = "creditinfoEntityManagerFactory")
//    @Primary
    public EntityManagerFactory creditinfoEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(creditinfoDataSource());
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        /* set spring JPA persistence.xml Map
         * springboot properties set abstractly entitymanager
         */
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(ImmutableMap.of(
                "hibernate.hbm2ddl.auto","none",
                "hibernate.dialect","org.hibernate.dialect.MySQL8Dialect",
                "default_batch_fetch_size",100,
                "hibernate.show_sql","false"
        ));

        //domain package
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.kcd.KCDSpringBatch.creditinfo");
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("creditinfo");
        localContainerEntityManagerFactoryBean.afterPropertiesSet();

        return localContainerEntityManagerFactoryBean.getObject();
    }

    @Bean(name = "creditinfoTransactionManager")
//    @Primary
    public PlatformTransactionManager creditinfoTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(creditinfoEntityManagerFactory());

        return jpaTransactionManager;
    }

}
