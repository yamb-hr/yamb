package com.tejko.yamb.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.services.LogService;
import com.tejko.yamb.models.payload.dto.LogDTO;
import com.tejko.yamb.util.Mapper;

@RestController
@RequestMapping("/api/logs")
public class LogController {

	@Autowired
	LogService logService;

	@Autowired
	Mapper mapper;

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public LogDTO getById(@PathVariable Long id) {
		return mapper.toDTO(logService.getById(id));
	}

	@GetMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<LogDTO> getAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "desc") String direction) {
		return logService.getAll(page, size, sort, direction)
			.stream()
			.map(mapper::toDTO)
			.collect(Collectors.toList());
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
