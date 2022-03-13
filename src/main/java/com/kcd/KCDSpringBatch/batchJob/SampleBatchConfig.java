package com.kcd.KCDSpringBatch.batchJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableBatchProcessing
public class SampleBatchConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public Step stepOne(){
        return steps.get("stepOne")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("!!!STEPONE...");
                    Thread.sleep(1000);
                    System.out.println("!!!STEPONE...END...");
                    return null;
                })
                .build();
    }

    @Bean
    public Step stepTwo(){
        return steps.get("stepTwo")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("@@@STEPTWO...");
                    Thread.sleep(1000);
                    System.out.println("@@@STEPTWO...END...");
                    return null;
                })
                .build();
    }

    @Bean(name="demoJobOne")
    public Job demoJobOne(){
        return jobs.get("demoJobOne")
                .start(stepOne())
                .next(stepTwo())
                .build();
    }

    @Bean(name="demoJobTwo")
    public Job demoJobTwo(){
        return jobs.get("demoJobTwo")
                .flow(stepOne())
                .build()
                .build();
    }
}
