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

import com.tejko.yamb.api.assemblers.LogDetailModelAssembler;
import com.tejko.yamb.api.assemblers.LogModelAssembler;
import com.tejko.yamb.api.dto.responses.LogDetailResponse;
import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.business.interfaces.LogService;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.util.SortFieldTranslator;

@RestController
@RequestMapping("/api/logs")
public class LogController {

	private final LogService logService;
	private final LogModelAssembler logModelAssembler;
	private final LogDetailModelAssembler logDetailModelAssembler;
	private final SortFieldTranslator sortFieldTranslator;

	@Autowired
	public LogController(LogService logService, LogModelAssembler logModelAssembler, 
						 LogDetailModelAssembler logDetailModelAssembler, SortFieldTranslator sortFieldTranslator) {
		this.logService = logService;
		this.logModelAssembler = logModelAssembler;
		this.logDetailModelAssembler = logDetailModelAssembler;
		this.sortFieldTranslator = sortFieldTranslator;
	}

	@GetMapping("/{externalId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<LogDetailResponse> getByExternalId(@PathVariable UUID externalId) {
		LogDetailResponse logDetailResponse = logDetailModelAssembler.toModel(logService.getByExternalId(externalId));
		return ResponseEntity.ok(logDetailResponse);
	}

	@GetMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<PagedModel<LogResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Pageable modifiedPageable = sortFieldTranslator.translateSortField(pageable, Log.class, LogResponse.class);
		PagedModel<LogResponse> pagedLogs = logModelAssembler.toPagedModel(logService.getAll(modifiedPageable));
		return ResponseEntity.ok(pagedLogs);
	}

	@DeleteMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deleteAll() {
		logService.deleteAll();
		return ResponseEntity.noContent()
			.location(linkTo(methodOn(LogController.class).getAll(Pageable.unpaged())).toUri())
			.build();
	}
	
}
