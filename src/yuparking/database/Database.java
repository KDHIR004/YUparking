package yuparking.database;

import java.io.*;
import java.util.*;

public class Database {
    private final Map<String, String> connectionInfo;
    private final List<String> dataTables;

    public Database() {
        connectionInfo = new HashMap<>();
        connectionInfo.put("users", "src/yuparking/data/users.csv");
        connectionInfo.put("bookings", "src/yuparking/data/bookings.csv");  // future
        connectionInfo.put("parkinglots", "src/yuparking/data/parkinglots.csv");  //future
        connectionInfo.put("bookings", "src/yuparking/data/bookings.csv");


        dataTables = new ArrayList<>(connectionInfo.keySet());
    }

    // Retrieves data from csv
    public List<String[]> retrieveData(String tableName) {
        List<String[]> data = new ArrayList<>();
        String filePath = connectionInfo.get(tableName.toLowerCase());
        if (filePath == null) {
            System.out.println("Table not found: " + tableName);
            return data;
        }

        File file = new File(filePath);
        System.out.println("Looking for file at: " + file.getAbsolutePath());  // Debugging path output

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Confirm updates to a CSV file
    public void confirmUpdate(String tableName, List<String[]> newData) {
        String filePath = connectionInfo.get(tableName.toLowerCase());
        if (filePath == null) {
            System.out.println("Table not found: " + tableName);
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : newData) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
            System.out.println("Data updated successfully in " + tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getDataTables() {
        return dataTables;
    }
}
