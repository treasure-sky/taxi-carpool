package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.member.MemberEntity;
import jakarta.persistence.*;
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
        String startLocation,
        String endLocation,
        String comment,
        int currentParticipantCount,
        int maxParticipantCount
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
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.comment = comment;
        this.currentParticipantCount = currentParticipantCount;
        this.maxParticipantCount = maxParticipantCount;
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

    @Column(name = "start_location")
    private String startLocation;

    @Column(name = "end_location")
    private String endLocation;

    @Column(name = "comment")
    private String comment;

    @Column(name = "current_participant_count")
    private int currentParticipantCount;

    @Column(name = "max_participant_count")
    private int maxParticipantCount;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

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

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
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
        String startLocation,
        String endLocation,
        String comment,
        int currentParticipantCount,
        int maxParticipantCount
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
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.comment = comment;
        this.currentParticipantCount = currentParticipantCount;
        this.maxParticipantCount = maxParticipantCount;
        return this;

    }

}
