package com.example.accountservice.repositories;

import com.example.accountservice.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {
}
