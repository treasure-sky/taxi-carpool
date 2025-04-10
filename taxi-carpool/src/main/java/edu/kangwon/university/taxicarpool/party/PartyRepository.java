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

    Optional<PartyEntity> findByIdAndIsDeletedFalse(Long partyId);

    boolean existsByNameAndIsDeletedFalse(String name);

    Page<PartyEntity> findAllByIsDeletedFalse(Pageable pageable);

    // 모든 파라미터가 온 경우
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
        "WHERE p.is_deleted = false " +
        "ORDER BY total_distance ASC, ABS(TIMESTAMPDIFF(MINUTE, p.start_date_time, :userDepartureTime)) ASC",
        countQuery = "SELECT COUNT(*) FROM party p WHERE p.is_deleted = false",
        nativeQuery = true)
    Page<PartyEntity> findCustomPartyList(
        @Param("userDepartureLng") Double userDepartureLng,
        @Param("userDepartureLat") Double userDepartureLat,
        @Param("userDestinationLng") Double userDestinationLng,
        @Param("userDestinationLat") Double userDestinationLat,
        @Param("userDepartureTime") LocalDateTime userDepartureTime,
        Pageable pageable
    );

    // 출발지 또는 도착지에 대한 파라미터가 오지 않은 경우(오버로딩 한 것. 파라미터의 이름은 일단 도착지로 해둠)
    @Query(value = "SELECT p.*, " +
        " ST_Distance_Sphere(" +
        "    ST_GeomFromText(CONCAT('POINT(', p.end_longitude, ' ', p.end_latitude, ')')), " +
        "    ST_GeomFromText(CONCAT('POINT(', :userDestinationLng, ' ', :userDestinationLat, ')'))"
        +
        " ) AS total_distance " +
        "FROM party p " +
        "WHERE p.is_deleted = false " +
        "ORDER BY total_distance ASC, ABS(TIMESTAMPDIFF(MINUTE, p.start_date_time, :userDepartureTime)) ASC",
        countQuery = "SELECT COUNT(*) FROM party p WHERE p.is_deleted = false",
        nativeQuery = true)
    Page<PartyEntity> findCustomPartyList(
        @Param("userDestinationLng") Double userDestinationLng,
        @Param("userDestinationLat") Double userDestinationLat,
        @Param("userDepartureTime") LocalDateTime userDepartureTime,
        Pageable pageable
    );

    // 출발 시간 파라미터가 오지 않은 경우
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
        "WHERE p.is_deleted = false " +
        "ORDER BY total_distance ASC",
        countQuery = "SELECT COUNT(*) FROM party p WHERE p.is_deleted = false",
        nativeQuery = true)
    Page<PartyEntity> findCustomPartyList(
        @Param("userDepartureLng") Double userDepartureLng,
        @Param("userDepartureLat") Double userDepartureLat,
        @Param("userDestinationLng") Double userDestinationLng,
        @Param("userDestinationLat") Double userDestinationLat,
        Pageable pageable
    );

}
