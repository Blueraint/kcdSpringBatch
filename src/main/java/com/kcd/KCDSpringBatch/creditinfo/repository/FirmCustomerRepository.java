package com.kcd.KCDSpringBatch.creditinfo.repository;

import com.kcd.KCDSpringBatch.creditinfo.domain.FirmCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirmCustomerRepository extends JpaRepository<FirmCustomer, Long> {

}
