package edu.kangwon.university.taxicarpool.member;

import edu.kangwon.university.taxicarpool.chatting.MessageEntity;
import edu.kangwon.university.taxicarpool.party.PartyEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MemberEntity {

    public MemberEntity(String email, String password, String nickname, Gender gender) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
    }

    public MemberEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Column(unique = true)
    private String nickname;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany(mappedBy = "memberEntities")
    private List<PartyEntity> parties = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<MessageEntity> sentMessages = new ArrayList<>();

    @Column(name = "total_saved_amount", nullable = false)
    private long totalSavedAmount = 0L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<MessageEntity> getSentMessages() {
        return sentMessages;
    }

    public List<PartyEntity> getParties() {
        return parties;
    }

    public void setParties(List<PartyEntity> parties) {
        this.parties = parties;
    }

    public long getTotalSavedAmount() {
        return totalSavedAmount;
    }

    public void setTotalSavedAmount(long totalSavedAmount) {
        this.totalSavedAmount = totalSavedAmount;
    }

    /** 절감 금액을 누적하는 편의 메서드 */
    public void addToTotalSavedAmount(long amountToAdd) {
        if (amountToAdd < 0) {
            throw new IllegalArgumentException("누적 절감 금액은 음수가 될 수 없습니다.");
        }
        this.totalSavedAmount += amountToAdd;
    }

}