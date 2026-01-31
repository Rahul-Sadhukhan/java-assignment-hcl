package com.fulfilment.application.monolith.warehouses.domain.usecases;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import io.restassured.http.ContentType;

@QuarkusTest
public class CreateWarehouseUseCaseTest {
    private static final String BASE_PATH = "/warehouses";
    @Test
    void createWarehouse_success() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "businessUnitCode": "BU100",
                  "location": "Bangalore",
                  "capacity": 100,
                  "stock": 50
                }
                """)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(200)
                .body("businessUnitCode", is("BU100"))
                .body("capacity", is(100))
                .body("stock", is(50));
    }

    @Test
    void createWarehouse_duplicateBusinessUnitCode() {
        // First create
        createWarehouse_success();

        // Duplicate create
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "businessUnitCode": "BU100",
                  "location": "Bangalore",
                  "capacity": 100,
                  "stock": 50
                }
                """)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(409)
                .body(containsString("Business Unit Code already exists"));
    }


}
