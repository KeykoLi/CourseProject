package ua.lviv.iot.course.spring.project.protection_system.csv_record;

import ua.lviv.iot.course.spring.project.protection_system.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.util.List;

public class CSVWriter {

    public static final String PATH_NOTIFICATIONS_DIR = "src/main/storage/notifications/";
    public static final String PATH_SENSORS_DIR = "src/main/storage/sensors/";
    public static final String PATH_ROOMS_DIR = "src/main/storage/rooms/";
    public static final String PATH_HALLWAYS_DIR = "src/main/storage/hallways/";


    public static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

    public void writeStrToFile(final File file, final List<String> str, final EntityCSV entityCSV) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, false),
                StandardCharsets.UTF_8))) {
            writer.write(entityCSV.getHeaders());
            for (String line : str) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public boolean checkFileEx(final String fileName) {
        return Files.exists(Path.of(fileName));
    }


    public File[] getFileOfThisEntityFromCurrentMonth(final EntityCSV entity) {
        File monthDirectory = new File(getMonthDirectoryPath(entity));

        if (monthDirectory.exists() && monthDirectory.isDirectory()) {
            if (monthDirectory.listFiles() != null) {
                return monthDirectory.listFiles();
            }
        }
        return null;
    }

    public List<String> readStrFromFile(final File file) throws IOException {
        List<String> readStr = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file),
                StandardCharsets.UTF_8))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                readStr.add(line);
            }
        }
        return readStr;
    }

    public String getFilePath(final EntityCSV entityCSV) {
        String monthDirectoryPath = getMonthDirectoryPath(entityCSV) + File.separator;

        return String.format("%s%s-%s.csv", monthDirectoryPath, entityCSV.getClass().getSimpleName(), getCurrentDate());
    }

    public String getMonthDirectoryPath(final EntityCSV entityCSV) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;

        Map<Class<?>, String> directoryPaths = new HashMap<>();
        directoryPaths.put(Notification.class, CSVWriter.PATH_NOTIFICATIONS_DIR);
        directoryPaths.put(Sensor.class, CSVWriter.PATH_SENSORS_DIR);
        directoryPaths.put(Room.class, CSVWriter.PATH_ROOMS_DIR);
        directoryPaths.put(Hallway.class, CSVWriter.PATH_HALLWAYS_DIR);

        Class<?> entityClass = entityCSV.getClass();
        return directoryPaths.getOrDefault(entityClass, null) + month;
    }
}
