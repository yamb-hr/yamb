package com.tejko.yamb.api.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.Map;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tejko.yamb.api.assemblers.TicketModelAssembler;
import com.tejko.yamb.api.dto.requests.TicketRequest;
import com.tejko.yamb.api.dto.responses.TicketResponse;
import com.tejko.yamb.business.interfaces.TicketService;
import com.tejko.yamb.domain.models.Ticket;
import com.tejko.yamb.util.SortFieldTranslator;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

	private final TicketService ticketService;
	private final TicketModelAssembler ticketModelAssembler;
	private final SortFieldTranslator sortFieldTranslator;

	@Autowired
	public TicketController(TicketService ticketService, TicketModelAssembler ticketModelAssembler, SortFieldTranslator sortFieldTranslator) {
		this.ticketService = ticketService;
		this.ticketModelAssembler = ticketModelAssembler;
		this.sortFieldTranslator = sortFieldTranslator;
	}

	@GetMapping("/{externalId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<TicketResponse> getByExternalId(@PathVariable UUID externalId) {
		TicketResponse ticketResponse = ticketModelAssembler.toModel(ticketService.getByExternalId(externalId));
		return ResponseEntity.ok(ticketResponse);
	}

	@GetMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<PagedModel<TicketResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Pageable modifiedPageable = sortFieldTranslator.translateSortField(pageable, Ticket.class, TicketResponse.class);
		PagedModel<TicketResponse> pagedTickets = ticketModelAssembler.toPagedModel(ticketService.getAll(modifiedPageable));
		return ResponseEntity.ok(pagedTickets);
	}

	@PostMapping("")	
	@PreAuthorize("isAuthenticated() and (#ticketRequest.playerId == principal.externalId or hasAuthority('ADMIN'))")
	public ResponseEntity<TicketResponse> create(@Valid @RequestBody TicketRequest ticketRequest) {
		TicketResponse ticketResponse = ticketModelAssembler.toModel(ticketService.create(ticketRequest.getEmailAddresses(), ticketRequest.getTitle(), ticketRequest.getDescription()));
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{externalId}")
			.buildAndExpand(ticketResponse.getId())
			.toUri();
		return ResponseEntity.created(location).body(ticketResponse);
	}

	@PatchMapping("/{externalId}")
	@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TicketResponse> patchTicket(@PathVariable UUID externalId, @RequestBody Map<String, Object> updates) {
		TicketResponse ticketResponse = ticketModelAssembler.toModel(ticketService.patchByExternalId(externalId, updates));
        return ResponseEntity.ok(ticketResponse);
    }

	@DeleteMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deleteAll() {
		ticketService.deleteAll();
		return ResponseEntity.noContent()
			.location(linkTo(methodOn(TicketController.class).getAll(Pageable.unpaged())).toUri())
			.build();
	}
	
}
