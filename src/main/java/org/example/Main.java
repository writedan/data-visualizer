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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, CsvException, InterruptedException {
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
        System.out.print("Select which to use as image: ");
        int imagefield = Integer.parseInt(inputReader.readLine());
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

        System.out.println(datetimes);

        int timeseriesNum = datetimes.size();
        if (timeseriesNum == 0) timeseriesNum = 1;

        HashMap<String, DrawableObject> registry = new HashMap<>();
        for (String[] data : lines.subList(1, lines.size())) {
            String identifier = data[imagefield - 1];
            System.out.println("data[" + (imagefield - 1) + "] = ");
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

            System.out.println(identifier + " ---- " + Arrays.asList(data));
            registry.get(identifier).putPoints(stamp, pps);
        }

        JFrame app = new JFrame("Visualizer");
        app.setSize(1000, 1000);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Renderer renderer = new Renderer(points);

        // now that we have data in the registry, we need to covert it to a [-x, x] scale for each dimension

        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
        for (DrawableObject obj : registry.values()) {
            System.out.println(obj);
            if (points == 1) {
                for (BigDecimal x : ((d1Object)obj).x) {
                    if (x.doubleValue() > maxX) maxX = x.doubleValue();
                    if (x.doubleValue() < minX) minX = x.doubleValue();
                }
            } else if (points == 2) {
                for (BigDecimal x : ((d1Object)obj).x) {
                    if (x.doubleValue() > maxX) maxX = x.doubleValue();
                    if (x.doubleValue() < minX) minX = x.doubleValue();
                }
                for (BigDecimal y : ((d2Object)obj).y) {
                    if (y.doubleValue() > maxY) maxY = y.doubleValue();
                    if (y.doubleValue() < minY) minY = y.doubleValue();
                }
            } else {
                for (BigDecimal x : ((d1Object) obj).x) {
                    if (x.doubleValue() > maxX) maxX = x.doubleValue();
                    if (x.doubleValue() < minX) minX = x.doubleValue();
                }
                for (BigDecimal y : ((d2Object) obj).y) {
                    if (y.doubleValue() > maxY) maxY = y.doubleValue();
                    if (y.doubleValue() < minY) minY = y.doubleValue();
                }
                for (BigDecimal z : ((d3Object) obj).z) {
                    if (z.doubleValue() > maxZ) maxZ = z.doubleValue();
                    if (z.doubleValue() < minZ) minZ = z.doubleValue();
                }
            }
            renderer.objects.add(obj);
        }

        System.out.println("["+minX+","+maxX+"]");

        renderer.index = 0;

        for (DrawableObject obj : renderer.objects) {
            // we will now normalize each data value, since we knowthe min and max in each dimension.
            // this will result is a value from [-1,1] which will we scale up to 500
            int a = 0, b= 900;

            if (points == 1) {
                d1Object d1 = (d1Object) obj;
                for (int i = 0; i < d1.timestamps.length; i++) {
                    System.out.println(a + (d1.x[i].doubleValue() - minX));
                    d1.x[i] = BigDecimal.valueOf(a + (d1.x[i].doubleValue() - minX) * (b - a) / (maxX - minX));
                }
            } else if (points == 2) {
                d2Object d2 = (d2Object) obj;
                for (int i = 0; i < d2.timestamps.length; i++) {
                    d2.x[i] = BigDecimal.valueOf(a + (d2.x[i].doubleValue() - minX) * (b - a) / (maxX - minX));
                    d2.y[i] = BigDecimal.valueOf(a + (d2.y[i].doubleValue() - minY) * (b - a) / (maxY - minY));
                }
            }
        }

        app.add(renderer);

        app.setVisible(true);

        if (timeseries) {
            new Thread(){
                @Override
                public void run() {
                    while (true) {
                        try {
                            app.setTitle("Visualizer -- " + renderer.objects.get(0).timestamps[renderer.index]);
                            renderer.index += 1;
                            renderer.index %= 11;
                            renderer.validate();
                            renderer.repaint();
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }.start();
        }
    }
}