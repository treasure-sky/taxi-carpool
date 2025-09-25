package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.chatting.MessageEntity;
import edu.kangwon.university.taxicarpool.map.MapPlace;
import edu.kangwon.university.taxicarpool.member.MemberEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "party")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToMany
    @JoinTable(
        name = "party_member",
        joinColumns = @JoinColumn(name = "party_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<MemberEntity> memberEntities = new ArrayList<>();

    @Column(name = "host_id")
    private Long hostMemberId;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Embedded
    private PartyOption options;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "comment")
    private String comment;

    @Column(name = "current_participant_count")
    private int currentParticipantCount;

    @Column(name = "max_participant_count")
    private int maxParticipantCount;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageEntity> messages = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "start_location")),
        @AttributeOverride(name = "roadAddressName", column = @Column(name = "start_road_address_name")),
        @AttributeOverride(name = "x", column = @Column(name = "start_longitude")),
        @AttributeOverride(name = "y", column = @Column(name = "start_latitude"))
    })
    private MapPlace startPlace;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "end_location")),
        @AttributeOverride(name = "roadAddressName", column = @Column(name = "end_road_address_name")),
        @AttributeOverride(name = "x", column = @Column(name = "end_longitude")),
        @AttributeOverride(name = "y", column = @Column(name = "end_latitude"))
    })
    private MapPlace endPlace;

    @Column(name = "notification")
    private String notification;

    @Column(name = "savings_calculated", nullable = false)
    private boolean savingsCalculated = false;

    public PartyEntity(
        Long hostMemberId,
        PartyOption options,
        LocalDateTime startDateTime,
        String comment,
        int currentParticipantCount,
        int maxParticipantCount,
        MapPlace startPlace,
        MapPlace endPlace
    ) {
        this.hostMemberId = hostMemberId;
        this.options = options;
        this.startDateTime = startDateTime;
        this.comment = comment;
        this.currentParticipantCount = currentParticipantCount;
        this.maxParticipantCount = maxParticipantCount;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setHostMemberId(Long hostMemberId) {
        this.hostMemberId = hostMemberId;
    }

    public void setCurrentParticipantCount(int currentParticipantCount) {
        this.currentParticipantCount = currentParticipantCount;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public void setSavingsCalculated(boolean savingsCalculated) {
        this.savingsCalculated = savingsCalculated;
    }

    public PartyEntity updateParty(
        PartyOption options,
        LocalDateTime startDateTime,
        String comment,
        int maxParticipantCount,
        MapPlace startPlace,
        MapPlace endPlace,
        String notification
    ) {
        this.options = options;
        this.startDateTime = startDateTime;
        this.comment = comment;
        this.maxParticipantCount = maxParticipantCount;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.notification = notification;
        return this;
    }
}
