package com.kcd.KCDSpringBatch.creditinfo.repository;

import com.kcd.KCDSpringBatch.creditinfo.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
