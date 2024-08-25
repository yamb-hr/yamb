package com.tejko.yamb.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.domain.services.interfaces.LogService;

@RestController
@RequestMapping("/api/logs")
public class LogController {

	private final LogService logService;

	@Autowired
	public LogController(LogService logService) {
		this.logService = logService;
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public LogResponse getById(@PathVariable Long id) {
		return logService.getById(id);
	}

	@GetMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<LogResponse> getAll() {
		return logService.getAll();
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteById(@PathVariable Long id) {
		logService.deleteById(id);
	}

	@DeleteMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteAll() {
		logService.deleteAll();
	}

}
