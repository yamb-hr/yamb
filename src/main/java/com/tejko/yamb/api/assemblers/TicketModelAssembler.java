package com.tejko.yamb.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.controllers.PlayerController;
import com.tejko.yamb.api.controllers.TicketController;
import com.tejko.yamb.api.dto.requests.TicketRequest;
import com.tejko.yamb.api.dto.responses.TicketResponse;
import com.tejko.yamb.business.interfaces.PlayerService;
import com.tejko.yamb.domain.models.Ticket;

@Component
public class TicketModelAssembler implements RepresentationModelAssembler<Ticket, TicketResponse> {

    private final ModelMapper modelMapper;
    private final PlayerService playerService;

    @Autowired
    public TicketModelAssembler(ModelMapper modelMapper, PlayerService playerService) {
        this.modelMapper = modelMapper;
        this.playerService = playerService;
    }

    @Override
    public TicketResponse toModel(Ticket ticket) {
        
        TicketResponse ticketResponse = modelMapper.map(ticket, TicketResponse.class);
		ticketResponse.add(linkTo(methodOn(TicketController.class).getByExternalId(ticketResponse.getId())).withSelfRel());
        if (ticketResponse.getPlayer() != null) ticketResponse.getPlayer().add(linkTo(methodOn(PlayerController.class).getByExternalId(ticketResponse.getPlayer().getId())).withSelfRel());

        return ticketResponse;
    }

    public PagedModel<TicketResponse> toPagedModel(Page<Ticket> tickets) {

        List<TicketResponse> ticketResponses = tickets.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        PagedModel<TicketResponse> pagedTickets = PagedModel.of(ticketResponses, new PagedModel.PageMetadata(
            tickets.getSize(), tickets.getNumber(), tickets.getTotalElements(), tickets.getTotalPages()
        ));

        return pagedTickets;
    }

    public Ticket fromModel(TicketRequest ticketRequest) {
        Ticket ticket = modelMapper.map(ticketRequest, Ticket.class);
        ticket.setPlayer(playerService.getByExternalId(ticketRequest.getPlayerId()));
        return ticket;
    }

}
