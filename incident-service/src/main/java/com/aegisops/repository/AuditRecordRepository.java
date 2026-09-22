package com.aegisops.repository;

import com.aegisops.entity.AuditRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRecordRepository
        extends JpaRepository<AuditRecord, Long> {

    List<AuditRecord> findByIncidentId(Long incidentId);
}