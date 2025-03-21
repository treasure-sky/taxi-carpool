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
        Long id,
        String name,
        List<MemberEntity> memberEntities,
        Long hostMemberId,
        LocalDateTime endDate) {
        this.id = id;
        this.name = name;
        this.isDeleted = false;
        this.memberEntities = memberEntities;
        this.hostMemberId = hostMemberId;
        this.endDate = endDate;
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

    @OneToMany(mappedBy = "partyEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberEntity> memberEntities = new ArrayList<>();

    @Column(name = "host_id")
    private Long hostMemberId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Column(name = "end_date")
    private LocalDateTime endDate;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public PartyEntity updateParty(
        String name,
        boolean isDeleted,
        List<MemberEntity> memberEntities,
        Long hostMemberId,
        LocalDateTime endDate) {
        this.name = name;
        this.isDeleted = isDeleted;
        this.memberEntities = memberEntities;
        this.hostMemberId = hostMemberId;
        this.endDate = endDate;
        return this;
    }

    public boolean isExpired() {
        return this.endDate != null && this.endDate.isBefore(LocalDateTime.now());
    }

    public boolean isActive() {
        return !isDeleted;
    }
}
