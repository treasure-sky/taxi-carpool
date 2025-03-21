package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.member.MemberEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartyDTO {

    public static class PartyResponseDTO {

        private Long id;
        private String name;
        private boolean isDeleted;
        private List<MemberEntity> memberEntities = new ArrayList<>();
        private Long hostMemberId;
        private LocalDateTime endDate;

        public PartyResponseDTO(Long id,
            String name,
            boolean isDeleted,
            List<MemberEntity> memberEntities,
            Long hostMemberId,
            LocalDateTime endDate) {
            this.id = id;
            this.name = name;
            this.isDeleted = isDeleted;
            this.memberEntities = memberEntities;
            this.hostMemberId = hostMemberId;
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
            return memberEntities;
        }

        public void setMemberEntities(List<MemberEntity> memberEntities) {
            this.memberEntities = memberEntities;
        }

        public Long getHostMemberId() {
            return hostMemberId;
        }

        public void setHostMemberId(Long hostMemberId) {
            this.hostMemberId = hostMemberId;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDateTime endDate) {
            this.endDate = endDate;
        }
    }

    public static class PartyCreateRequestDTO {
        private Long id;
        private String name;
        private boolean isDeleted;
        private List<MemberEntity> memberEntities = new ArrayList<>();
        private Long hostMemberId;
        private LocalDateTime endDate;

        public PartyCreateRequestDTO(Long id, String name, boolean isDeleted,
            List<MemberEntity> memberEntities,
            Long hostMemberId,
            LocalDateTime endDate) {
            this.id = id;
            this.name = name;
            this.isDeleted = isDeleted;
            this.memberEntities = memberEntities;
            this.hostMemberId = hostMemberId;
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
            return memberEntities;
        }

        public void setMemberEntities(List<MemberEntity> memberEntities) {
            this.memberEntities = memberEntities;
        }

        public Long getHostMemberId() {
            return hostMemberId;
        }

        public void setHostMemberId(Long hostMemberId) {
            this.hostMemberId = hostMemberId;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDateTime endDate) {
            this.endDate = endDate;
        }
    }

    public static class PartyUpdateRequestDTO {
        private Long id;
        private String name;
        private boolean isDeleted;
        private List<MemberEntity> memberEntities = new ArrayList<>();
        private Long hostMemberId;
        private LocalDateTime endDate;

        public PartyUpdateRequestDTO(Long id, String name, boolean isDeleted,
            List<MemberEntity> memberEntities,
            Long hostMemberId,
            LocalDateTime endDate) {
            this.id = id;
            this.name = name;
            this.isDeleted = isDeleted;
            this.memberEntities = memberEntities;
            this.hostMemberId = hostMemberId;
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
            return memberEntities;
        }

        public void setMemberEntities(List<MemberEntity> memberEntities) {
            this.memberEntities = memberEntities;
        }

        public Long getHostMemberId() {
            return hostMemberId;
        }

        public void setHostMemberId(Long hostMemberId) {
            this.hostMemberId = hostMemberId;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDateTime endDate) {
            this.endDate = endDate;
        }
    }
}
