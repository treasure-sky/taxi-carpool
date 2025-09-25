package edu.kangwon.university.taxicarpool.party.PartyUtil;

public class CoordinateValidator {

    private CoordinateValidator() {}

    /**
     * 좌표 유효성을 검증합니다(대략적인 한반도 범위).
     *
     * @param x 경도
     * @param y 위도
     * @param label 검증 대상 라벨(출발지/도착지 등)
     * @throws IllegalArgumentException 좌표가 허용 범위를 벗어난 경우
     */
    public static void validate(double x, double y, String label) {
        if (x < 124 || x > 132) {
            throw new IllegalArgumentException(label + " 경도(x)가 한반도 범위를 벗어났습니다: x=" + x);
        }
        if (y < 33 || y > 39) {
            throw new IllegalArgumentException(label + " 위도(y)가 한반도 범위를 벗어났습니다: y=" + y);
        }
    }
}