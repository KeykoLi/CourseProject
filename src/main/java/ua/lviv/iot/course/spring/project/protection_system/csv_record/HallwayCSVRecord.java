package ua.lviv.iot.course.spring.project.protection_system.csv_record;

import ua.lviv.iot.course.spring.project.protection_system.model.Hallway;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HallwayCSVRecord implements TCSVRecord<Hallway> {
    private final CSVWriter csvWriter = new CSVWriter();

    @Override
    public void writeToCSV(final Hallway hallway, final String pathToFiles) throws IOException {
        if (hallway == null) {
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
                writer.write(hallway.getHeaders());
            }
            writer.write(hallway.toCSV());
            writer.write("\n");
        }
    }

    @Override
    public void deleteFromCSV(final Integer id, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = csvWriter.readStrFromFile(file);
                Iterator<String> iterator = lines.iterator();
                while (iterator.hasNext()) {
                    String line = iterator.next();
                    Hallway hallway = getObjFromCVS(line);
                    if (hallway.getId().equals(id)) {
                        iterator.remove();
                        csvWriter.writeStrToFile(file, lines, hallway);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Hallway getObjFromCVS(final String csvLine) {

        String[] values = csvLine.split(",");

        Integer id = Integer.valueOf(values[0]);
        String name = values[1];
        int numberOfDoors = Integer.parseInt(values[2]);

        return new Hallway(id, name, numberOfDoors);
    }

    @Override
    public void changeObjByID(final Integer id, final  Hallway hallway, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = csvWriter.readStrFromFile(file);
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Hallway roomCVS = getObjFromCVS(line);
                    if (roomCVS.getId().equals(id)) {
                        hallway.setId(id);
                        lines.set(i, hallway.toCSV());
                        csvWriter.writeStrToFile(file, lines, hallway);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Map<Integer, Hallway> readFromCSV(final File monthDirectory) {
        Map<Integer, Hallway> integerRoomHashMap = new HashMap<>();

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
                            Hallway hallway = getObjFromCVS(line);
                            integerRoomHashMap.put(hallway.getId(), hallway);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return integerRoomHashMap;
    }

    @Override
    public Integer getIdOfLastObjInMap(final Map<Integer, Hallway> hallwayMap) {
        return hallwayMap.values().stream()
                .mapToInt(Hallway::getId)
                .max()
                .orElse(0);
    }
}
