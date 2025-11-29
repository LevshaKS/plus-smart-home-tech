package ru.yandex.practicum.warehouse.dal;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.interactionapi.model.BookedProductDto;
import ru.yandex.practicum.warehouse.model.Booking;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {
    BookedProductDto productToBookingProductDto(Booking booking);
}
