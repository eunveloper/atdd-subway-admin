package nextstep.subway.dto;

import nextstep.subway.domain.Line;

public class LineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    private LineRequest() {}

    public LineRequest(String name,
                       String color,
                       Long upStationId,
                       Long downStationId,
                       int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Line toLine() {
        return new Line(name, color, distance);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getDistance() {
        return distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

}
