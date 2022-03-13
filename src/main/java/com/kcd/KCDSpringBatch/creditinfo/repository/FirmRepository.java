package com.kcd.KCDSpringBatch.creditinfo.repository;

import com.kcd.KCDSpringBatch.creditinfo.domain.Firm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirmRepository extends JpaRepository<Firm, Long> {


}
