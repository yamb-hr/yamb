package com.tejko.yamb.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.controllers.ClashController;
import com.tejko.yamb.api.controllers.PlayerController;
import com.tejko.yamb.api.dto.responses.ClashDetailResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.business.interfaces.PlayerService;
import com.tejko.yamb.domain.models.Clash;
import com.tejko.yamb.domain.models.Clash.ClashPlayer;
import com.tejko.yamb.domain.models.Player;

@Component
public class ClashDetailModelAssembler implements RepresentationModelAssembler<Clash, ClashDetailResponse> {

    private final ModelMapper modelMapper;
    private final PlayerService playerService;

    @Autowired
    public ClashDetailModelAssembler(ModelMapper modelMapper, PlayerService playerService) {
        this.modelMapper = modelMapper;
        this.playerService = playerService;
    }

    @Override
    public ClashDetailResponse toModel(Clash clash) {
        Set<UUID> allPlayerIds = new HashSet<>();
        if (clash.getOwnerId() != null) {
            allPlayerIds.add(clash.getOwnerId());
        }
        if (clash.getWinnerId() != null) {
            allPlayerIds.add(clash.getWinnerId());
        }
        if (clash.getPlayers() != null) {
            for (ClashPlayer clashplayer : clash.getPlayers()) {
                if (clashplayer.getId() != null) {
                    allPlayerIds.add(clashplayer.getId());
                }
            }
        }

        Map<UUID, Player> playerMap = playerService.findAllByExternalIds(allPlayerIds).stream()
            .collect(Collectors.toMap(Player::getExternalId, Function.identity()));

        ClashDetailResponse clashDetailResponse = modelMapper.map(clash, ClashDetailResponse.class);

        if (clash.getOwnerId() != null) {
            Player owner = playerMap.get(clash.getOwnerId());
            if (owner != null) {
                PlayerResponse ownerResponse = modelMapper.map(owner, PlayerResponse.class);
                ownerResponse.add(linkTo(methodOn(PlayerController.class)
                        .getByExternalId(ownerResponse.getId()))
                        .withSelfRel());
                clashDetailResponse.setOwner(ownerResponse);
            }
        }

        if (clash.getWinnerId() != null) {
            Player winner = playerMap.get(clash.getWinnerId());
            if (winner != null) {
                PlayerResponse winnerResponse = modelMapper.map(winner, PlayerResponse.class);
                winnerResponse.add(linkTo(methodOn(PlayerController.class)
                        .getByExternalId(winnerResponse.getId()))
                        .withSelfRel());
                clashDetailResponse.setWinner(winnerResponse);
            }
        }

        if (clash.getPlayers() != null) {
            List<ClashDetailResponse.ClashPlayer> clashPlayerResponses = clash.getPlayers().stream()
                .map(clashPlayer -> {
                    ClashDetailResponse.ClashPlayer clashPlayerResponse = modelMapper.map(clashPlayer, ClashDetailResponse.ClashPlayer.class);
                    Player p = playerMap.get(clashPlayer.getId());
                    if (p != null) {
                        clashPlayerResponse.setName(p.getUsername());
                        clashPlayerResponse.add(linkTo(methodOn(PlayerController.class)
                            .getByExternalId(clashPlayerResponse.getId()))
                            .withSelfRel());
                    }
                    return clashPlayerResponse;
                })
                .collect(Collectors.toList());

            clashDetailResponse.setPlayers(clashPlayerResponses);
        }

        clashDetailResponse.add(linkTo(methodOn(ClashController.class).getByExternalId(clashDetailResponse.getId())).withSelfRel());
        clashDetailResponse.add(linkTo(methodOn(ClashController.class).acceptInvitationByExternalId(clashDetailResponse.getId(), null)).withRel("accept"));
        clashDetailResponse.add(linkTo(methodOn(ClashController.class).declineInvitationByExternalId(clashDetailResponse.getId(), null)).withRel("decline"));
    
        return clashDetailResponse;
    }

}