package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.map.MapPlaceDTO;
import edu.kangwon.university.taxicarpool.member.MemberEntity;
import jakarta.validation.constraints.Future;
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

        private List<Long> memberIds;

        private Long hostMemberId;

        private LocalDateTime endDate;

        private boolean sameGenderOnly;

        private boolean costShareBeforeDropOff;

        private boolean quietMode;

        private boolean destinationChangeIn5Minutes;

        private LocalDateTime startDateTime;

        private String comment;

        private int currentParticipantCount;

        private int maxParticipantCount;

        private MapPlaceDTO startPlace;

        private MapPlaceDTO endPlace;

        public PartyResponseDTO(
            Long id,
            String name,
            boolean isDeleted,
            List<Long> memberIds,
            Long hostMemberId,
            LocalDateTime endDate,
            boolean sameGenderOnly,
            boolean costShareBeforeDropOff,
            boolean quietMode,
            boolean destinationChangeIn5Minutes,
            LocalDateTime startDateTime,
            String comment,
            int currentParticipantCount,
            int maxParticipantCount,
            MapPlaceDTO startPlace,
            MapPlaceDTO endPlace) {
            this.id = id;
            this.name = name;
            this.isDeleted = isDeleted;
            this.memberIds = memberIds;
            this.hostMemberId = hostMemberId;
            this.endDate = endDate;
            this.sameGenderOnly = sameGenderOnly;
            this.costShareBeforeDropOff = costShareBeforeDropOff;
            this.quietMode = quietMode;
            this.destinationChangeIn5Minutes = destinationChangeIn5Minutes;
            this.startDateTime = startDateTime;
            this.comment = comment;
            this.currentParticipantCount = currentParticipantCount;
            this.maxParticipantCount = maxParticipantCount;
            this.startPlace = startPlace;
            this.endPlace = endPlace;
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

        public List<Long> getMemberIds() {
            return memberIds;
        }

        public void setMemberIds(List<Long> memberIds) {
            this.memberIds = memberIds;
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

        public MapPlaceDTO getStartPlace() {
            return startPlace;
        }

        public void setStartPlace(MapPlaceDTO startPlace) {
            this.startPlace = startPlace;
        }

        public MapPlaceDTO getEndPlace() {
            return endPlace;
        }

        public void setEndPlace(MapPlaceDTO endPlace) {
            this.endPlace = endPlace;
        }
    }

    // creatorMemberId 필드 존재, hostMemberId필드 삭제 -> creatorMemberId사용의 강제를 위해.
    public static class PartyCreateRequestDTO {

        // 아래 4개 옵션들 NotNull해야하나..?
        private boolean sameGenderOnly;

        private boolean costShareBeforeDropOff;

        private boolean quietMode;

        private boolean destinationChangeIn5Minutes;

        @Future(message = "출발 시간은 현재 시간보다 이후여야 합니다.")
        @NotNull(message = "출발 시간 입력은 필수입니다.")
        private LocalDateTime startDateTime;

        @Size(max = 30, message = "설명은 최대 30글자입니다.")
        private String comment;

        private int currentParticipantCount;

        @Max(value = 4, message = "택시의 최대 탑승 인원 수는 4명입니다.")
        private int maxParticipantCount;

        private MapPlaceDTO startPlace;

        private MapPlaceDTO endPlace;

        public PartyCreateRequestDTO(
            boolean sameGenderOnly,
            boolean costShareBeforeDropOff,
            boolean quietMode,
            boolean destinationChangeIn5Minutes,
            LocalDateTime startDateTime,
            String comment,
            int currentParticipantCount,
            int maxParticipantCount,
            MapPlaceDTO startPlace,
            MapPlaceDTO endPlace) {
            this.sameGenderOnly = sameGenderOnly;
            this.costShareBeforeDropOff = costShareBeforeDropOff;
            this.quietMode = quietMode;
            this.destinationChangeIn5Minutes = destinationChangeIn5Minutes;
            this.startDateTime = startDateTime;
            this.comment = comment;
            this.currentParticipantCount = currentParticipantCount;
            this.maxParticipantCount = maxParticipantCount;
            this.startPlace = startPlace;
            this.endPlace = endPlace;
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

        public MapPlaceDTO getStartPlace() {
            return startPlace;
        }

        public void setStartPlace(MapPlaceDTO startPlace) {
            this.startPlace = startPlace;
        }

        public MapPlaceDTO getEndPlace() {
            return endPlace;
        }

        public void setEndPlace(MapPlaceDTO endPlace) {
            this.endPlace = endPlace;
        }
    }

    // UpdateRequestDTO에는 현재 인원수에 대한 필드가 없음 -> 파티의 인원수에 관한 로직은 무조건 join/leave 엔트포인트 사용을 강제를 위해
    public static class PartyUpdateRequestDTO {

        private boolean sameGenderOnly;

        private boolean costShareBeforeDropOff;

        private boolean quietMode;

        private boolean destinationChangeIn5Minutes;

        @NotNull(message = "출발 시간 입력은 필수입니다.")
        private LocalDateTime startDateTime;

        @Size(max = 30, message = "설명은 최대 30글자입니다.")
        private String comment;

        @Max(value = 4, message = "택시의 최대 탑승 인원 수는 4명입니다.")
        private int maxParticipantCount;

        private MapPlaceDTO startPlace;

        private MapPlaceDTO endPlace;

        public PartyUpdateRequestDTO(
            boolean sameGenderOnly,
            boolean costShareBeforeDropOff,
            boolean quietMode,
            boolean destinationChangeIn5Minutes,
            LocalDateTime startDateTime,
            String comment,
            int maxParticipantCount,
            MapPlaceDTO startPlace,
            MapPlaceDTO endPlace
        ) {
            this.sameGenderOnly = sameGenderOnly;
            this.costShareBeforeDropOff = costShareBeforeDropOff;
            this.quietMode = quietMode;
            this.destinationChangeIn5Minutes = destinationChangeIn5Minutes;
            this.startDateTime = startDateTime;
            this.comment = comment;
            this.maxParticipantCount = maxParticipantCount;
            this.startPlace = startPlace;
            this.endPlace = endPlace;
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

        public MapPlaceDTO getStartPlace() {
            return startPlace;
        }

        public void setStartPlace(MapPlaceDTO startPlace) {
            this.startPlace = startPlace;
        }

        public MapPlaceDTO getEndPlace() {
            return endPlace;
        }

        public void setEndPlace(MapPlaceDTO endPlace) {
            this.endPlace = endPlace;
        }
    }
}
