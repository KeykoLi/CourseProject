package ua.lviv.iot.course.spring.project.protection_system.csv_record;

import ua.lviv.iot.course.spring.project.protection_system.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RoomCSVRecord implements TCSVRecord<Room> {
    private final CSVWriter csvWriter = new CSVWriter();

    @Override
    public void writeToCSV(final Room room, final String pathToFiles) throws IOException {
        if (room == null) {
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
                writer.write(room.getHeaders());
            }
            writer.write(room.toCSV());
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
                    Room room = getObjFromCVS(line);
                    if (room.getId().equals(id)) {
                        iterator.remove();
                        csvWriter.writeStrToFile(file, lines, room);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Room getObjFromCVS(final String csvLine) {

        String[] values = csvLine.split(",");

        Integer id = Integer.valueOf(values[0]);
        String name = values[1];
        int numberOfWindows = Integer.parseInt(values[2]);

        return new Room(id, name, numberOfWindows);
    }

    @Override
    public void changeObjByID(final Integer id, final Room room, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = csvWriter.readStrFromFile(file);
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Room roomCVS = getObjFromCVS(line);
                    if (roomCVS.getId().equals(id)) {
                        room.setId(id);
                        lines.set(i, room.toCSV());
                        csvWriter.writeStrToFile(file, lines, room);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Map<Integer, Room> readFromCSV(final File monthDirectory) {
        Map<Integer, Room> integerRoomHashMap = new HashMap<>();

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
                            Room room = getObjFromCVS(line);
                            integerRoomHashMap.put(room.getId(), room);
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
    public Integer getIdOfLastObjInMap(final Map<Integer, Room> room) {
        return room.values().stream()
                .mapToInt(Room::getId)
                .max()
                .orElse(0);
    }
}
