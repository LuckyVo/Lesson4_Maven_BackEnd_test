package org.example;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;


public abstract class Initializalization {

    ResponseSpecification responseSpecification = null;
    RequestSpecification requestSpecification = null;

    final static java.util.Properties prop = new java.util.Properties();

    private static void loadProperties() throws IOException {
        try (FileInputStream configFile = new FileInputStream("src/test/resources/properties.properties")) {
            prop.load(configFile);
        }
    }

    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeAll
    static void beforeAll() {
        RestAssured.filters(new AllureRestAssured());
    }

    @BeforeEach
    void beforeEach() throws IOException {
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();

        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", getApiKey())
                .addQueryParam("includeNutrition", "false")
                .setContentType(ContentType.JSON)
//                .log(LogDetail.ALL)
                .build();
    }

    public static String getURL() throws IOException {
        loadProperties();
        return prop.getProperty("baseURL");
    }

    public static String getApiKey() throws IOException {
        loadProperties();
        return prop.getProperty("apiKey");
    }

    public static String getHash() throws IOException {
        loadProperties();
        return prop.getProperty("hash");
    }

    public static String getUserName() throws IOException {
        loadProperties();
        return prop.getProperty("userName");
    }

    static void tearDown(String url) throws IOException {
        given()
                .queryParam("hash", getHash())
                .queryParam("apiKey", getApiKey())
                .delete(url)
                .then()
                .statusCode(200);
    }

}

