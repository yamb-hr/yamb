package com.tejko.yamb.api.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.assemblers.LogModelAssembler;
import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.business.interfaces.LogService;

@RestController
@RequestMapping("/api/logs")
public class LogController {

	private final LogService logService;
	private final LogModelAssembler logModelAssembler;

	@Autowired
	public LogController(LogService logService, LogModelAssembler logModelAssembler) {
		this.logService = logService;
		this.logModelAssembler = logModelAssembler;
	}

	@GetMapping("/{externalId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<LogResponse> getByExternalId(@PathVariable UUID externalId) {
		LogResponse logResponse = logModelAssembler.toModel(logService.getByExternalId(externalId));
		return ResponseEntity.ok(logResponse);
	}

	@GetMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<PagedModel<LogResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		PagedModel<LogResponse> pagedLogs = logModelAssembler.toPagedModel(logService.getAll(pageable));
		return ResponseEntity.ok(pagedLogs);
	}

	@DeleteMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deleteAll() {
		logService.deleteAll();
		return ResponseEntity.noContent()
			.location(linkTo(methodOn(LogController.class).getAll(null)).toUri())
			.build();
	}
	
}
