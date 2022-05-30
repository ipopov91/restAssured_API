import com.google.common.collect.Ordering;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class RestAssuredRequests {

    private final int OK_STATUS_CODE = 200;
    private final int NOT_FOUND_STATUS_CODE = 404;
    private final int CREATED_STATUS_CODE = 201;

    @Before
    public void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    //Case1
    @Test
    public void getRequestToPosts() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/posts")
                .then().log().all()
                .extract().response();
        List<String> jsonResponse = response.jsonPath().getList("id");

        Assert.assertEquals(OK_STATUS_CODE, response.statusCode());
        Assert.assertTrue(Ordering.natural().isOrdered(jsonResponse));

    }

    //Case2
    @Test
    public void getRequestToPostsWithQueryParam() {
        int postNumber = 99;
        String paramName = "userId";
        String paramValue = "10";

        Response response = given()
                .contentType(ContentType.JSON)
                .param(paramName, paramValue)
                .when()
                .get("/posts/" + postNumber)
                .then().log().all()
                .extract().response();

        Assert.assertEquals(OK_STATUS_CODE, response.statusCode());
        Assert.assertFalse(response.jsonPath().getString("title").isEmpty());
        Assert.assertFalse(response.jsonPath().getString("body").isEmpty());
    }

    //Case3
    @Test
    public void getRequestToPostsWithIncorrectData() {
        int postNumber = 150;

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/posts/" + postNumber)
                .then().log().all()
                .extract().response();

        Assert.assertEquals(NOT_FOUND_STATUS_CODE, response.statusCode());
    }

    //Case4
    @Test
    public void postRequest() {
        String requestBody = "{\n" +
                "  \"title\": \"AlexExample\",\n" +
                "  \"body\": \"example_body\",\n" +
                "  \"userId\": \"1\",\n" +
                "  \"id\": \"101\" \n}";

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/posts")
                .then().log().all()
                .extract().response();

        Assert.assertEquals(CREATED_STATUS_CODE, response.statusCode());
        Assert.assertEquals("AlexExample", response.jsonPath().getString("title"));
        Assert.assertEquals("example_body", response.jsonPath().getString("body"));
        Assert.assertEquals("1", response.jsonPath().getString("userId"));
        Assert.assertEquals("101", response.jsonPath().getString("id"));
    }

    //Case5
    @Test
    public void getRequestUsers() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users")
                .then().log().all()
                .extract().response();

        Assert.assertEquals(OK_STATUS_CODE, response.statusCode());
        Assert.assertEquals("Chelsey Dietrich", response.jsonPath().getString("name[4]"));
        Assert.assertEquals("Kamren", response.jsonPath().getString("username[4]"));
        Assert.assertEquals("Lucio_Hettinger@annie.ca", response.jsonPath().getString("email[4]"));
        Assert.assertEquals("Skiles Walks", response.jsonPath().getString("address.street[4]"));
        Assert.assertEquals("Suite 351", response.jsonPath().getString("address.suite[4]"));
        Assert.assertEquals("Roscoeview", response.jsonPath().getString("address.city[4]"));
        Assert.assertEquals("33263", response.jsonPath().getString("address.zipcode[4]"));
        Assert.assertEquals("-31.8129", response.jsonPath().getString("address.geo.lat[4]"));
        Assert.assertEquals("62.5342", response.jsonPath().getString("address.geo.lng[4]"));
        Assert.assertEquals("(254)954-1289", response.jsonPath().getString("phone[4]"));
        Assert.assertEquals("demarco.info", response.jsonPath().getString("website[4]"));
        Assert.assertEquals("Keebler LLC", response.jsonPath().getString("company.name[4]"));
        Assert.assertEquals("User-centric fault-tolerant solution", response.jsonPath().getString("company.catchPhrase[4]"));
        Assert.assertEquals("revolutionize end-to-end systems", response.jsonPath().getString("company.bs[4]"));
    }

    //Case6
    @Test
    public void getRequestToUsersWithQueryParam() {
        String paramName = "id";
        String paramValue = "5";

        Response response = given()
                .contentType(ContentType.JSON)
                .param(paramName, paramValue)
                .when()
                .get("/users")
                .then().log().all()
                .extract().response();

        Assert.assertEquals(OK_STATUS_CODE, response.statusCode());
        Assert.assertEquals("Chelsey Dietrich", response.jsonPath().getString("name[0]"));
        Assert.assertEquals("Kamren", response.jsonPath().getString("username[0]"));
        Assert.assertEquals("Lucio_Hettinger@annie.ca", response.jsonPath().getString("email[0]"));
        Assert.assertEquals("Skiles Walks", response.jsonPath().getString("address.street[0]"));
        Assert.assertEquals("Suite 351", response.jsonPath().getString("address.suite[0]"));
        Assert.assertEquals("Roscoeview", response.jsonPath().getString("address.city[0]"));
        Assert.assertEquals("33263", response.jsonPath().getString("address.zipcode[0]"));
        Assert.assertEquals("-31.8129", response.jsonPath().getString("address.geo.lat[0]"));
        Assert.assertEquals("62.5342", response.jsonPath().getString("address.geo.lng[0]"));
        Assert.assertEquals("(254)954-1289", response.jsonPath().getString("phone[0]"));
        Assert.assertEquals("demarco.info", response.jsonPath().getString("website[0]"));
        Assert.assertEquals("Keebler LLC", response.jsonPath().getString("company.name[0]"));
        Assert.assertEquals("User-centric fault-tolerant solution", response.jsonPath().getString("company.catchPhrase[0]"));
        Assert.assertEquals("revolutionize end-to-end systems", response.jsonPath().getString("company.bs[0]"));
    }
}
