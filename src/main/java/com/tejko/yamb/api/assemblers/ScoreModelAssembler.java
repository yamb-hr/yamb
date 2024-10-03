package com.tejko.yamb.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.controllers.PlayerController;
import com.tejko.yamb.api.controllers.ScoreController;
import com.tejko.yamb.api.dto.responses.GlobalScoreStatsResponse;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.domain.models.GlobalScoreStats;
import com.tejko.yamb.domain.models.Score;

@Component
public class ScoreModelAssembler implements RepresentationModelAssembler<Score, ScoreResponse> {

    private final ModelMapper modelMapper;

    public ScoreModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ScoreResponse toModel(Score score) {
        
        ScoreResponse scoreResponse = modelMapper.map(score, ScoreResponse.class);
		scoreResponse.add(linkTo(methodOn(ScoreController.class).getByExternalId(scoreResponse.getId())).withSelfRel());
		scoreResponse.getPlayer().add(linkTo(methodOn(PlayerController.class).getByExternalId(scoreResponse.getPlayer().getId())).withSelfRel());

        return scoreResponse;
    }

    public PagedModel<ScoreResponse> toPagedModel(Page<Score> scores) {

        List<ScoreResponse> scoreResponses = scores.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        PagedModel<ScoreResponse> pagedScores = PagedModel.of(scoreResponses, new PagedModel.PageMetadata(
            scores.getSize(), scores.getNumber(), scores.getTotalElements(), scores.getTotalPages()
        ));

        return pagedScores;
    }

    public GlobalScoreStatsResponse toModel(GlobalScoreStats globalScoreStats) {
        GlobalScoreStatsResponse globalScoreStatsResponse = modelMapper.map(globalScoreStats, GlobalScoreStatsResponse.class);
        return globalScoreStatsResponse;
    }

}
