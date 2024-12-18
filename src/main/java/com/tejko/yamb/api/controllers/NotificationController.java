package com.tejko.yamb.api.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.assemblers.NotificationModelAssembler;
import com.tejko.yamb.api.dto.responses.NotificationResponse;
import com.tejko.yamb.business.interfaces.NotificationService;

@RestController
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationModelAssembler notificationModelAssembler;

    @Autowired
    public NotificationController(NotificationService notificationService, NotificationModelAssembler notificationModelAssembler) {
        this.notificationService = notificationService;
        this.notificationModelAssembler = notificationModelAssembler;
    }

    @GetMapping("/{externalId}")
	@PreAuthorize("isAuthenticated() and (#externalId == principal.externalId or hasAuthority('ADMIN'))")
    public ResponseEntity<NotificationResponse> getByExternalId(@PathVariable UUID externalId) {
		NotificationResponse notificationResponse = notificationModelAssembler.toModel(notificationService.getByExternalId(externalId));
		return ResponseEntity.ok(notificationResponse);
    }

	@DeleteMapping("/{externalId}")
	@PreAuthorize("isAuthenticated() and (#externalId == principal.externalId or hasAuthority('ADMIN'))")
    public ResponseEntity<Void> deleteByExternalId(@PathVariable UUID externalId) {
		notificationService.deleteByExternalId(externalId);
		return ResponseEntity.noContent().build();
    }
    
}
