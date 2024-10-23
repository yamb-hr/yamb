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

import com.tejko.yamb.api.controllers.LogController;
import com.tejko.yamb.api.controllers.PlayerController;
import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.domain.models.Log;

@Component
public class LogModelAssembler implements RepresentationModelAssembler<Log, LogResponse> {

    private final ModelMapper modelMapper;

    @Autowired
    public LogModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public LogResponse toModel(Log log) {
        
        LogResponse logResponse = modelMapper.map(log, LogResponse.class);
		logResponse.add(linkTo(methodOn(LogController.class).getByExternalId(logResponse.getId())).withSelfRel());
		if (logResponse.getPlayer() != null) logResponse.getPlayer().add(linkTo(methodOn(PlayerController.class).getByExternalId(logResponse.getPlayer().getId())).withSelfRel());
        
        return logResponse;
    }

    public PagedModel<LogResponse> toPagedModel(Page<Log> logs) {

        List<LogResponse> logResponses = logs.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        PagedModel<LogResponse> pagedLogs = PagedModel.of(logResponses, new PagedModel.PageMetadata(
            logs.getSize(), logs.getNumber(), logs.getTotalElements(), logs.getTotalPages()
        ));

        return pagedLogs;
    }

}
