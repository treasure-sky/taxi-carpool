package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyCreateRequestDTO;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class PartyDTOTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("PartyCreateRequestDTO 필수 필드 누락 시 검증 에러 발생")
    void testPartyCreateRequestDTOValidation() {
        // startDateTime, startLocation, endLocation 등에 NotBlank가 걸려있으므로
        // 일부러 누락된 상태로 DTO를 생성해서 검증 오류를 유도
        PartyCreateRequestDTO dto = new PartyCreateRequestDTO(
            "", // name에 @NotBlank 적용되어 있으므로 빈 값으로 테스트
            false,
            null, // memberEntities
            null, // hostMemberId
            null, // endDate
            null, // creatorMemberId
            false,
            false,
            false,
            false,
            null, // startDateTime - NotBlank
            "",   // startLocation - NotBlank
            "",   // endLocation - NotBlank
            null,
            0,
            5     // maxParticipantCount - 4를 초과
        );

        Set<ConstraintViolation<PartyCreateRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        // 각각의 필드별로 어느 constraint가 깨졌는지 확인 가능
        violations.forEach(v ->
            System.out.println("필드: " + v.getPropertyPath() + ", 메시지: " + v.getMessage()));
    }
}
