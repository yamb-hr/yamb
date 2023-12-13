package com.tejko.yamb.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.services.RoleService;
import com.tejko.yamb.models.Role;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	@Autowired
	RoleService roleService;

	@GetMapping("/{id}")
	public ResponseEntity<Role> getById(@PathVariable Long id) {
		return new ResponseEntity<>(roleService.getById(id), HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<List<Role>> getAll(@RequestParam(defaultValue = "0") Integer page,
	@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sort,
	@RequestParam(defaultValue = "desc") String direction) {
		return new ResponseEntity<>(roleService.getAll(page, size, sort, direction), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteById(@PathVariable Long id) {
		roleService.deleteById(id);
	}

}
