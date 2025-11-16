package ru.yandex.practicum.warehouse.dal;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.interactionapi.model.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.model.Warehouse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
    Warehouse newProductToWarehouse(NewProductInWarehouseRequest newProductInWarehouse);

}
