package Methods;

import Runner.BaseClass;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;
import utils.Log;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class userAPI extends BaseClass {


    ResponseSpecification responseSpecification;
    ResponseSpecBuilder responseSpecBuilder;

    public ResponseSpecification setup()
    {
        baseURI = "https://fakestoreapi.com";

        responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecification = responseSpecBuilder
                .expectStatusCode(200)
                .expectContentType("application/json")
                .build();
        return responseSpecification;
    }

    public static boolean find_users(Response users) {
        boolean don = false, david = false, miriam = false, names = false;

        List<String> first_names = users.jsonPath().getList("name[\"firstname\"]");
        for (String name : first_names) {
            if (name.equals("don")) {
                don = true;
            }
            if (name.equals("david")) {
                david = true;
            }
            if (name.equals("miriam")) {
                miriam = true;
            }

            if (don == true && david == true && miriam == true) {
                names = true;
                break;
            }
        }

        if (names) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean lat_long_null(Response users) {
        boolean lat_check = false;
        boolean long_check = false;

        List<String> latitude = users.jsonPath().getList("address[\"geolocation\"][\"lat\"]");
        List<String> longitude = users.jsonPath().getList("address[\"geolocation\"][\"long\"]");
//        System.out.println(latitudes);
//        System.out.println(longitudes);
        for (String lat : latitude) {
//            System.out.print(lat + " ");
            if (lat == null) {
                lat_check = true;
            }
        }
//        System.out.println();
        for (String longtd : longitude) {
//            System.out.print(longi + " ");
            if (longtd == null) {
                long_check = true;
            }
        }

        if (lat_check == true || long_check == true) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validPassword(Response users) {
        List<String> passwords = users.jsonPath().getList("password");
        boolean validPsw = true;

        String constraints = "^.*[a-zA-Z0-9!@#$%^&*]+.*$";
        Pattern pattern = Pattern.compile(constraints);
        for (String password : passwords) {
            Matcher matcher = pattern.matcher(password);
            if (!matcher.matches()) {
                validPsw = false;
            }
            Log.info(password);
        }
        return validPsw;
    }


    ResponseSpecification responseSpec = setup();
    @Test
    public void usersGETMethod() {

        Response userDetails = given()
                .get("/users")
                .then()
//                .log().all()
                .spec(responseSpec)
                .extract().response();

        /*verify david, don, miriam are present in response payload:*/
        assertThat(find_users(userDetails), is(equalTo(true)));
//
       /*verify	lat and long in the response are not null.*/
        assertThat(lat_long_null(userDetails), is(equalTo(false)));

        /*verify passwords are valid*/
        assertThat(validPassword(userDetails), is(equalTo(true)));
    }
}
