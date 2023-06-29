package ua.lviv.iot.course.spring.project.protection_system.csv_record;


import ua.lviv.iot.course.spring.project.protection_system.model.Notification;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationCSVRecord implements TCSVRecord<Notification> {
    private final CSVWriter csvWriter = new CSVWriter();

    @Override
    public void writeToCSV(final Notification notification, final String pathToFiles) throws IOException {
        if (notification == null) {
            return;
        }

        File directory = new File(pathToFiles).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println(directory.getAbsolutePath());
            return;
        }

        boolean fileExists = csvWriter.checkFileEx(pathToFiles);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(pathToFiles, true),
                StandardCharsets.UTF_8))) {
            if (!fileExists) {
                writer.write(notification.getHeaders());
            }
            writer.write(notification.toCSV());
            writer.write("\n");
        }

    }

    @Override
    public void deleteFromCSV(final Integer id, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = csvWriter.readStrFromFile(file);
                for (String line : lines) {
                    Notification notification = getObjFromCVS(line);
                    if (notification.getId().equals(id)) {
                        lines.remove(line);
                        csvWriter.writeStrToFile(file, lines, notification);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void changeObjByID(final Integer id, final Notification notification, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = csvWriter.readStrFromFile(file);
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Notification notificationCSV = getObjFromCVS(line);
                    if (notificationCSV.getId().equals(id)) {
                        notification.setId(id);
                        lines.set(i, notification.toCSV());
                        csvWriter.writeStrToFile(file, lines, notification);
                        break;
                    }
                }
            }

        }
    }



    @Override
    public Map<Integer, Notification> readFromCSV(final File monthDirectory) {
        Map<Integer, Notification> carMap = new HashMap<>();

        if (monthDirectory.exists() && monthDirectory.isDirectory()) {
            File[] files = monthDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                            new FileInputStream(file),
                            StandardCharsets.UTF_8))) {
                        String line;
                        reader.readLine();
                        while ((line = reader.readLine()) != null) {
                            Notification notification = getObjFromCVS(line);
                            carMap.put(notification.getId(), notification);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return carMap;
    }

    @Override
    public Notification getObjFromCVS(final String csvLine) {

        String[] values = csvLine.split(",");
            Integer id = Integer.valueOf(values[0]);
            boolean enabled = Boolean.parseBoolean(values[1]);
            String message = values[2];

            return new Notification(id, enabled, message);

    }

    @Override
    public Integer getIdOfLastObjInMap(final Map<Integer, Notification> notificationMap) {
        return notificationMap.values().stream()
                .mapToInt(Notification::getId)
                .max()
                .orElse(0);
    }
}
