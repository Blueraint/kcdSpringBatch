package com.kcd.KCDSpringBatch;

import com.kcd.KCDSpringBatch.config.SpringbatchDataSourceConfiguration;
import com.kcd.KCDSpringBatch.dto.BatchParam;
import com.kcd.KCDSpringBatch.springbatch.domain.BatchJobExecution;
import com.kcd.KCDSpringBatch.springbatch.repository.BatchJobExecutionRepository;
import com.kcd.KCDSpringBatch.springbatch.repository.BatchJobInstanceRepository;
import com.kcd.KCDSpringBatch.util.DateUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public class SpringBatchRepositoryTest {
    @Autowired
    BatchJobExecutionRepository batchJobExecutionRepository;

    @Autowired
    BatchJobInstanceRepository batchJobInstanceRepository;

    @Resource(name = "springbatchEntityManagerFactory")
//    @Qualifier("springbatchEntityManagerFactory")
    private EntityManagerFactory springbatchEntityManagerFactory;

    @Test
    public void batchJobExecutionRepositoryTest() {

        batchJobExecutionRepository.findAll().forEach(batchJobExecution -> {
            System.out.println("@@@ " + batchJobExecution.toString());
//            batchJobExecution.getBatchJobExecutionParamsList().forEach(p -> {
//                System.out.println("\t$$$ Params : " + p.toString());
//            });
        });

    }

    @Test
    public void test2() {
//        List<BatchJobExecution> batchJobExecution1 =batchJobExecutionRepository.findByBatchJobExecutionParamsListKeyNameAndBatchJobExecutionParamsListStringVal("firmCode","0001");
//        batchJobExecution1.forEach(p -> {
//            System.out.println("### firmCode executon : " + p.toString());
//            p.getBatchJobExecutionParamsList().forEach(params -> System.out.println("\t >>> batchParams Collections : " + params.toString()));
//            System.out.println("\t\t * batchParams obj : " + p.getBatchParam().toString());
//        });

        LocalDateTime startTime = LocalDateTime.now().minusHours(48);
        LocalDateTime endTime = LocalDateTime.now().minusHours(28);

        batchJobExecutionRepository.findByCreateTimeGreaterThanEqualAndBatchJobExecutionParamsListKeyNameAndBatchJobExecutionParamsListStringVal(startTime, "firmCode","0001").forEach(p-> System.out.println("@@@" + p.toString()));
        batchJobExecutionRepository.findByCreateTimeBetweenAndBatchJobExecutionParamsListKeyNameAndBatchJobExecutionParamsListStringVal(startTime, endTime, "firmCode","0001").forEach(p-> System.out.println("@@@" + p.toString()));

//        List<BatchJobExecution> batchJobExecution2 = batchJobExecutionRepository.findByBatchJobParam("firmCode","0001");

//        Assert.assertEquals(batchJobExecution1, batchJobExecution2);
    }

    @Test
    public void test3() {

    }

    @Test
    public void batchJobInstanceRepositoryTest() {
        System.out.println(DateUtil.getCurrentDate());
    }
}
