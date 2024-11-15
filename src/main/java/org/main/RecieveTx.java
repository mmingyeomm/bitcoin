package org.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RecieveTx {


    public void readFile() {
        File file = new File("C:\\work\\POJO\\src\\main\\java\\mempool\\transactions.json");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String st;
            while ((st = br.readLine()) != null) {
                System.out.println(st);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }







}
