package nextstep.subway.dto;

import nextstep.subway.domain.Section;

public class SectionResponse {

    private Long id;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public SectionResponse(Long id, Long upStationId, Long downStationId, int distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

}
