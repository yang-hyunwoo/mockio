package com.mockio.user_service.controller;

import com.mockio.user_service.kafka.service.OutboxAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/outbox")
public class OutboxAdminController {

    private final OutboxAdminService outboxAdminService;

    @PostMapping("/user-events/retry-dead")
    public ResponseEntity<String> retryDead(@RequestParam(defaultValue = "50") int max) {
        int retried = outboxAdminService.retryDeadEvents(max);
        return ResponseEntity.ok("retried=" + retried);
    }
}
