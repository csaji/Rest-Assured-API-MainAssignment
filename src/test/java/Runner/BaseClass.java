package Runner;

import Methods.userAPI;
import Methods.productAPI;
import Methods.cartAPI;
import utils.Log;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;

public class BaseClass {


    ResponseSpecification responseSpecification;
    ResponseSpecBuilder responseSpecBuilder;

    @BeforeTest
    public ResponseSpecification setup() {
        baseURI = "https://fakestoreapi.com";

        responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecification = responseSpecBuilder
                .expectStatusCode(200)
                .expectContentType("application/json")
                .build();
        return responseSpecification;
    }

    @Test(priority = 0)
    public static void execute() throws InterruptedException {
        Log.info("Test case Started");
//        userAPI.usersGETMethod();

//        productAPI.productsGETMethod();
//        productAPI.productPOSTMethod();

//        cartAPI.cartGETMethod();
    }
}
