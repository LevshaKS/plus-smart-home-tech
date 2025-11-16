package ru.yandex.practicum.shoppingcart.exception;

public class NoCartException extends RuntimeException {

    public NoCartException(String message) {
        super(message);
    }
}
