package ru.yandex.practicum.warehouse.exception;

public class NotFoundProductInWareHouseException extends RuntimeException {

    public NotFoundProductInWareHouseException(String message) {
        super(message);
    }
}
