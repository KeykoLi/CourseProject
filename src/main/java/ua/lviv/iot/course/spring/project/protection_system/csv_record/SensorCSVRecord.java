package ua.lviv.iot.course.spring.project.protection_system.csv_record;

import ua.lviv.iot.course.spring.project.protection_system.model.*;
import ua.lviv.iot.course.spring.project.protection_system.service.NotificationService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorCSVRecord implements TCSVRecord<Sensor> {
    private final CSVWriter csvWriter = new CSVWriter();



    @Override
    public void writeToCSV(final Sensor sensor, final String pathToFiles) throws IOException {

        if (sensor == null) {
            return;
        }

        File directory = new File(pathToFiles).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            return;
        }

        boolean fileExists = csvWriter.checkFileEx(pathToFiles);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(pathToFiles, true),
                StandardCharsets.UTF_8))) {
            if (!fileExists) {
                writer.write(sensor.getHeaders());
            }
            writer.write(sensor.toCSV());
            writer.write("\n");
        }
    }

    @Override
    public void deleteFromCSV(final Integer id, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = csvWriter.readStrFromFile(file);
                for (String line : lines) {
                    Sensor sensorCSV = getObjFromCVS(line);
                    if (sensorCSV.getId().equals(id)) {
                        lines.remove(line);
                        csvWriter.writeStrToFile(file, lines, sensorCSV);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void changeObjByID(final Integer id, final Sensor sensor, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = csvWriter.readStrFromFile(file);
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Sensor sensorCSV = getObjFromCVS(line);
                    if (sensorCSV.getId().equals(id)) {
                        sensor.setId(id);
                        lines.set(i, sensor.toCSV());
                        csvWriter.writeStrToFile(file, lines, sensor);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Map<Integer, Sensor> readFromCSV(final File monthDirectory) {
        Map<Integer, Sensor> integerSensorHashMap = new HashMap<>();

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
                            Sensor sensor = getObjFromCVS(line);
                            integerSensorHashMap.put(sensor.getId(), sensor);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return integerSensorHashMap;
    }

    @Override
    public Sensor getObjFromCVS(final String csvLine) {

        String[] values = csvLine.split(",");

        var id = Integer.parseInt(values[0]);
        var idRoom = Integer.parseInt(values[1]);
        var idHallway = Integer.parseInt(values[2]);
        var type = SensorType.valueOf(values[3]);
        var statusActive = Boolean.parseBoolean(values[4]);
        var batteryPower = Integer.parseInt(values[5]);
        var sensitivity = SensitivityModForSensor.valueOf(values[6]);
        var notificationService = new NotificationService();
        Notification notification = notificationService.getById(Integer.valueOf(values[7]));
        return new Sensor(id, idRoom, idHallway, type, statusActive, batteryPower, sensitivity, notification);
    }

    @Override
    public Integer getIdOfLastObjInMap(final Map<Integer, Sensor> sensors) {
        return sensors.values().stream()
                .mapToInt(Sensor::getId)
                .max()
                .orElse(0);
    }
}
