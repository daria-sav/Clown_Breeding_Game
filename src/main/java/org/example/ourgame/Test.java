package org.example.ourgame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Test {
    private static HashMap<Integer, String[]> readFileClowns (String faliName) {
    //clowns from file
    File clownsFile = new File("textFiles", faliName);
    HashMap<Integer, String[]> clowns = new HashMap<>();

    try (BufferedReader br = new BufferedReader(new FileReader(clownsFile))) {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("//")) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String[] values = {parts[0].trim(), parts[2].trim()};
                    try {
                        int key = Integer.parseInt(parts[1].trim());
                        clowns.put(key, values);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format: " + parts[1].trim());
                    }
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return clowns;
}


    public static void main(String[] args) {
        System.out.println(readFileClowns("clownsInfo.txt"));

        /*for (HashMap.Entry<Integer, String[]> entry : clowns.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue()[0] + ", " + entry.getValue()[1]);
        }*/
    }
}
