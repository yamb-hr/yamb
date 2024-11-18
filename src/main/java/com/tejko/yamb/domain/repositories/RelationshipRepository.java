package com.tejko.yamb.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tejko.yamb.domain.models.PlayerRelationship;
import com.tejko.yamb.domain.models.PlayerRelationshipId;

public interface RelationshipRepository extends JpaRepository<PlayerRelationship, PlayerRelationshipId> {

   Optional<PlayerRelationship> findByExternalId(UUID externalId);

   @Query("SELECT pr FROM PlayerRelationship pr WHERE pr.id.player.id = :playerId OR pr.id.relatedPlayer.id = :playerId ORDER BY updatedAt")
   List<PlayerRelationship> getRelationshipsByPlayerId(Long playerId);

   @Query("SELECT pr FROM PlayerRelationship pr " +
      "WHERE (pr.id.player.id = :playerId AND pr.id.relatedPlayer.id = :relatedPlayerId)" +
      "OR (pr.id.player.id = :relatedPlayerId AND pr.id.relatedPlayer.id = :playerId) ORDER BY updatedAt")
   Optional<PlayerRelationship> findByPlayerIds(Long playerId, Long relatedPlayerId);

   void deleteByExternalId(UUID externalId);

}
