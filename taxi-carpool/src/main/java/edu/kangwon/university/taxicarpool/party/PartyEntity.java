package edu.kangwon.university.taxicarpool.party;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "party")
public class PartyEntity {
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
