package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, CsvException {
        if (args.length < 1) {
            System.out.println("Usage: <data csv> ");
            System.exit(0);
        }

        List<String[]> lines = new CSVReader(Files.newBufferedReader(Paths.get(args[0]))).readAll();

        int index = 1;
        for (String header : lines.get(0)) {
            System.out.println("[" + index++ + "] " + header);
        }

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println();
        System.out.print("Select which to use as identifier: ");
        int id = Integer.parseInt(inputReader.readLine());
        System.out.print("Select which to use as timestamp (or leave black): ");
        String timestamp = inputReader.readLine();

        int points = 0;
        String[] datapoints = new String[3];
        while (points < 3) {
            System.out.print("Select up to " + (3 - points) + " other columns as data (leave blank to exit): ");
            datapoints[points] = inputReader.readLine();
            if (datapoints[points].equals("")) break;
            points++;
        }

        boolean timeseries = !timestamp.equals("");
        System.out.println((timeseries) ? "Using " + timestamp + " as time series of data." : "Using no time series data.");
        System.out.print(points + " data in use: ");
        for (int i = 0; i < points; i++) System.out.print(lines.get(0)[i + 1]+", ");

        System.out.println();

        HashSet<LocalDate> datetimes = new HashSet<>();
        if (timeseries) {
            for (String[] d : lines.subList(1, lines.size())) {
                datetimes.add(LocalDate.parse(d[Integer.parseInt(timestamp) - 1]));
            }
        }

        int timeseriesNum = datetimes.size();
        if (timeseriesNum == 0) timeseriesNum = 1;

        HashMap<String, DrawableObject> registry = new HashMap<>();
        for (String[] data : lines.subList(1, lines.size())) {
            String identifier = data[id - 1];
            if (!registry.containsKey(identifier)) {
                DrawableObject next;
                if (points == 1) {
                    next = new d1Object(identifier, timeseriesNum);
                } else if (points == 2) {
                    next = new d2Object(identifier, timeseriesNum);
                } else if (points == 3) {
                    next = new d3Object(identifier, timeseriesNum);
                } else {
                    throw new NullPointerException();
                }

                registry.put(identifier, next);
            }

            LocalDate stamp = timeseries ? LocalDate.parse(data[Integer.parseInt(timestamp) - 1]) : LocalDate.now();

            BigDecimal[] pps = new BigDecimal[points];
            pps[0] = BigDecimal.valueOf(Double.parseDouble(data[Integer.parseInt(datapoints[0]) - 1]));
            if (points > 1) {
                pps[1] = BigDecimal.valueOf(Double.parseDouble(data[Integer.parseInt(datapoints[1]) - 1]));
            }
            if (points > 2) {
                pps[2] = BigDecimal.valueOf(Double.parseDouble(data[Integer.parseInt(datapoints[2]) - 1]));
            }

            registry.get(identifier).putPoints(stamp, pps);
        }

        JFrame app = new JFrame("Visualizer");
        app.setSize(500, 500);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Renderer renderer = new Renderer(points);

        // now that we have data in the registry, we need to covert it to a [-x, x] scale for each dimension

        for (DrawableObject obj : registry.values()) {
            renderer.objects.add(obj);
        }

        app.add(renderer);

        app.setVisible(true);
    }
}