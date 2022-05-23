package org.example;

import io.restassured.path.json.JsonPath;
import org.example.response.POSTResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PostTest extends Initializalization{


    @Test
    @Tag("Positive")
    @DisplayName("POST. Classify Cuisine (American)")
    void classifyCuisineWithoutQueryParametersTest() throws IOException {
        POSTResponse response = given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .queryParam("apiKey", getApiKey())
                .queryParam("title", "The Blarney Burger")
                .when()
                .post(getURL() + "/recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(POSTResponse.class);

        assertThat(response.getCuisine(), containsString("American"));
        assertThat(response.getCuisine(), equalToIgnoringCase("american"));
        assertThat(response.getCuisines(), hasItem("American"));
    }


    @Test
    @Tag("Positive")
    @DisplayName("POST. Classify Cuisine (Italian)")
    void classifyCuisineMediterraneanTypeTest() throws IOException {
        POSTResponse response = given()
                .queryParam("apiKey", getApiKey())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "Italian Seafood Stew")
                .param("language", "en")
                .when()
                .post(getURL() + "/recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(POSTResponse.class);

        assertThat(response.getCuisine(), containsString("Mediterranean"));
        assertThat(response.getCuisine(), equalToIgnoringCase("mediterranean"));
        assertThat(response.getCuisines(), hasItem("Italian"));
        assertThat(response.getConfidence(), not(equalTo(0f)));

    }


    @Test
    @Tag("Positive")
    @DisplayName("POST. Classify Cuisine (African)")
    void classifyCuisineAfricanTypeTest() throws IOException {
        POSTResponse response = given()
                .queryParam("apiKey", getApiKey())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "African Bean Soup")
                .param("ingredientList", "")
                .param("language", "en")
                .when()
                .post(getURL() + "/recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(POSTResponse.class);

        assertThat(response.getCuisine(), equalToIgnoringCase("african"));
        assertThat(response.getCuisines(), hasItem("African"));
        assertThat(response.getConfidence(), equalTo(0.85F));
        assertThat(response.getCuisines() instanceof ArrayList, is(true));
    }


    @Test
    @Tag("Positive")
    @DisplayName("POST. Classify Cuisine (Korean)")
    void classifyCuisineKoreanTypeTest() throws IOException {
        POSTResponse response = given()
                .queryParam("apiKey", getApiKey())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "Winter Kimchi")
                .param("language", "en")
                .when()
                .post(getURL() + "/recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(POSTResponse.class);

        assertThat(response.getCuisine(), equalToIgnoringCase("korean"));
        assertThat(response.getCuisines(), hasItem("Korean"));
        assertThat(response.getConfidence(), equalTo(0.85F));
    }


    @Test
    @Tag("Positive")
    @DisplayName("POST. Classify Cuisine (Seafood)")
    void classifyCuisineSeafoodNewBurgTest() throws IOException {
        POSTResponse response = given()
                .queryParam("apiKey", getApiKey())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "Seafood Newburg")
                .param("ingredientList", "¼ lb Scallops, ⅓ lb Shrimp, ½ lb white cod")
                .param("language", "en")
                .when()
                .post(getURL() + "/recipes/cuisine")
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(POSTResponse.class);

        assertThat(response.getCuisine(), containsString("Mediterranean"));
        assertThat(response.getCuisine(), equalToIgnoringCase("mediterranean"));
        assertThat(response.getCuisines(), hasItem("Italian"));
        assertThat(response.getConfidence(), equalTo(0f));
    }
}

