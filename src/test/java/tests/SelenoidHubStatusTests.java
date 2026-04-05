package tests;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class SelenoidHubStatusTests extends TestBase{
    @Test
    public void statusTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void unauthorizedStatusTest() {
        given()
                .log().all()
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(401);
    }

    @Test
    public void presenceOfTextStatusTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("value.message", containsString("Selenoid 1.11.3")) // не поняла, какую именно проверку сделать, поэтому оставила две
                .body("value.message", equalTo("Selenoid 1.11.3 built at 2024-05-25_12:34:40PM"))
                .body("value.ready", equalTo(true));
    }

    @Test
    public void schemaStatusTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/status_response_schema.json"));
    }

    @Test
    public void passwordErrorStatusTest() {
        given()
                .log().all()
                .auth().basic("user1", "12345")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(401);
    }

    @Test
    public void emptyAuthStatusTest() {
        given()
                .log().all()
                .auth().basic("", "")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(401);
    }

    @Test
    public void emptyAutStatusTest() {
        given()
                .log().all()
                .auth().basic("user<script>", "pass'\"\\")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(401);
    }
}