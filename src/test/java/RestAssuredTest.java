import com.google.common.collect.Ordering;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static io.restassured.RestAssured.given;

public class RestAssuredTest {

    private static RequestSpecification spec;

    private final int OK_STATUS_CODE = 200;
    private final int NOT_FOUND_STATUS_CODE = 404;
    private final int CREATED_STATUS_CODE = 201;

    @BeforeAll
    public static void initSpec(){
        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .build();
    }

    /**
     * Case 1
     */
    @Test
    public void getRequestToPosts() {
        Response response = given()
                .spec(spec)
                .when()
                .get("/posts")
                .then().log().all()
                .extract().response();
        List<String> jsonResponse = response.jsonPath().get("id");

        Assertions.assertEquals(OK_STATUS_CODE, response.statusCode());
        Assertions.assertTrue(Ordering.natural().isOrdered(jsonResponse));

    }

    /**
     * Case 2
     */
    @Test
    public void getRequestToPostsWithQueryParam() {
        int postNumber = 99;
        String paramName = "userId";
        String paramValue = "10";

        Response response = given()
                .spec(spec)
                .param(paramName, paramValue)
                .when()
                .get("/posts/" + postNumber)
                .then().log().all()
                .extract().response();

        Assertions.assertEquals(OK_STATUS_CODE, response.statusCode());
        Assertions.assertFalse(response.jsonPath().getString("title").isEmpty());
        Assertions.assertFalse(response.jsonPath().getString("body").isEmpty());
    }

    /**
     * Case 3
     */
    @Test
    public void getRequestToPostsWithIncorrectData() {
        int postNumber = 150;

        Response response = given()
                .spec(spec)
                .when()
                .get("/posts/" + postNumber)
                .then().log().all()
                .body(Matchers.anything())
                .extract().response();

        Assertions.assertEquals(NOT_FOUND_STATUS_CODE, response.statusCode());
    }

    /**
     * Case 4
     */
    @Test
    public void postRequest() {
        String requestBody = "{\n" +
                "  \"title\": \"AlexExample\",\n" +
                "  \"body\": \"example_body\",\n" +
                "  \"userId\": \"1\",\n" +
                "  \"id\": \"101\" \n}";

        Response response = given()
                .spec(spec)
                .and()
                .body(requestBody)
                .when()
                .post("/posts")
                .then().log().all()
                .extract().response();

        Assertions.assertEquals(CREATED_STATUS_CODE, response.statusCode());
        Assertions.assertEquals("AlexExample", response.jsonPath().getString("title"));
        Assertions.assertEquals("example_body", response.jsonPath().getString("body"));
        Assertions.assertEquals("1", response.jsonPath().getString("userId"));
        Assertions.assertEquals("101", response.jsonPath().getString("id"));
    }

    /**
     * Case 5
     */
    @Test
    public void getRequestUsers() {
        Response response = given()
                .spec(spec)
                .when()
                .get("/users")
                .then().log().all()
                .extract().response();

        Assertions.assertEquals(OK_STATUS_CODE, response.statusCode());
        Assertions.assertEquals("Chelsey Dietrich", response.jsonPath().getString("name[4]"));
        Assertions.assertEquals("Kamren", response.jsonPath().getString("username[4]"));
        Assertions.assertEquals("Lucio_Hettinger@annie.ca", response.jsonPath().getString("email[4]"));
        Assertions.assertEquals("Skiles Walks", response.jsonPath().getString("address.street[4]"));
        Assertions.assertEquals("Suite 351", response.jsonPath().getString("address.suite[4]"));
        Assertions.assertEquals("Roscoeview", response.jsonPath().getString("address.city[4]"));
        Assertions.assertEquals("33263", response.jsonPath().getString("address.zipcode[4]"));
        Assertions.assertEquals("-31.8129", response.jsonPath().getString("address.geo.lat[4]"));
        Assertions.assertEquals("62.5342", response.jsonPath().getString("address.geo.lng[4]"));
        Assertions.assertEquals("(254)954-1289", response.jsonPath().getString("phone[4]"));
        Assertions.assertEquals("demarco.info", response.jsonPath().getString("website[4]"));
        Assertions.assertEquals("Keebler LLC", response.jsonPath().getString("company.name[4]"));
        Assertions.assertEquals("User-centric fault-tolerant solution", response.jsonPath().getString("company.catchPhrase[4]"));
        Assertions.assertEquals("revolutionize end-to-end systems", response.jsonPath().getString("company.bs[4]"));
    }

    /**
     * Case 6
     */
    @Test
    public void getRequestToUsersWithQueryParam() {
        String paramName = "id";
        String paramValue = "5";

        Response response = given()
                .spec(spec)
                .param(paramName, paramValue)
                .when()
                .get("/users")
                .then().log().all()
                .extract().response();

        Assertions.assertEquals(OK_STATUS_CODE, response.statusCode());
        Assertions.assertEquals("Chelsey Dietrich", response.jsonPath().getString("name[0]"));
        Assertions.assertEquals("Kamren", response.jsonPath().getString("username[0]"));
        Assertions.assertEquals("Lucio_Hettinger@annie.ca", response.jsonPath().getString("email[0]"));
        Assertions.assertEquals("Skiles Walks", response.jsonPath().getString("address.street[0]"));
        Assertions.assertEquals("Suite 351", response.jsonPath().getString("address.suite[0]"));
        Assertions.assertEquals("Roscoeview", response.jsonPath().getString("address.city[0]"));
        Assertions.assertEquals("33263", response.jsonPath().getString("address.zipcode[0]"));
        Assertions.assertEquals("-31.8129", response.jsonPath().getString("address.geo.lat[0]"));
        Assertions.assertEquals("62.5342", response.jsonPath().getString("address.geo.lng[0]"));
        Assertions.assertEquals("(254)954-1289", response.jsonPath().getString("phone[0]"));
        Assertions.assertEquals("demarco.info", response.jsonPath().getString("website[0]"));
        Assertions.assertEquals("Keebler LLC", response.jsonPath().getString("company.name[0]"));
        Assertions.assertEquals("User-centric fault-tolerant solution", response.jsonPath().getString("company.catchPhrase[0]"));
        Assertions.assertEquals("revolutionize end-to-end systems", response.jsonPath().getString("company.bs[0]"));
    }
}
