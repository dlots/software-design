package org.example.storage;

public interface Storage {
    void save(String data);
    String retrieve(int id);
}