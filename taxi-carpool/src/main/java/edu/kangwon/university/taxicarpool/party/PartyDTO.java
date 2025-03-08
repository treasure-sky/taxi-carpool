package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.member.MemberEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartyDTO {
    private Long id;
    private String name;
    private boolean isDeleted;
    private List<MemberEntity> MemberEntities = new ArrayList<>();
    private LocalDateTime startDate;
    private LocalDateTime endDate;


    public PartyDTO(Long id,
                    String name,
                    boolean isDeleted,
                    List<MemberEntity> memberEntities,
                    LocalDateTime startDate,
                    LocalDateTime endDate) {
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
        MemberEntities = memberEntities;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

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
}
