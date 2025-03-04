package edu.kangwon.university.taxicarpool.member;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "member")
public class MemberEntity {
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
