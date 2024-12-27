package com.tejko.yamb.api.controllers;

import java.net.URI;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tejko.yamb.util.SortFieldTranslator;
import com.tejko.yamb.api.assemblers.NotificationModelAssembler;
import com.tejko.yamb.api.dto.requests.NotificationRequest;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.api.dto.responses.NotificationResponse;
import com.tejko.yamb.business.interfaces.NotificationService;
import com.tejko.yamb.domain.models.Game;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	private final NotificationService notificationService;
	private final NotificationModelAssembler notificationModelAssembler;
	private final SortFieldTranslator sortFieldTranslator;

	@Autowired
	public NotificationController(NotificationService notificationService, NotificationModelAssembler notificationModelAssembler, 
								  SortFieldTranslator sortFieldTranslator) {
		this.notificationService = notificationService;
		this.notificationModelAssembler = notificationModelAssembler;
		this.sortFieldTranslator = sortFieldTranslator;
	}

	@GetMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<PagedModel<NotificationResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Pageable modifiedPageable = sortFieldTranslator.translateSortField(pageable, Game.class, GameResponse.class);
		PagedModel<NotificationResponse> pagedNotifications = notificationModelAssembler.toPagedModel(notificationService.getAll(modifiedPageable));
		return ResponseEntity.ok(pagedNotifications);
	}

	@PostMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<NotificationResponse> create(@RequestBody @Valid NotificationRequest notificationRequest) {
		NotificationResponse notificationResponse = notificationModelAssembler.toModel(notificationService.create(notificationRequest.getPlayerId(), notificationRequest.getContent(), notificationRequest.getLink(), notificationRequest.getType()));
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{externalId}")
			.buildAndExpand(notificationResponse.getId())
			.toUri();
		return ResponseEntity.created(location).body(notificationResponse);
	}

	@GetMapping("/{externalId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<NotificationResponse> getByExternalId(@PathVariable UUID externalId) {
		NotificationResponse notificationResponse = notificationModelAssembler.toModel(notificationService.getByExternalId(externalId));
		return ResponseEntity.ok(notificationResponse);
	}

	@DeleteMapping("/{externalId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> deleteByExternalId(@PathVariable UUID externalId) {
		notificationService.deleteByExternalId(externalId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deleteAll() {
		notificationService.deleteAll();
		return ResponseEntity.noContent().build();
	}
		
}
