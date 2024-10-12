package com.jvnyor.multdatasourcesjdbc.model.todo;

public record Todo(Long id, String name) {
    public Todo(String name) {
        this(null, name);
    }
}
