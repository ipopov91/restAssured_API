import com.google.common.collect.Ordering;
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
import static org.hamcrest.Matchers.containsString;

public class RestAssuredTest {

    private static RequestSpecification spec;

    private String postPath = "/posts/";
    private String userPath = "/users/";

    String requestBody = "{\n" +
            "  \"title\": \"AlexExample\",\n" +
            "  \"body\": \"example_body\",\n" +
            "  \"userId\": \"1\",\n" +
            "  \"id\": \"101\" \n}";

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
    public void getResponseFromPosts() {
        Response response = given()
                .spec(spec)
                .when()
                .get(postPath)
                .then().statusCode(200).log().all()
                .extract().response();
        List<String> jsonResponse = response.jsonPath().get("id");

        Assertions.assertTrue(Ordering.natural().isOrdered(jsonResponse));

    }

    /**
     * Case 2
     */
    @Test
    public void getResponseFromPostsWithQueryParam() {
        int postNumber = 99;
        String paramName = "userId";
        String paramValue = "10";

        Response response = given()
                .spec(spec)
                .param(paramName, paramValue)
                .when()
                .get(postPath + postNumber)
                .then().statusCode(200).log().all()
                .extract().response();

        Assertions.assertFalse(response.jsonPath().getString("title").isEmpty());
        Assertions.assertFalse(response.jsonPath().getString("body").isEmpty());
    }

    /**
     * Case 3
     */
    @Test
    public void getResponseFromPostsWithIncorrectData() {
        int postNumber = 150;

        Response response = given()
                .spec(spec)
                .when()
                .get(postPath + postNumber)
                .then().statusCode(404).log().all()
                .body(Matchers.anything())
                .extract().response();
    }

    /**
     * Case 4
     */
    @Test
    public void addPost() {
        Response response = given()
                .spec(spec)
                .and()
                .body(requestBody)
                .when()
                .post(postPath)
                .then().statusCode(201).log().all()
                .body(containsString("id"))
                .extract().response();

        Assertions.assertEquals("AlexExample", response.jsonPath().getString("title"));
        Assertions.assertEquals("example_body", response.jsonPath().getString("body"));
        Assertions.assertEquals("1", response.jsonPath().getString("userId"));
        Assertions.assertEquals("101", response.jsonPath().getString("id"));
    }

    /**
     * Case 5
     */
    @Test
    public void getResponseUsers() {
        Response response = given()
                .spec(spec)
                .when()
                .get(userPath)
                .then().statusCode(200).log().all()
                .extract().response();

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
    public void getResponseFromUsersWithQueryParam() {
        String paramName = "id";
        String paramValue = "5";

        Response response = given()
                .spec(spec)
                .param(paramName, paramValue)
                .when()
                .get(userPath)
                .then().statusCode(200).log().all()
                .and().body(containsString("Chelsey Dietrich"))
                .and().body(containsString("Kamren"))
                .and().body(containsString("Lucio_Hettinger@annie.ca"))
                .and().body(containsString("Skiles Walks"))
                .and().body(containsString("Suite 351"))
                .and().body(containsString("Roscoeview"))
                .and().body(containsString("33263"))
                .and().body(containsString("-31.8129"))
                .and().body(containsString("62.5342"))
                .and().body(containsString("(254)954-1289"))
                .and().body(containsString("demarco.info"))
                .and().body(containsString("Keebler LLC"))
                .and().body(containsString("User-centric fault-tolerant solution"))
                .and().body(containsString("revolutionize end-to-end systems"))
                .extract().response();
    }
}
