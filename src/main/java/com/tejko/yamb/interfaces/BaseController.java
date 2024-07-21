package com.tejko.yamb.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.tejko.yamb.api.payload.responses.BaseDTO;

public interface BaseController<T extends BaseDTO> {

    @GetMapping("")
	public List<T> getAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "createdAt") String sort, @RequestParam(defaultValue = "desc") String direction);

	@GetMapping("/{externalId}")
	public T getByExternalId(@PathVariable UUID externalId);

	@DeleteMapping("/{externalId}")
	public void deleteByExternalId(@PathVariable UUID externalId);
    
}
