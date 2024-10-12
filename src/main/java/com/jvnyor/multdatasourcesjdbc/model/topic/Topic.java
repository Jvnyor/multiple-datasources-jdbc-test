package com.jvnyor.multdatasourcesjdbc.model.topic;

public record Topic(Long id, String name) {
    public Topic(String name) {
        this(null, name);
    }
}
