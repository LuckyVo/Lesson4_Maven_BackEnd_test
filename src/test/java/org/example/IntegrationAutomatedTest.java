package org.example;


import org.example.request.AddToShoppingListRequest;
import org.example.response.AddToShoppingListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class IntegrationAutomatedTest extends Initializalization{

    @Test
    @Tag("Positive")
    @DisplayName("POST. Add to Shopping List")
    void addToShoppingListTest() throws IOException {

        AddToShoppingListRequest addToShoppingListRequest = new AddToShoppingListRequest();
        addToShoppingListRequest.setItem("1 package baking powder");
        addToShoppingListRequest.setAisle("Baking");
        addToShoppingListRequest.setParse(true);

        AddToShoppingListResponse response = given()
                .spec(requestSpecification)
                .queryParam("hash", getHash())
                .queryParam("apiKey", getApiKey())
                .body(addToShoppingListRequest)
                .when()
                .post(getURL() + "/mealplanner/" + getUserName() + "/shopping-list/items")
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(AddToShoppingListResponse.class);

        tearDown(getURL() + "/mealplanner/" + getUserName() + "/shopping-list/items/" + response.getId());
    }

}

