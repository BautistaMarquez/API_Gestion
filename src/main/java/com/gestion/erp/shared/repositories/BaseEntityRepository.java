package com.gestion.erp.shared.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.erp.shared.models.BaseEntity;

@Repository
public interface BaseEntityRepository extends JpaRepository<BaseEntity, Long> {
    
}
