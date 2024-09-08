package com.tejko.yamb.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.business.interfaces.LogService;

@RestController
@RequestMapping("/api/logs")
public class LogController {

	private final LogService logService;
	private final ModelMapper modelMapper;

	@Autowired
	public LogController(LogService logService, ModelMapper modelMapper) {
		this.logService = logService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<LogResponse> getById(@PathVariable Long id) {
		LogResponse logResponse = modelMapper.map(logService.getById(id), LogResponse.class);
		return ResponseEntity.ok(logResponse);
	}

	@GetMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<LogResponse>> getAll() {
		List<LogResponse> logResponses = logService.getAll().stream().map(log -> modelMapper.map(log, LogResponse.class)).collect(Collectors.toList());
		return ResponseEntity.ok(logResponses);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		logService.deleteById(id);
    	return ResponseEntity.noContent().build();
	}

	@DeleteMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deleteAll() {
		logService.deleteAll();
    	return ResponseEntity.noContent().build();
	}

}
