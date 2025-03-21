package edu.kangwon.university.taxicarpool.member;

import edu.kangwon.university.taxicarpool.party.PartyEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "member")
public class MemberEntity {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private PartyEntity partyEntity;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
