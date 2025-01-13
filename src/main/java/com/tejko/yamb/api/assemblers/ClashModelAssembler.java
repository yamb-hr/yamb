package com.tejko.yamb.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.controllers.ClashController;
import com.tejko.yamb.api.controllers.PlayerController;
import com.tejko.yamb.api.dto.responses.ClashResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.business.interfaces.PlayerService;
import com.tejko.yamb.domain.models.Clash;
import com.tejko.yamb.domain.models.Player;

@Component
public class ClashModelAssembler implements RepresentationModelAssembler<Clash, ClashResponse> {

    private final ModelMapper modelMapper;
    private final PlayerService playerService;

    @Autowired
    public ClashModelAssembler(ModelMapper modelMapper, PlayerService playerService) {
        this.modelMapper = modelMapper;
        this.playerService = playerService;
    }

    @Override
    public ClashResponse toModel(Clash clash) {
        Set<UUID> allPlayerIds = new HashSet<>();
        if (clash.getOwnerId() != null) {
            allPlayerIds.add(clash.getOwnerId());
        }
        if (clash.getWinnerId() != null) {
            allPlayerIds.add(clash.getWinnerId());
        }

        Map<UUID, Player> playerMap = playerService.findAllByExternalIds(allPlayerIds).stream()
            .collect(Collectors.toMap(Player::getExternalId, Function.identity()));

        ClashResponse clashResponse = modelMapper.map(clash, ClashResponse.class);

        if (clash.getOwnerId() != null) {
            Player owner = playerMap.get(clash.getOwnerId());
            if (owner != null) {
                PlayerResponse ownerResponse = modelMapper.map(owner, PlayerResponse.class);
                ownerResponse.add(linkTo(methodOn(PlayerController.class)
                        .getByExternalId(ownerResponse.getId()))
                        .withSelfRel());
                clashResponse.setOwner(ownerResponse);
            }
        }

        if (clash.getWinnerId() != null) {
            Player winner = playerMap.get(clash.getWinnerId());
            if (winner != null) {
                PlayerResponse winnerResponse = modelMapper.map(winner, PlayerResponse.class);
                winnerResponse.add(linkTo(methodOn(PlayerController.class)
                        .getByExternalId(winnerResponse.getId()))
                        .withSelfRel());
                clashResponse.setWinner(winnerResponse);
            }
        }

        clashResponse.add(linkTo(methodOn(ClashController.class).getByExternalId(clashResponse.getId())).withSelfRel());
        clashResponse.add(linkTo(methodOn(ClashController.class).acceptInvitationByExternalId(clashResponse.getId(), null)).withRel("accept"));
        clashResponse.add(linkTo(methodOn(ClashController.class).declineInvitationByExternalId(clashResponse.getId(), null)).withRel("decline"));

        return clashResponse;
    }

    public PagedModel<ClashResponse> toPagedModel(Page<Clash> clashes) {

        Set<UUID> allPlayerIds = new HashSet<>();
        for (Clash clash : clashes) {
            if (clash.getOwnerId() != null) {
                allPlayerIds.add(clash.getOwnerId());
            }
            if (clash.getWinnerId() != null) {
                allPlayerIds.add(clash.getWinnerId());
            }
        }

        Map<UUID, Player> playerMap = playerService.findAllByExternalIds(allPlayerIds).stream()
            .collect(Collectors.toMap(Player::getExternalId, Function.identity()));

        List<ClashResponse> clashResponses = clashes.stream()
            .map(clash -> {
                ClashResponse clashResponse = modelMapper.map(clash, ClashResponse.class);

                if (clash.getOwnerId() != null) {
                    Player owner = playerMap.get(clash.getOwnerId());
                    if (owner != null) {
                        PlayerResponse ownerResponse = modelMapper.map(owner, PlayerResponse.class);
                        ownerResponse.add(linkTo(methodOn(PlayerController.class)
                                .getByExternalId(ownerResponse.getId()))
                                .withSelfRel());
                        clashResponse.setOwner(ownerResponse);
                    }
                }

                if (clash.getWinnerId() != null) {
                    Player winner = playerMap.get(clash.getWinnerId());
                    if (winner != null) {
                        PlayerResponse winnerResponse = modelMapper.map(winner, PlayerResponse.class);
                        winnerResponse.add(linkTo(methodOn(PlayerController.class)
                                .getByExternalId(winnerResponse.getId()))
                                .withSelfRel());
                        clashResponse.setWinner(winnerResponse);
                    }
                }

                clashResponse.add(linkTo(methodOn(ClashController.class).getByExternalId(clashResponse.getId())).withSelfRel());
                clashResponse.add(linkTo(methodOn(ClashController.class).acceptInvitationByExternalId(clashResponse.getId(), null)).withRel("accept"));
                clashResponse.add(linkTo(methodOn(ClashController.class).declineInvitationByExternalId(clashResponse.getId(), null)).withRel("decline"));

                return clashResponse;
            })
            .collect(Collectors.toList());

        return PagedModel.of(
            clashResponses,
            new PagedModel.PageMetadata(
                clashes.getSize(),
                clashes.getNumber(),
                clashes.getTotalElements(),
                clashes.getTotalPages()
            )
        );
    }

    @Override
    public CollectionModel<ClashResponse> toCollectionModel(Iterable<? extends Clash> clashes) {

        Set<UUID> allPlayerIds = new HashSet<>();
        for (Clash clash : clashes) {
            if (clash.getOwnerId() != null) {
                allPlayerIds.add(clash.getOwnerId());
            }
            if (clash.getWinnerId() != null) {
                allPlayerIds.add(clash.getWinnerId());
            }
        }

        Map<UUID, Player> playerMap = playerService.findAllByExternalIds(allPlayerIds).stream()
                .collect(Collectors.toMap(Player::getExternalId, Function.identity()));

        List<ClashResponse> clashResponses = new ArrayList<>();
        for (Clash clash : clashes) {
            ClashResponse clashResponse = modelMapper.map(clash, ClashResponse.class);

            if (clash.getOwnerId() != null) {
                Player owner = playerMap.get(clash.getOwnerId());
                if (owner != null) {
                    PlayerResponse ownerResponse = modelMapper.map(owner, PlayerResponse.class);
                    ownerResponse.add(linkTo(methodOn(PlayerController.class)
                            .getByExternalId(ownerResponse.getId()))
                            .withSelfRel());
                    clashResponse.setOwner(ownerResponse);
                }
            }

            if (clash.getWinnerId() != null) {
                Player winner = playerMap.get(clash.getWinnerId());
                if (winner != null) {
                    PlayerResponse winnerResponse = modelMapper.map(winner, PlayerResponse.class);
                    winnerResponse.add(linkTo(methodOn(PlayerController.class)
                            .getByExternalId(winnerResponse.getId()))
                            .withSelfRel());
                    clashResponse.setWinner(winnerResponse);
                }
            }

            clashResponse.add(linkTo(methodOn(ClashController.class).getByExternalId(clashResponse.getId())).withSelfRel());
            clashResponse.add(linkTo(methodOn(ClashController.class).acceptInvitationByExternalId(clashResponse.getId(), null)).withRel("accept"));
            clashResponse.add(linkTo(methodOn(ClashController.class).declineInvitationByExternalId(clashResponse.getId(), null)).withRel("decline"));

            clashResponses.add(clashResponse);
        }

        return CollectionModel.of(clashResponses);
    }

}