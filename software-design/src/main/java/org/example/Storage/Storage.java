package org.example.Storage;

public interface Storage {
    void save(String data);
    String retrieve(int id);
}