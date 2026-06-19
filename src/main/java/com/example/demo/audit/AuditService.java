package com.example.demo.audit;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(
            AuditLogRepository auditLogRepository) {

        this.auditLogRepository = auditLogRepository;
    }

    public void log(
            String username,
            String action) {

        AuditLog auditLog =
                new AuditLog(username, action);

        auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAllLogs() {

        return auditLogRepository.findAll();
    }

    public List<AuditLog> getLogsByUser(
            String username) {

        return auditLogRepository.findByUsername(
                username
        );
    }

    public List<AuditLog> getLogsByAction(
            String action) {

        return auditLogRepository.findByAction(
                action
        );
    }
}
