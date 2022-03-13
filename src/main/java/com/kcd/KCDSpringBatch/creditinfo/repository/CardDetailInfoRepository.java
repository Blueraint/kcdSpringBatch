package com.kcd.KCDSpringBatch.creditinfo.repository;

import com.kcd.KCDSpringBatch.creditinfo.domain.CardDetailInfo;
import com.kcd.KCDSpringBatch.creditinfo.domain.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardDetailInfoRepository extends JpaRepository<CardDetailInfo, Long> {

    @EntityGraph(attributePaths = {"customer"})
    List<CardDetailInfo> findCardDetailInfoByCustomer(@Param("customer") Customer customer);

}
