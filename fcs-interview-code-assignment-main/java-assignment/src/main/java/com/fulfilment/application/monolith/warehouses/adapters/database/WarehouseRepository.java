package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  @Override
  public List<Warehouse> getAll() {

    return this.listAll().stream().map(DbWarehouse::toWarehouse).toList();
  }

  @Override
  @Transactional
  public void create(Warehouse warehouse) {
    DbWarehouse dbWarehouse = DbWarehouse.fromWarehouse(warehouse);
    persist(dbWarehouse);
  }

  @Override
  @Transactional
  public void update(Warehouse warehouse) {
    Optional<DbWarehouse> existing =
            find("businessUnitCode", warehouse.businessUnitCode).firstResultOptional();

    if (existing.isEmpty()) {
      throw new IllegalStateException("Warehouse not found for update");
    }

    DbWarehouse db = existing.get();
    db.location = warehouse.location;
    db.capacity = warehouse.capacity;
    db.stock = warehouse.stock;
    db.archivedAt = warehouse.archivedAt;
  }

  @Override
  @Transactional
  public void remove(Warehouse warehouse) {
    Optional<DbWarehouse> existing =
            find("businessUnitCode", warehouse.businessUnitCode).firstResultOptional();

    if (existing.isEmpty()) {
      throw new IllegalStateException("Warehouse not found for removal");
    }

    DbWarehouse db = existing.get();
    db.archivedAt = LocalDateTime.now();
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    return find("businessUnitCode = ?1 AND archivedAt IS NULL", buCode)
            .firstResultOptional()
            .map(DbWarehouse::toWarehouse)
            .orElse(null);
  }
}
