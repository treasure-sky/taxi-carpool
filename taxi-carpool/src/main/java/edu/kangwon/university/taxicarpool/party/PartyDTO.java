package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.member.MemberEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

        private boolean sameGenderOnly;

        private boolean costShareBeforeDropOff;

        private boolean quietMode;

        private boolean destinationChangeIn5Minutes;

        private LocalDateTime startDateTime;

        private String startLocation;

        private String endLocation;

        private String comment;

        private int currentParticipantCount;

        private int maxParticipantCount;

        public PartyResponseDTO(Long id,
            String name,
            boolean isDeleted,
            List<MemberEntity> memberEntities,
            Long hostMemberId,
            LocalDateTime endDate,
            boolean sameGenderOnly,
            boolean costShareBeforeDropOff,
            boolean quietMode,
            boolean destinationChangeIn5Minutes,
            LocalDateTime startDateTime,
            String startLocation,
            String endLocation,
            String comment,
            int currentParticipantCount,
            int maxParticipantCount) {
            this.id = id;
            this.name = name;
            this.isDeleted = isDeleted;
            this.memberEntities = memberEntities;
            this.hostMemberId = hostMemberId;
            this.endDate = endDate;
            this.sameGenderOnly = sameGenderOnly;
            this.costShareBeforeDropOff = costShareBeforeDropOff;
            this.quietMode = quietMode;
            this.destinationChangeIn5Minutes = destinationChangeIn5Minutes;
            this.startDateTime = startDateTime;
            this.startLocation = startLocation;
            this.endLocation = endLocation;
            this.comment = comment;
            this.currentParticipantCount = currentParticipantCount;
            this.maxParticipantCount = maxParticipantCount;
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

        public LocalDateTime getStartDateTime() {
            return startDateTime;
        }

        public void setStartDateTime(LocalDateTime startDateTime) {
            this.startDateTime = startDateTime;
        }

        public String getStartLocation() {
            return startLocation;
        }

        public void setStartLocation(String startLocation) {
            this.startLocation = startLocation;
        }

        public String getEndLocation() {
            return endLocation;
        }

        public void setEndLocation(String endLocation) {
            this.endLocation = endLocation;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getCurrentParticipantCount() {
            return currentParticipantCount;
        }

        public void setCurrentParticipantCount(int currentParticipantCount) {
            this.currentParticipantCount = currentParticipantCount;
        }

        public int getMaxParticipantCount() {
            return maxParticipantCount;
        }

        public void setMaxParticipantCount(int maxParticipantCount) {
            this.maxParticipantCount = maxParticipantCount;
        }
    }

    // creatorMemberId 필드 존재
    public static class PartyCreateRequestDTO {

        @NotNull
        @NotBlank(message = "파티 이름은 필수입니다.")
        @Size(max = 20, message = "이름은 공백 포함 최대 20글자까지 가능합니다.")
        private String name;

        private boolean isDeleted;

        private List<MemberEntity> memberEntities = new ArrayList<>();

        private Long hostMemberId;

        private LocalDateTime endDate;

        private Long creatorMemberId;

        // 아래 4개 옵션들 NotNull해야하나..?
        private boolean sameGenderOnly;

        private boolean costShareBeforeDropOff;

        private boolean quietMode;

        private boolean destinationChangeIn5Minutes;

        @NotNull
        @NotBlank(message = "출발 시간 입력은 필수입니다.")
        private LocalDateTime startDateTime;

        @NotNull
        @NotBlank(message = "출발지 입력은 필수입니다.")
        private String startLocation;

        @NotNull
        @NotBlank(message = "목적지 입력은 필수입니다.")
        private String endLocation;

        @Size(max = 30, message = "설명은 최대 30글자입니다.")
        private String comment;

        private int currentParticipantCount;

        @Max(value = 4, message = "택시의 최대 탑승 인원 수는 4명입니다.")
        private int maxParticipantCount;

        public PartyCreateRequestDTO(String name,
            boolean isDeleted,
            List<MemberEntity> memberEntities,
            Long hostMemberId,
            LocalDateTime endDate,
            Long creatorMemberId,
            boolean sameGenderOnly,
            boolean costShareBeforeDropOff,
            boolean quietMode,
            boolean destinationChangeIn5Minutes,
            LocalDateTime startDateTime,
            String startLocation,
            String endLocation,
            String comment,
            int currentParticipantCount,
            int maxParticipantCount) {
            this.name = name;
            this.isDeleted = isDeleted;
            this.memberEntities = memberEntities;
            this.hostMemberId = hostMemberId;
            this.endDate = endDate;
            this.creatorMemberId = creatorMemberId;
            this.sameGenderOnly = sameGenderOnly;
            this.costShareBeforeDropOff = costShareBeforeDropOff;
            this.quietMode = quietMode;
            this.destinationChangeIn5Minutes = destinationChangeIn5Minutes;
            this.startDateTime = startDateTime;
            this.startLocation = startLocation;
            this.endLocation = endLocation;
            this.comment = comment;
            this.currentParticipantCount = currentParticipantCount;
            this.maxParticipantCount = maxParticipantCount;
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

        public Long getCreatorMemberId() {
            return creatorMemberId;
        }

        public void setCreatorMemberId(Long creatorMemberId) {
            this.creatorMemberId = creatorMemberId;
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

        public LocalDateTime getStartDateTime() {
            return startDateTime;
        }

        public void setStartDateTime(LocalDateTime startDateTime) {
            this.startDateTime = startDateTime;
        }

        public String getStartLocation() {
            return startLocation;
        }

        public void setStartLocation(String startLocation) {
            this.startLocation = startLocation;
        }

        public String getEndLocation() {
            return endLocation;
        }

        public void setEndLocation(String endLocation) {
            this.endLocation = endLocation;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getCurrentParticipantCount() {
            return currentParticipantCount;
        }

        public void setCurrentParticipantCount(int currentParticipantCount) {
            this.currentParticipantCount = currentParticipantCount;
        }

        public int getMaxParticipantCount() {
            return maxParticipantCount;
        }

        public void setMaxParticipantCount(int maxParticipantCount) {
            this.maxParticipantCount = maxParticipantCount;
        }
    }

    // UpdateRequestDTO에는 현재 인원수에 대한 필드가 없음 -> 파티의 인원수에 관한 로직은 무조건 join/leave 엔트포인트 사용을 강제를 위해
    public static class PartyUpdateRequestDTO {

        @NotNull
        @NotBlank(message = "파티 이름은 필수입니다.")
        private String name;

        private boolean isDeleted;

        private List<MemberEntity> memberEntities = new ArrayList<>();

        private Long hostMemberId;

        private LocalDateTime endDate;

        private boolean sameGenderOnly;

        private boolean costShareBeforeDropOff;

        private boolean quietMode;

        private boolean destinationChangeIn5Minutes;

        @NotNull
        @NotBlank(message = "출발 시간 입력은 필수입니다.")
        private LocalDateTime startDateTime;

        @NotNull
        @NotBlank(message = "출발지 입력은 필수입니다.")
        private String startLocation;

        @NotNull
        @NotBlank(message = "목적지 입력은 필수입니다.")
        private String endLocation;

        @Size(max = 30, message = "설명은 최대 30글자입니다.")
        private String comment;

        @Max(value = 4, message = "택시의 최대 탑승 인원 수는 4명입니다.")
        private int maxParticipantCount;

        public PartyUpdateRequestDTO(
            String name,
            boolean isDeleted,
            List<MemberEntity> memberEntities,
            Long hostMemberId,
            LocalDateTime endDate,
            boolean sameGenderOnly,
            boolean costShareBeforeDropOff,
            boolean quietMode,
            boolean destinationChangeIn5Minutes,
            LocalDateTime startDateTime,
            String startLocation,
            String endLocation,
            String comment,
            int maxParticipantCount) {
            this.name = name;
            this.isDeleted = isDeleted;
            this.memberEntities = memberEntities;
            this.hostMemberId = hostMemberId;
            this.endDate = endDate;
            this.sameGenderOnly = sameGenderOnly;
            this.costShareBeforeDropOff = costShareBeforeDropOff;
            this.quietMode = quietMode;
            this.destinationChangeIn5Minutes = destinationChangeIn5Minutes;
            this.startDateTime = startDateTime;
            this.startLocation = startLocation;
            this.endLocation = endLocation;
            this.comment = comment;
            this.maxParticipantCount = maxParticipantCount;
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

        public LocalDateTime getStartDateTime() {
            return startDateTime;
        }

        public void setStartDateTime(LocalDateTime startDateTime) {
            this.startDateTime = startDateTime;
        }

        public String getStartLocation() {
            return startLocation;
        }

        public void setStartLocation(String startLocation) {
            this.startLocation = startLocation;
        }

        public String getEndLocation() {
            return endLocation;
        }

        public void setEndLocation(String endLocation) {
            this.endLocation = endLocation;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getMaxParticipantCount() {
            return maxParticipantCount;
        }

        public void setMaxParticipantCount(int maxParticipantCount) {
            this.maxParticipantCount = maxParticipantCount;
        }
    }
}
