package ru.yandex.practicum.shoppingcart.exception;

public class NullAuthorizedException extends RuntimeException {
    public NullAuthorizedException(String message) {
        super(message);
    }
}
