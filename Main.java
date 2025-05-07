import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        @SuppressWarnings("resource")
        Scanner in = new Scanner(System.in);

        System.out.print("Enter number of threads to run in parallel: ");
        int tn;
        while (true) {
            try {
                tn = Integer.parseInt(in.nextLine().trim());
                if (tn <= 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid positive integer for threads: ");
            }
        }
        
        // Load dataset
        List<Point> allPoints = new ArrayList<>();
        File file = new File("greek_cities_dataset.txt");
        if (!file.exists() || !file.canRead()) {
            System.out.println("Failed to load dataset. File does not exist or cannot be read.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length != 3) continue;
                String name = parts[0];
                double lat = Double.parseDouble(parts[1]);
                double lon = Double.parseDouble(parts[2]);

                Point temp = new Point(tn);
                temp.name = name;
                temp.x_c = lat;
                temp.y_c = lon;
                Arrays.fill(temp.thread_flag, true);
                allPoints.add(temp);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading the dataset: " + e.getMessage());
            return;
        }

        if (allPoints.size() <= 1) {
            System.out.println("Dataset must contain more than one city.");
            return;
        }

        System.out.print("Enter how many cities to use (max " + allPoints.size() + "): ");
        int cityCount;
        while (true) {
            try {
                cityCount = Integer.parseInt(in.nextLine().trim());
                if (cityCount < 2 || cityCount > allPoints.size()) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid integer between 2 and " + allPoints.size() + ": ");
            }
        }
        
        // **Preview the names of cities loaded from the dataset (up to the `cityCount` number)**
        System.out.println("\nCities loaded from the dataset:");
        for (int i = 0; i < cityCount; i++) {
            if (i < allPoints.size()) {
                System.out.println(allPoints.get(i).name);
            }
        }

        // Copy selected cities
        TSP.p.clear();
        for (int i = 0; i < cityCount; i++) {
            TSP.p.add(allPoints.get(i));
        }
        TSP.n = cityCount;

        TSP[] t_arr = new TSP[tn];
        for (int i = 0; i < tn; i++) t_arr[i] = new TSP(i);
        TSP.tn = tn;
        TSP.init(tn);
        for (int i = 0; i < tn; i++) TSP.min[i] = Double.MAX_VALUE;

        // Starting point
        System.out.print("Enter starting point: ");
        while (true) {
            String startName = in.nextLine().trim();
            boolean found = false;
            for (int i = 0; i < TSP.n; i++) {
                if (startName.equalsIgnoreCase(TSP.p.get(i).name)) {
                    TSP.sp = TSP.p.get(i);
                    TSP.isp = i;
                    for (int j = 0; j < tn; j++) {
                        TSP.p.get(i).thread_flag[j] = false;
                    }
                    found = true;
                    break;
                }
            }
            if (found) break;
            System.out.print("City not found. Enter a valid starting city name: ");
        }
        
        // Start threads
        for (TSP t : t_arr) t.start();
        for (TSP t : t_arr) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Thread error: " + e.getMessage());
            }
        }
        
        // Result
        double min_f = Double.MAX_VALUE;
        int ans_index = -1;
        for (int i = 0; i < tn; i++) {
            if (TSP.answer[i] != null && TSP.min[i] < min_f) {
                min_f = TSP.min[i];
                ans_index = i;
            }
        }

        if (ans_index == -1 || TSP.answer[ans_index] == null) {
            System.out.println("No valid path found.");
            return;
        }

        System.out.print("The optimal path is: " + TSP.p.get(TSP.isp).name + "-->");
        for (int i = 1; i <= TSP.n - 1; i++) {
            int idx = TSP.answer[ans_index].get(i).intValue();
            System.out.print(TSP.p.get(idx).name + "-->");
        }

        System.out.println(TSP.p.get(TSP.isp).name);

        System.out.printf("The total distance covered in the trip is: %.5f\n", min_f);
        
    }
}
