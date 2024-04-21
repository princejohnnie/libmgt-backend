package com.johnny.libmgtbackend.exception;

public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(Class<?> clazz, Long id) {
        super("Cannot find " + clazz.getSimpleName() + " with id: " + id);
    }

    public ModelNotFoundException(Class<?> clazz, String email) {
        super("Cannot find " + clazz.getSimpleName() + " with email: " + email);
    }

    public ModelNotFoundException(Class<?> clazz, String book, String patron) {
        super("Cannot find " + clazz.getSimpleName() + " with book title: " + book + " and patron: " + patron);
    }
}
