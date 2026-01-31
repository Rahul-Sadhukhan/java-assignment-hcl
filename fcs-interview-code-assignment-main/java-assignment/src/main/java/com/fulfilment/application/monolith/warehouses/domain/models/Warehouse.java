package com.fulfilment.application.monolith.warehouses.domain.models;

import java.time.LocalDateTime;

public class Warehouse {

  // unique identifier
  public String businessUnitCode;

  public String location;

  public Integer capacity;

  public Integer stock;

  public LocalDateTime createdAt;

  public LocalDateTime archivedAt;

  public Warehouse() {
  }

  public String getBusinessUnitCode() {
    return businessUnitCode;
  }

  public void setBusinessUnitCode(String businessUnitCode) {
    this.businessUnitCode = businessUnitCode;
  }

  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public int getStock() {
    return stock;
  }
  public void setStock(int stock) {
    this.stock = stock;
  }
}

