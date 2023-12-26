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

import com.tejko.yamb.api.services.RoleService;
import com.tejko.yamb.models.payload.dto.RoleDTO;
import com.tejko.yamb.util.Mapper;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	@Autowired
	RoleService roleService;

	@Autowired
	Mapper mapper;

	@GetMapping("/{id}")
	public RoleDTO getById(@PathVariable Long id) {
		return mapper.toDTO(roleService.getById(id));
	}

	@GetMapping("")
	public List<RoleDTO> getAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "desc") String direction) {
		return roleService.getAll(page, size, sort, direction)
			.stream()
			.map(mapper::toDTO)
			.collect(Collectors.toList());
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteById(@PathVariable Long id) {
		roleService.deleteById(id);
	}

}
