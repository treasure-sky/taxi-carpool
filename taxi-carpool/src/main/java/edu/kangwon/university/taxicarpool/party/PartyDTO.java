package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.member.MemberEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

        @NotNull
        @NotBlank(message = "파티 이름은 필수입니다.")
        private String name;

        private boolean isDeleted;

        private List<MemberEntity> memberEntities = new ArrayList<>();

        private Long hostMemberId;

        private LocalDateTime endDate;

        private Long memberId;

        private boolean sameGenderOnly;

        private boolean costShareBeforeDropOff;

        private boolean quietMode;

        private boolean destinationChangeIn5Minutes;

        public PartyCreateRequestDTO(String name, boolean isDeleted,
            List<MemberEntity> memberEntities,
            Long hostMemberId, LocalDateTime endDate, Long memberId, boolean sameGenderOnly,
            boolean costShareBeforeDropOff, boolean quietMode,
            boolean destinationChangeIn5Minutes) {
            this.name = name;
            this.isDeleted = isDeleted;
            this.memberEntities = memberEntities;
            this.hostMemberId = hostMemberId;
            this.endDate = endDate;
            this.memberId = memberId;
            this.sameGenderOnly = sameGenderOnly;
            this.costShareBeforeDropOff = costShareBeforeDropOff;
            this.quietMode = quietMode;
            this.destinationChangeIn5Minutes = destinationChangeIn5Minutes;
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

        public Long getMemberId() {
            return memberId;
        }

        public void setMemberId(Long memberId) {
            this.memberId = memberId;
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
    }

    public static class PartyUpdateRequestDTO {

        private Long id;

        @NotNull
        @NotBlank(message = "파티 이름은 필수입니다.")
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
