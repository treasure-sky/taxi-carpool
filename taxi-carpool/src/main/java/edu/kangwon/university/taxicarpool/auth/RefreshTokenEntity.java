package edu.kangwon.university.taxicarpool.auth;

import edu.kangwon.university.taxicarpool.member.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어느 회원의 리프래쉬 토큰인지 저장해야함.
    @OneToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    // 실제 리프래쉬 토큰 문자열 필드임
    private String refreshToken;

    // 만료 기한
    private LocalDateTime expiryDate;

    public RefreshTokenEntity() {
    }

    public RefreshTokenEntity(MemberEntity member, String refreshToken, LocalDateTime expiryDate) {
        this.member = member;
        this.refreshToken = refreshToken;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public MemberEntity getMember() {
        return member;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void updateRefreshToken(String refreshToken, LocalDateTime expiryDate) {
        this.refreshToken = refreshToken;
        this.expiryDate = expiryDate;
    }
}
