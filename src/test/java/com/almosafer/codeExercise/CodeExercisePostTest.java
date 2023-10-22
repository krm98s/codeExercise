package com.almosafer.codeExercise;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CodeExercisePostTest {

   private Response response;
    @BeforeClass
    public void advanceSearchPost  () {

        JSONObject requestBody = new JSONObject();
        requestBody.put("sortBy", "rank");
        requestBody.put("sortOrder", "DESC");
        requestBody.put("rankType", "dynamic");
        requestBody.put("pageNo", 1);
        requestBody.put("pageSize", 10);
        requestBody.put("searchId", "PROPERTY_SR-31994");
        requestBody.put("searchText", "chalet");

        final String token = "skdjfh73273$7268u2j89s";

        RestAssured.baseURI = "https://www.almosafer.com";
        response = RestAssured
                .given()
                .header("x-locale", "en")
                .header("Accept", "application/json, text/javascript")
                .header("x-currency", "SAR")
                .header("Referer", "https://www.almosafer.com/en/offers")
                .header("x-authorization", token)
                .header("cache-control", "no-cache")
                .header("Content-Type", "application/json")
                .header("Connection","keep-alive")

                .body(requestBody.toString())
                .when()
                .post("/api/accommodation/property/advance-search");
    } // karam


    @Test
    public void testStatusCode() {
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Status code is not as expected (200).");

    }

    // Validate if totalCount is above 1  to assure that se
    @Test
    public void testSearch(){
        Assert.assertNotNull(response.jsonPath(), "Response is returned as null"); // Validate response not null
        Assert.assertEquals(response.jsonPath().get("currencyCode"),"SAR", "Incorrect currency returned"); // Validate base path is returned
        Assert.assertTrue((int) response.jsonPath().get("totalCount") >= 1, "totalCount is less than 1, no search results");
    }
}