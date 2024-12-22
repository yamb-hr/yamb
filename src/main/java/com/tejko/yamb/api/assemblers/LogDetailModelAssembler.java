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
import com.tejko.yamb.api.dto.responses.LogDetailResponse;
import com.tejko.yamb.domain.models.Log;

@Component
public class LogDetailModelAssembler implements RepresentationModelAssembler<Log, LogDetailResponse> {

    private final ModelMapper modelMapper;

    @Autowired
    public LogDetailModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public LogDetailResponse toModel(Log log) {
        
        LogDetailResponse logDetailResponse = modelMapper.map(log, LogDetailResponse.class);
		logDetailResponse.add(linkTo(methodOn(LogController.class).getByExternalId(logDetailResponse.getId())).withSelfRel());
        if (logDetailResponse.getPlayer() != null) logDetailResponse.getPlayer().add(linkTo(methodOn(PlayerController.class).getByExternalId(logDetailResponse.getPlayer().getId())).withSelfRel());

        return logDetailResponse;
    }

    public PagedModel<LogDetailResponse> toPagedModel(Page<Log> logs) {

        List<LogDetailResponse> logDetailResponses = logs.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        PagedModel<LogDetailResponse> pagedLogs = PagedModel.of(logDetailResponses, new PagedModel.PageMetadata(
            logs.getSize(), logs.getNumber(), logs.getTotalElements(), logs.getTotalPages()
        ));

        return pagedLogs;
    }

}
