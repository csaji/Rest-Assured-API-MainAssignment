package Methods;
import utils.Log;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class cartAPI {


    ResponseSpecification responseSpecification;
    ResponseSpecBuilder responseSpecBuilder;

    public ResponseSpecification setup() {
        baseURI = "https://fakestoreapi.com";

        responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecification = responseSpecBuilder
                .expectStatusCode(200)
                .expectContentType("application/json")
                .build();
        return responseSpecification;
    }

    public static boolean productNotNull(Response users)
    {
        boolean productCheck = false;
        boolean productIdCheck = false;

        List<String> product = users.jsonPath().getList("products");
        List<String> productId = users.jsonPath().getList("products[\"productId\"]");
        System.out.println(product);
        System.out.println(productId);
        for (String prd : product) {
            if (prd == null) {
                productCheck = true;
            }
        }
        for (String prdId : productId) {
            if (prdId == null) {
                productIdCheck = true;
            }
        }
        if (productCheck==true && productIdCheck==true)
        {
            return true;
        }
        else {
            return false;
        }
    }

    public static Boolean verifyProductNotNull(Response cart_details) {
        List<List<HashMap<String, Integer>>> productList = cart_details.jsonPath().getList("products");
        Boolean checkProductList = true;
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).size() < 1) {
                checkProductList = false;
            }
        }
        Log.info("Checking Product list contain minimum one product");
        return checkProductList;
    }


    ResponseSpecification responseSpec = setup();
    @Test
    public void cartGETMethod() {

        Response user_details = given()
                .get("/carts")
                .then()
//                .log().all()
                .spec(responseSpec)
                .extract().response();

        /*verify	products are not null.*/
        assertThat(verifyProductNotNull(user_details), is(equalTo(true)));
    }
}