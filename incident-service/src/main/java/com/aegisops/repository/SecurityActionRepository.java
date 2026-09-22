package com.aegisops.repository;

import com.aegisops.entity.SecurityAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecurityActionRepository
        extends JpaRepository<SecurityAction, Long> {

    List<SecurityAction> findByIncidentId(Long incidentId);
}