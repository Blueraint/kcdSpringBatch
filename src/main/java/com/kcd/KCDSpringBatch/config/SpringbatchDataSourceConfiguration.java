package com.kcd.KCDSpringBatch.config;

import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * SpringBoot Datasource Configuration(Bean) - SpringBatch Metadata Database (@primary Datasource)
 * ConfigurationProperties is pulled by springboot properties(in case, resources/application.yml -> spring -> springbatch Database -> datasource -> prifix (k,v))
 * Set JpaRepositories for @EnableJpaRepositories (entity manager factory, transaction manager, ...)
 */

@Configuration
@ConfigurationProperties(prefix = "spring.springbatch.datasource")
@EnableJpaRepositories(
        entityManagerFactoryRef = "springbatchEntityManagerFactory",
        transactionManagerRef = "springbatchTransactionManager",
        basePackages = {
                // batch config metadata
                "com.kcd.KCDSpringBatch.springbatch",
                // quartz config metadata
                "com.kcd.KCDSpringBatch.quartz"
        }
)
public class SpringbatchDataSourceConfiguration extends HikariConfig {
    /**
     * HikariDataSource constructure 반환
     * Multi Resource 체계에서 Start하는 Datasource Bean 이 최상위 bean으로 등록될 수 있도록 @Primary 지정
     * Batch Schema가 저장되어 있는 Database를 @Primary 로 참조
     */
    @Bean(name = "springbatchDataSource")
    @BatchDataSource
    @Primary
    public DataSource springbatchDataSource() {
        return new LazyConnectionDataSourceProxy(new HikariDataSource(this));
    }

    /*
     * JPA Entity Manager Factory 구현체 반환
     */
    @Bean(name = "springbatchEntityManagerFactory")
    @Primary
    public EntityManagerFactory springbatchEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(springbatchDataSource());
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        /* set spring JPA persistence.xml Map
         * springboot properties set abstractly entitymanager
         */
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(ImmutableMap.of(
                "hibernate.hbm2ddl.auto","none",
//                "hibernate.dialect","org.hibernate.dialect.MySQL8Dialect",
                "hibernate.dialect","org.hibernate.dialect.H2Dialect",
                "hibernate.show_sql","true",
                "hibernate.physical_naming_strategy","org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy",
                "hibernate.implicit_naming_strategy","org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy"
        ));

        //domain package
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.kcd.KCDSpringBatch.springbatch", "com.kcd.KCDSpringBatch.quartz");
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("springbatch");
        localContainerEntityManagerFactoryBean.afterPropertiesSet();

        return localContainerEntityManagerFactoryBean.getObject();
    }

    @Bean(name = "springbatchTransactionManager")
    @Primary
    public PlatformTransactionManager springbatchTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(springbatchEntityManagerFactory());

        return jpaTransactionManager;
    }
}
