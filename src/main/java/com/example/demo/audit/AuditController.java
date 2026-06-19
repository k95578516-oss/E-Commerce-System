package com.example.demo.audit;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(
            AuditService auditService) {

        this.auditService = auditService;
    }

    @GetMapping
    public List<AuditLog> getAllLogs() {

        return auditService.getAllLogs();
    }

    @GetMapping("/user/{username}")
    public List<AuditLog> getByUser(
            @PathVariable String username) {

        return auditService.getLogsByUser(
                username
        );
    }

    @GetMapping("/action/{action}")
    public List<AuditLog> getByAction(
            @PathVariable String action) {

        return auditService.getLogsByAction(
                action
        );
    }
}
