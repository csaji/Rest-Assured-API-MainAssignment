package Methods;

import Runner.BaseClass;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONArray;
import org.testng.annotations.Test;
import utils.Log;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class productAPI extends BaseClass {

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
    public static int getSize(Response products_details)
    {
        List<Integer> productsList = products_details.jsonPath().getList("id");
        return productsList.size();
    }

    public static Boolean uniqueId(Response products_details)
    {

        List<Integer> productsIid = products_details.jsonPath().getList("id");
        HashSet<Integer> unique_products_id = new HashSet<>(productsIid);

        if(unique_products_id.size() == productsIid.size())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    ResponseSpecification responseSpec = setup();
    @Test
    public void productsGETMethod()
    {

        Response products_data = given()
                .get("/products")
                .then()
                .log().all()
                .spec(responseSpec)
                .extract().response();

        /*The API is returning 20 products.*/
        Log.info("API returinig 20 products");
        int totalProducts = getSize(products_data);
        assertThat(totalProducts, is(equalTo(20)));

            /*Each id in the response payload should be unique*/
        Log.info("ID is unique");
        Boolean uniqueIdValue = uniqueId(products_data);
        assertThat(uniqueIdValue, is(equalTo(true)));
    }

    @Test
    public static void productPOSTMethod()
    {
        /*Read json file*/
        File json_data = new File("src/test/resources/newProduct.json");
        /* 	Validate the JSON Schema for the response payload. */
        File schema = new File("src/test/resources/jsonSchema.json");
        Response newProduct = given()
                .header("Content-Type", "application/json")
                .body(json_data)
                .post("/products")
                .then().log().all()
                .body(matchesJsonSchema(schema))
//                .spec(responseSpec)
                .extract().response();

    }
}
