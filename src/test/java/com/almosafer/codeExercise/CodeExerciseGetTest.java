package com.almosafer.codeExercise;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeExerciseGetTest {


    private Response response;

    // Here we will call /currency/list API
    @BeforeClass
    public void setUp() {
        final String token = "skdjfh73273$7268u2j89s";

        RestAssured.baseURI = "https://www.almosafer.com";
        response = RestAssured
                .given()
                .header("x-locale", "en")
                .header("Accept", "application/json, text/javascript")
                .header("x-currency", "SAR")
                .header("Referer", "https://www.almosafer.com/en/offers")
                .header("token", token)
                .header("cache-control", "no-cache")
                .when()
                .get("/api/system/currency/list");
    }
    // Validate response is 200
    @Test
    public void testStatusCode() {
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Status code is not 200");
    }

    // After exploring list API behaviour I've found that it always return SAR currency details in 'base' json object and other currencies in 'equivalent' array

    // Validate base JSON object
    @Test
    public void testBaseObject() {

        Assert.assertNotNull(response.jsonPath()); // Validate response not null
        Assert.assertNotNull(response.jsonPath().get("base"),"Response JSON is null"); // Validate base path is returned
        Assert.assertEquals(response.jsonPath().get("base.symbol"), "SAR","Unexpected base symbol");
        Assert.assertEquals(response.jsonPath().get("base.name"), "Saudi Riyal", "Unexpected base name");
        Assert.assertEquals(response.jsonPath().get("base.name_ar"), "ريال سعودي", "Unexpected base name_ar");
        Assert.assertEquals(response.jsonPath().get("base.symbol_native"), "ر.س.‏", "Unexpected base symbol_native");
        Assert.assertEquals(response.jsonPath().get("base.decimal_digits").toString(),"2", "Unexpected base decimal_digits");
        Assert.assertEquals(response.jsonPath().get("base.rounding").toString(),"0","Unexpected base rounding" );
        Assert.assertEquals(response.jsonPath().get("base.code"), "SAR", "Unexpected base code");
        Assert.assertEquals(response.jsonPath().get("base.name_plural"), "Saudi riyals", "Unexpected base name_plural");
    }

    // Validate other supported currencies. If currency is not found it will be added to missing currencies array and will be printed in console
    @Test
    public void testSupportedCurrencies() {
        List<String> expectedCurrencies = Arrays.asList("SAR", "AED", "QAR", "KWD", "BHD", "USD", "EUR", "GBP", "EGP", "INR", "OMR");

        List<String> actualCurrencies = response.jsonPath().getList("equivalent.code");
        List<String> missingCurrencies = new ArrayList<>();

        for (String currency : expectedCurrencies) {
            if (!actualCurrencies.contains(currency)) {
                missingCurrencies.add(currency);
            }
        }

        // Assertion after the loop is finished
        if (!missingCurrencies.isEmpty()) {
            Assert.fail("Missing currency codes: " + missingCurrencies);
        }
    }
}