package de.luh.hci.pcl.boxhandschuh.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import de.luh.hci.pcl.boxhandschuh.model.Combination;

public class JSONFileWriter {

    private String filename;

    public JSONFileWriter(String filename) {
        super();
        this.filename = filename;
    }

    public void write(Object object) {

        Gson gson = new Gson();
        String json = gson.toJson(object);
        try {

            FileWriter file = new FileWriter(filename);
            BufferedWriter writer = new BufferedWriter(file);
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public Object read(Class<?> c) {
        Gson gson = new Gson();
        try {
            FileReader input = new FileReader(filename);
            BufferedReader reader = new BufferedReader(input);
            
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine())  != null) {
                sb.append(line);
                sb.append("\n");
            }
            System.out.println(sb.toString());
            return gson.fromJson(sb.toString(), c);
            
        } catch (IOException e) {
            return null;

        }

    }

}
