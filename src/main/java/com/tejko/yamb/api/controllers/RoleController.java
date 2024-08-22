package com.tejko.yamb.api.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.payload.responses.RoleResponse;
import com.tejko.yamb.interfaces.BaseController;
import com.tejko.yamb.interfaces.services.RoleService;
import com.tejko.yamb.util.PayloadMapper;

@RestController
@RequestMapping("/api/roles")
public class RoleController implements BaseController<RoleResponse> {

	@Autowired
	private RoleService roleService;

	@Autowired
	private PayloadMapper mapper;

	@GetMapping("/{externalId}")
	public RoleResponse getByExternalId(@PathVariable UUID externalId) {
		return mapper.toDTO(roleService.getByExternalId(externalId));
	}

	@GetMapping("")
	public List<RoleResponse> getAll(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "createdAt") String sort,
			@RequestParam(defaultValue = "desc") String direction) {
		return roleService.getAll(page, size, sort, direction)
				.stream()
				.map(mapper::toDTO)
				.collect(Collectors.toList());
	}

	@DeleteMapping("/{externalId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteByExternalId(@PathVariable UUID externalId) {
		roleService.deleteByExternalId(externalId);
	}

}
