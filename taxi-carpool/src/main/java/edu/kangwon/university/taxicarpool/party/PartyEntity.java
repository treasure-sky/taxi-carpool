package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.chatting.MessageEntity;
import edu.kangwon.university.taxicarpool.map.MapPlace;
import edu.kangwon.university.taxicarpool.member.MemberEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "party")
@EntityListeners(AuditingEntityListener.class)
public class PartyEntity {

    public PartyEntity() {
    }

    public PartyEntity(
        String name,
        List<MemberEntity> memberEntities,
        Long hostMemberId,
        LocalDateTime endDate,
        boolean sameGenderOnly,
        boolean costShareBeforeDropOff,
        boolean quietMode,
        boolean destinationChange5Minutes,
        LocalDateTime startDateTime,
        String comment,
        int currentParticipantCount,
        int maxParticipantCount,
        MapPlace startPlace,
        MapPlace endPlace
    ) {
        this.name = name;
        this.isDeleted = false;
        this.memberEntities = memberEntities;
        this.hostMemberId = hostMemberId;
        this.endDate = endDate;
        this.sameGenderOnly = sameGenderOnly;
        this.costShareBeforeDropOff = costShareBeforeDropOff;
        this.quietMode = quietMode;
        this.destinationChangeIn5Minutes = destinationChange5Minutes;
        this.startDateTime = startDateTime;
        this.comment = comment;
        this.currentParticipantCount = currentParticipantCount;
        this.maxParticipantCount = maxParticipantCount;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    @NotNull
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

    @Column(name = "same_gender_only")
    private boolean sameGenderOnly;

    @Column(name = "cost_share_before_drop_off")
    private boolean costShareBeforeDropOff;

    @Column(name = "quiet_mode")
    private boolean quietMode;

    @Column(name = "destination_change_5minutes")
    private boolean destinationChangeIn5Minutes;

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

    public String getName() {
        return name;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public List<MemberEntity> getMemberEntities() {
        return memberEntities;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Long getHostMemberId() {
        return hostMemberId;
    }

    public void setHostMemberId(Long hostMemberId) {
        this.hostMemberId = hostMemberId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public boolean isExpired() {
        return this.endDate != null && this.endDate.isBefore(LocalDateTime.now());
    }

    public boolean isActive() {
        return !isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isSameGenderOnly() {
        return sameGenderOnly;
    }

    public void setSameGenderOnly(boolean sameGenderOnly) {
        this.sameGenderOnly = sameGenderOnly;
    }

    public boolean isCostShareBeforeDropOff() {
        return costShareBeforeDropOff;
    }

    public void setCostShareBeforeDropOff(boolean costShareBeforeDropOff) {
        this.costShareBeforeDropOff = costShareBeforeDropOff;
    }

    public boolean isQuietMode() {
        return quietMode;
    }

    public void setQuietMode(boolean quietMode) {
        this.quietMode = quietMode;
    }

    public boolean isDestinationChangeIn5Minutes() {
        return destinationChangeIn5Minutes;
    }

    public void setDestinationChangeIn5Minutes(boolean destinationChangeIn5Minutes) {
        this.destinationChangeIn5Minutes = destinationChangeIn5Minutes;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCurrentParticipantCount() {
        return currentParticipantCount;
    }

    public void setCurrentParticipantCount(int currentParticipantCount) {
        this.currentParticipantCount = currentParticipantCount;
    }

    public int getMaxParticipantCount() {
        return maxParticipantCount;
    }

    public void setMaxParticipantCount(int maxParticipantCount) {
        this.maxParticipantCount = maxParticipantCount;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public MapPlace getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(MapPlace startPlace) {
        this.startPlace = startPlace;
    }

    public MapPlace getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(MapPlace endPlace) {
        this.endPlace = endPlace;
    }

    public PartyEntity updateParty(
        String name,
        boolean isDeleted,
        List<MemberEntity> memberEntities,
        Long hostMemberId,
        LocalDateTime endDate,
        boolean sameGenderOnly,
        boolean costShareBeforeDropOff,
        boolean quietMode,
        boolean destinationChangeIn5Minutes,
        LocalDateTime startDateTime,
        String comment,
        int maxParticipantCount,
        MapPlace startPlace,
        MapPlace endPlace
    ) {
        this.name = name;
        this.isDeleted = isDeleted;
        this.memberEntities = memberEntities;
        this.hostMemberId = hostMemberId;
        this.endDate = endDate;
        this.sameGenderOnly = sameGenderOnly;
        this.costShareBeforeDropOff = costShareBeforeDropOff;
        this.quietMode = quietMode;
        this.destinationChangeIn5Minutes = destinationChangeIn5Minutes;
        this.startDateTime = startDateTime;
        this.comment = comment;
        this.maxParticipantCount = maxParticipantCount;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        return this;
    }

}
