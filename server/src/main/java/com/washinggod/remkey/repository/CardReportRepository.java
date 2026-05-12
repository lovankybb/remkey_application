package com.washinggod.remkey.repository;

import com.washinggod.remkey.entity.CardReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardReportRepository extends JpaRepository<CardReport, Long> {
}
