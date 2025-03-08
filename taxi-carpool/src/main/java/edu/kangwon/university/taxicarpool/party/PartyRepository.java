package edu.kangwon.university.taxicarpool.party;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, Long> {
    Optional<PartyEntity> findById(Long partyId);
}
