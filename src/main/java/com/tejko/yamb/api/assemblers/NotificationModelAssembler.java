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

import com.tejko.yamb.api.controllers.NotificationController;
import com.tejko.yamb.api.dto.responses.NotificationResponse;
import com.tejko.yamb.domain.models.Notification;

@Component
public class NotificationModelAssembler implements RepresentationModelAssembler<Notification, NotificationResponse> {

    private final ModelMapper modelMapper;

    @Autowired
    public NotificationModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public NotificationResponse toModel(Notification notification) {
        
        NotificationResponse notificationResponse = modelMapper.map(notification, NotificationResponse.class);
        notificationResponse.add(linkTo(methodOn(NotificationController.class).getByExternalId(notificationResponse.getId())).withSelfRel());

        return notificationResponse;
    }

    public PagedModel<NotificationResponse> toPagedModel(Page<Notification> notifications) {

        List<NotificationResponse> notificationResponses = notifications.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        PagedModel<NotificationResponse> pagedNotifications = PagedModel.of(notificationResponses, new PagedModel.PageMetadata(
            notifications.getSize(), notifications.getNumber(), notifications.getTotalElements(), notifications.getTotalPages()
        ));

        return pagedNotifications;
    }
}
