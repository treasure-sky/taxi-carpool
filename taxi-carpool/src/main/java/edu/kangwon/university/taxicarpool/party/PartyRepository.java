package edu.kangwon.university.taxicarpool.party;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, Long> {

    Optional<PartyEntity> findById(Long partyId);
}
