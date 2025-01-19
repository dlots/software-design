package org.example;

import org.example.storage.*;

import java.io.File;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Storage memoryStorage = new InMemoryStorage();
        memoryStorage.save("Data in memory");
        System.out.println("InMemoryStorage: " + memoryStorage.retrieve(0));
        Storage fileStorage = new FileStorage();
        fileStorage.save("Data in file");
        System.out.println("FileStorage: " + fileStorage.retrieve(0));
        try (DatabaseStorage dbStorage = new DatabaseStorage()) {
            dbStorage.save("Data in db");
            System.out.printf("DB storage: %s%n", dbStorage.retrieve(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        File db = new File("example.db");
        db.delete();
    }
}