package edu.kangwon.university.taxicarpool.party;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, Long> {

    Optional<PartyEntity> findById(Long partyId);

    @Query(value = "SELECT p.*, " +
        " (ST_Distance_Sphere(" +
        "    ST_GeomFromText(CONCAT('POINT(', p.start_longitude, ' ', p.start_latitude, ')')), " +
        "    ST_GeomFromText(CONCAT('POINT(', :userDepartureLng, ' ', :userDepartureLat, ')'))" +
        " ) + " +
        " ST_Distance_Sphere(" +
        "    ST_GeomFromText(CONCAT('POINT(', p.end_longitude, ' ', p.end_latitude, ')')), " +
        "    ST_GeomFromText(CONCAT('POINT(', :userDestinationLng, ' ', :userDestinationLat, ')'))"
        +
        " )" +
        " ) AS total_distance " +
        "FROM party p " +
        "ORDER BY total_distance ASC, ABS(TIMESTAMPDIFF(MINUTE, p.start_date_time, :userDepartureTime)) ASC",
        countQuery = "SELECT COUNT(*) FROM party p",
        nativeQuery = true)
    Page<PartyEntity> findCustomPartyList(
        @Param("userDepartureLng") double userDepartureLng,
        @Param("userDepartureLat") double userDepartureLat,
        @Param("userDestinationLng") double userDestinationLng,
        @Param("userDestinationLat") double userDestinationLat,
        @Param("userDepartureTime") LocalDateTime userDepartureTime,
        Pageable pageable
    );

}
