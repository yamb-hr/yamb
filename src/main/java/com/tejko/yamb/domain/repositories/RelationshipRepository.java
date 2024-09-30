package com.tejko.yamb.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tejko.yamb.domain.models.PlayerRelationship;
import com.tejko.yamb.domain.models.PlayerRelationshipId;

public interface RelationshipRepository extends JpaRepository<PlayerRelationship, PlayerRelationshipId> {

    @Query("SELECT pr FROM PlayerRelationship pr " +
       "WHERE (pr.type = 'FRIEND' AND (pr.id.firstPlayer.id = :playerId OR pr.id.secondPlayer.id = :playerId)) " +
       "OR (pr.type = 'BLOCK' AND pr.id.firstPlayer.id = :playerId)")
    List<PlayerRelationship> getRelationshipsByPlayerId(Long playerId);

    @Query("SELECT pr FROM PlayerRelationship pr " +
       "WHERE (pr.id.firstPlayer.id = :firstPlayerId AND pr.id.secondPlayer.id = :secondPlayerId)" +
       "OR (pr.id.firstPlayer.id = :secondPlayerId AND pr.id.secondPlayer.id = :firstPlayerId)")
    Optional<PlayerRelationship> findByPlayerIds(Long firstPlayerId, Long secondPlayerId);

}
