package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.member.MemberEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "party")
public class PartyEntity {

    public PartyEntity() {
    }

    public PartyEntity(
        String name,
        List<MemberEntity> memberEntities,
        LocalDateTime startDate,
        LocalDateTime endDate) {
        this.name = name;
        this.isDeleted = false;
        MemberEntities = memberEntities;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "party_Id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "partyEntity", cascade = CascadeType.ALL)
    private List<MemberEntity> MemberEntities = new ArrayList<>();

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;


    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<MemberEntity> getMemberEntities() {
        return MemberEntities;
    }

    public void setMemberEntities(List<MemberEntity> memberEntities) {
        MemberEntities = memberEntities;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }


    // service계층에 만들면, setter, getter 사용해서돼서 일단 entity에 만듦.
    public PartyEntity updateParty(
        String name,
        boolean isDeleted,
        LocalDateTime startDate,
        LocalDateTime endDate) {
        this.name = name;
        this.isDeleted = isDeleted;
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    public boolean isExpired() {
        return this.endDate.isBefore(LocalDateTime.now());
    }

    public boolean isActive() {
        return Boolean.FALSE.equals(isDeleted);
    }


}
