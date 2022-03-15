package com.kcd.KCDSpringBatch;

import com.kcd.KCDSpringBatch.creditinfo.domain.CardDetailInfo;
import com.kcd.KCDSpringBatch.creditinfo.domain.Customer;
import com.kcd.KCDSpringBatch.creditinfo.domain.Firm;
import com.kcd.KCDSpringBatch.creditinfo.domain.FirmCustomer;
import com.kcd.KCDSpringBatch.creditinfo.repository.CardDetailInfoRepository;
import com.kcd.KCDSpringBatch.creditinfo.repository.CustomerRepository;
import com.kcd.KCDSpringBatch.creditinfo.repository.FirmCustomerRepository;
import com.kcd.KCDSpringBatch.creditinfo.repository.FirmRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import static java.time.LocalDateTime.now;

@SpringBootTest
@Transactional
@Commit
public class RepositoryTest {
    @Autowired
    CardDetailInfoRepository cardDetailInfoRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    FirmRepository firmRepository;

    @Autowired
    FirmCustomerRepository firmCustomerRepository;

    @PersistenceContext
    EntityManager entityManager;

    static Random random = new Random();

    @Test
    public void cardRepositoryTest() {

    }

    @Test
    public void customerRepositoryTest() {
        Customer customer = new Customer();
        customer.setCustomerNumber("1111111111111");
        customer.setRating(123);
        customer.setCodeGender("F");
        customer.setLastModifiedTime(now());

        Optional<Firm> firm = firmRepository.findById(1L);

//        if(firm.isPresent()) customer.setFirm(firm.get());

        customerRepository.save(customer);
    }

    @Test
    @Transactional
    public void firmRepositoryTest() {
        Firm firm = new Firm();
        firm.setFirmCode("0004");

        firmRepository.save(firm);

        List<FirmCustomer> firmCustomers = new ArrayList<>();
        List<Customer> customerList = customerRepository.findAll(PageRequest.of(0,1000)).getContent();

        customerList.forEach(customer -> {
            FirmCustomer firmCustomer = new FirmCustomer();
            firmCustomer.setCustomer(customer);
            firmCustomer.setFirm(firm);

            firmCustomers.add(firmCustomer);
        });

        firmCustomerRepository.saveAll(firmCustomers);

//        Assertions.assertThat(firmRepository.findById(1L).get().getFirmCode()).isEqualTo("0001");
    }

    @Test
    public void findCustomerTest() {
        customerRepository.findAll(PageRequest.of(0,20)).getContent().forEach(customer -> System.out.println("@@@CUSTOMER : " + customer.toString()));
    }

    @Test
    public void insertData() {
        

        insertCustomerData();
        insertFirmData();
        insertCardDetailInfoData();
    }


    public void insertCardDetailInfoData() {
        int cardInfoSize = 100;
        List<CardDetailInfo> cardDetailInfoList = new ArrayList<>();

        List<Customer> customerList = customerRepository.findAll();

        customerList.forEach(customer -> {
            CardDetailInfo cardDetailInfo = new CardDetailInfo();
            cardDetailInfo.setCustomer(customerList.get(random.nextInt(customerList.size())));
            cardDetailInfo.setTarget(random.nextInt(10000));
            cardDetailInfoList.add(cardDetailInfo);
        });

        cardDetailInfoRepository.saveAll(cardDetailInfoList);

        return;
    }


    public void insertFirmData() {
        int firmSize = 100;
        List<Firm> firmList = new ArrayList<>();

        IntStream.rangeClosed(1,firmSize).forEach(i -> {
            Firm firm = new Firm();

            firm.setFirmCode(String.valueOf(i));
//            firm.setCustomerList(setRandomCustomerList(random.nextInt(50)+10));

            firmList.add(firm);
        });

        firmRepository.saveAll(firmList);

        return;
    }


    public List<Customer> setRandomCustomerList(int size) {
        List<Customer> customerList = customerRepository.findAll();
        List<Customer> returnCustomerList = new ArrayList<>();

        System.out.println("@@@ customerList size : " + customerList.size());

        IntStream.range(0,size).forEach(i -> {
            returnCustomerList.add(customerList.get(random.nextInt(size)));
        });

        return returnCustomerList;
    }

    @Commit
    public void insertCustomerData() {
        int customerSize = 100;
        List<Customer> customerList = new ArrayList<>();

        IntStream.rangeClosed(1,customerSize).forEach(i -> {
            Customer customer = new Customer();
            customer.setCustomerNumber(createCustomerNumber(random));
            customer.setCodeGender("M");
            customer.setLastModifiedTime(now());

            customerList.add(customer);
        });

        IntStream.rangeClosed(1,customerSize).forEach(i -> {
            Customer customer = new Customer();
            customer.setCustomerNumber(createCustomerNumber(random));
            customer.setCodeGender("F");
            customer.setRating(random.nextInt(1000));
            customer.setLastModifiedTime(now());

            customerList.add(customer);
        });

        System.out.println("## customerList Save size : " + customerList.size());

        customerRepository.saveAll(customerList);

    }

    static String createCustomerNumber(Random random) {
        StringBuilder stringBuilder = new StringBuilder();
        IntStream.rangeClosed(1,13).forEach(i -> {
            stringBuilder.append(String.valueOf(random.nextInt(9)));
        });

        return stringBuilder.toString();
    }
}
