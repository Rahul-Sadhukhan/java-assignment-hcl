package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;

@RequestScoped
public class WarehouseResourceImpl implements com.warehouse.api.WarehouseResource {

  @Inject private WarehouseRepository warehouseRepository;

  @Override
  public List<com.warehouse.api.beans.Warehouse> listAllWarehousesUnits() {
    return warehouseRepository.getAll().stream().map(this::toWarehouseResponse)
            .toList();
  }

  @Override
  public com.warehouse.api.beans.Warehouse createANewWarehouseUnit(
          @NotNull com.warehouse.api.beans.Warehouse data) {

      // Business Unit Code must be unique
      if (warehouseRepository.findByBusinessUnitCode(data.getBusinessUnitCode()) != null) {
        throw new WebApplicationException(
                "Warehouse with this Business Unit Code already exists", Response.Status.CONFLICT);
        }
        Warehouse warehouse = toDomainWarehouse(data);
        warehouse.createdAt = LocalDateTime.now();

        warehouseRepository.create(warehouse);

        return toWarehouseResponse(warehouse);
  }

  @Override
  public com.warehouse.api.beans.Warehouse getAWarehouseUnitByID(String id) {
      Warehouse warehouse = warehouseRepository.findByBusinessUnitCode(id);

      if (warehouse == null) {
        throw new WebApplicationException(
                "Warehouse not found", Response.Status.NOT_FOUND);
      }

      return toWarehouseResponse(warehouse);
  }

  @Override
  public void archiveAWarehouseUnitByID(String id) {
      Warehouse warehouse = warehouseRepository.findByBusinessUnitCode(id);

      if (warehouse == null) {
        throw new WebApplicationException(
                "Warehouse not found", Response.Status.NOT_FOUND);
      }

      warehouse.archivedAt = LocalDateTime.now();
      warehouseRepository.remove(warehouse);
  }

  @Override
  public com.warehouse.api.beans.Warehouse replaceTheCurrentActiveWarehouse(String businessUnitCode,
                                                                            @NotNull com.warehouse.api.beans.Warehouse data) {

        Warehouse existing = warehouseRepository.findByBusinessUnitCode(businessUnitCode);

    if (existing == null) {
      throw new WebApplicationException(
              "Warehouse not found", Response.Status.NOT_FOUND);
    }

    if (data.getCapacity() < existing.stock) {
      throw new WebApplicationException(
              "New warehouse capacity cannot be lower than existing stock",
              Response.Status.BAD_REQUEST);
    }

    if (data.getStock() != existing.stock) {
      throw new WebApplicationException(
              "Stock must match existing warehouse stock",
              Response.Status.BAD_REQUEST);
    }

    existing.archivedAt = LocalDateTime.now();
    warehouseRepository.update(existing);

    Warehouse replacement = toDomainWarehouse(data);
    replacement.createdAt = LocalDateTime.now();

    warehouseRepository.create(replacement);

    return toWarehouseResponse(replacement);
  }

  private com.warehouse.api.beans.Warehouse toWarehouseResponse(Warehouse warehouse) {
    var response = new com.warehouse.api.beans.Warehouse();
    response.setBusinessUnitCode(warehouse.businessUnitCode);
    response.setLocation(warehouse.location);
    response.setCapacity(warehouse.capacity);
    response.setStock(warehouse.stock);
    return response;
  }
  private Warehouse
  toDomainWarehouse(com.warehouse.api.beans.Warehouse data) {
    var w = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
    w.businessUnitCode = data.getBusinessUnitCode();
    w.location = data.getLocation();
    w.capacity = data.getCapacity();
    w.stock = data.getStock();
    return w;
  }
}
