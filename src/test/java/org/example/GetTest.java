package org.example;

import io.restassured.path.json.JsonPath;
import org.example.response.GETResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetTest extends Initializalization {


    @Test
    @Tag("Negative")
    @DisplayName("GET. No apiKey")
    void getRecipeBurgerNegativeTest() throws IOException {
        given()
                .log()
                .all()
                .queryParam("query", "burger")
                .expect()
                .body("status", equalTo("failure"))
                .body("code", equalTo(401))
                .when()
                .get(getURL() + "/recipes/complexSearch")
                .prettyPeek()
                .then()
                .statusCode(401);
    }


    @Test
    @Tag("Positive")
    @DisplayName("GET. Search recipe pasta")
    void getRecipePastaPositiveTest() throws IOException {
        GETResponse response = given()
                .spec(requestSpecification)
                .queryParam("query", "pasta")
                .when()
                .get(getURL() + "/recipes/complexSearch")
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(GETResponse.class);

//        for (GETResponse.Result res : response.getResults()) {
//            assertThat(res.getTitle(), containsString("Pasta"));
//        }

        response.getResults().forEach(product ->
                assertThat(product.getTitle(), containsString("Pasta")));
    }


    @Test
    @Tag("Positive")
    @DisplayName("GET. Search recipe salad")
    void getRecipeSaladPositiveTest() throws IOException {
        GETResponse response = given()
                .spec(requestSpecification)
                .queryParam("query", "salad")
                .queryParam("sort", "calories")
                .when()
                .get(getURL() + "/recipes/complexSearch")
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(GETResponse.class);

//        ToDO
//        int index = getMaxIndex(response);
//        while (index >= 0) {
//            assertThat(response.get("results[" + index + "].nutrition.nutrients[0].amount"), lessThanOrEqualTo(1029.68F));
//            index--;
//        }

//                for (GETResponse.Result res : response.getResults()) {
//            assertThat(res.getTitle(), containsString("Pasta"));
//        }
//
//        response.getResults().forEach(product ->
//                assertThat(product.getTitle(), containsString("Pasta")));
    }


    @Test
    @Tag("Positive")
    @DisplayName("GET. Search by cooking time")
    void getSearchByCookingTimeTest() throws IOException {
        GETResponse response = given()
                .spec(requestSpecification)
                .queryParam("addRecipeInformation", true)
                .queryParam("maxReadyTime", 5)
                .when()
                .get(getURL() + "/recipes/complexSearch")
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(GETResponse.class);

        //        for (GETResponse.Result res : response.getResults()) {
//            assertThat(res.getReadyInMinutes(), lessThanOrEqualTo(5));
//        }
        response.getResults().forEach(product ->
                assertThat(product.getReadyInMinutes(), lessThanOrEqualTo(5)));
    }


    @Test
    @Tag("Positive")
    @DisplayName("GET. Search recipe steak")
    void getRecipeSteakPositiveTest() throws IOException {
        GETResponse response = given()
                .spec(requestSpecification)
                .queryParam("query", "steak")
                .queryParam("sort", "calories")
                .queryParam("sortDirection", "asc")
                .queryParam("minCalories", 100)
                .when()
                .get(getURL() + "/recipes/complexSearch")
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(GETResponse.class);

        int i = 0;
        float previousValue = 100f;
        for (GETResponse.Result res : response.getResults()) {
            for (GETResponse.Nutrient nutr : res.nutrition.getNutrients()) {
                assertThat(nutr.getName(), equalToIgnoringCase("Calories"));
                if (i == 0) {
                    assertThat(nutr.getAmount(), greaterThanOrEqualTo(100f));
                }
                if (i != 0) {
                    assertThat(nutr.getAmount() >= previousValue, is(true));//
                }
                previousValue = nutr.getAmount();
            }
            i++;
        }
    }


    @Test
    @Tag("Positive")
    @DisplayName("GET. Search recipe soup")
    void getRecipeSoupPositiveTest() throws IOException {
        GETResponse response = given()
                .spec(requestSpecification)
                .queryParam("query", "soup")
                .queryParam("addRecipeInformation", true)
                .queryParam("diet", "vegetarian")
                .when()
                .get(getURL() + "/recipes/complexSearch")
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(GETResponse.class);

        for (GETResponse.Result res : response.getResults()) {
            assertThat(res.getVegetarian(), is(true));
        }

    }


}






