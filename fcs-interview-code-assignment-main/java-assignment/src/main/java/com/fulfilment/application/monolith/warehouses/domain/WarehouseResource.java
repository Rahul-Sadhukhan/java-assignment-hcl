package com.fulfilment.application.monolith.warehouses.domain;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface WarehouseResource {
    List<com.warehouse.api.beans.Warehouse> listAllWarehousesUnits();

    com.warehouse.api.beans.Warehouse createANewWarehouseUnit(
            @NotNull com.warehouse.api.beans.Warehouse data);

    com.warehouse.api.beans.Warehouse getAWarehouseUnitByID(String id);

    void archiveAWarehouseUnitByID(String id);

    com.warehouse.api.beans.Warehouse replaceTheCurrentActiveWarehouse(String businessUnitCode, @NotNull com.warehouse.api.beans.Warehouse data);
}
