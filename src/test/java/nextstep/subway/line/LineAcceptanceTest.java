package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import nextstep.subway.dto.LineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철호선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    private final static LineRequest line_1 = new LineRequest("1호선", "빨간색", 10);
    private final static LineRequest line_2 = new LineRequest("2호선", "노색", 15);

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
        }
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        String location = insertLineSuccess(line_1).header("Location");

        // then
        String lineName = selectLine(null, location).extract().jsonPath().get("name");
        assertThat(lineName).isEqualTo(line_1.getName());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        insertLineSuccess(line_1);
        insertLineSuccess(line_2);

        // when
        List<String> lineNames =
                selectLines().extract().jsonPath().getList("name", String.class);

        // then
        assertAll (
                () -> assertThat(lineNames.size()).isEqualTo(2),
                () -> assertThat(lineNames).contains(line_1.getName(), line_2.getName())
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        String location = insertLineSuccess(line_1).header("Location");

        // when
        String lineName = selectLine(null, location).extract().jsonPath().get("name");

        // then
        assertThat(lineName).isNotNull();
        assertThat(lineName).isEqualTo(line_1.getName());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        String location = insertLineSuccess(line_1).header("Location");

        // when
        updateLineSuccess(null, location, line_2);

        // then
        String lineName = selectLine(null, location).extract().jsonPath().get("name");
        assertThat(lineName).isEqualTo(line_2.getName());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        String location = insertLineSuccess(line_1).header("Location");

        // when
        deleteLineSuccess(null, location);

        // then
        String lineName = selectLine(null, location).extract().jsonPath().get("name");
        assertThat(lineName).isNullOrEmpty();
    }

    private ValidatableResponse selectLine(Long id, String location) {
        return RestAssured.given().log().all()
                .when().get(generateUrl(id, location))
                .then().log().all();
    }

    private ValidatableResponse selectLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all();
    }

    private ExtractableResponse<Response> insertLineSuccess(LineRequest lineRequest) {
        ExtractableResponse<Response> response = insertLine(lineRequest).extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

    private ValidatableResponse insertLine(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all();
    }

    private void updateLineSuccess(Long id, String location, LineRequest lineRequest) {
        ExtractableResponse<Response> response = updateLine(id, location, lineRequest).extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ValidatableResponse updateLine(Long id, String location, LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(generateUrl(id, location))
                .then().log().all();
    }

    private void deleteLineSuccess(Long id, String location) {
        ExtractableResponse<Response> response = deleteLine(id, location).extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ValidatableResponse deleteLine(Long id, String location) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(generateUrl(id, location))
                .then().log().all();
    }

    private String generateUrl(Long id, String location) {
        if (location != null) {
            return location;
        }
        return "/line/" + id;
    }

}