package ru.yandex.practicum.interactionapi.circuitBreaker;

public class WarehouseServerError extends RuntimeException {
    public WarehouseServerError(String message) {
        super(message);
    }
}
