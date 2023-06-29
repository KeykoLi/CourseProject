package ua.lviv.iot.course.spring.project.protection_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.course.spring.project.protection_system.csv_record.CSVWriter;
import ua.lviv.iot.course.spring.project.protection_system.csv_record.RoomCSVRecord;
import ua.lviv.iot.course.spring.project.protection_system.model.Room;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RoomService implements TService<Room> {
    private final Room roomObj = new Room();

    private final RoomCSVRecord roomCSVRecord = new RoomCSVRecord();
    private final CSVWriter csvWriter = new CSVWriter();
    private final File[] files = csvWriter.getFileOfThisEntityFromCurrentMonth(roomObj);
    private SensorService sensorService = new SensorService();

    private  Map<Integer, Room> roomMap;
    private AtomicInteger idCounter;

    @Autowired
    public RoomService() {
        this.roomMap = roomCSVRecord.readFromCSV(new File(csvWriter.getMonthDirectoryPath(roomObj)));
        this.idCounter = new AtomicInteger(roomCSVRecord.getIdOfLastObjInMap(roomMap));

    }

    @Override
    public Room getById(final Integer id) {
        return roomMap.get(id);
    }

    @Override
    public Collection<Room> getAll() {
        return roomMap.values();
    }

    @Override
    public Room create(final Room room) throws IOException {
        String fileName = csvWriter.getFilePath(room);
        room.setId(idCounter.incrementAndGet());
        roomMap.put(room.getId(), room);
        roomCSVRecord.writeToCSV(room, fileName);
        return room;
    }

    @Override
    public Room update(final Integer id, final Room room) throws IOException {
        if (roomMap.containsKey(id)) {
            room.setId(id);
            roomCSVRecord.changeObjByID(id, room, files);
            roomMap.put(id, room);
            return room;
        }
        return null;
    }

    @Override
    public void delete(final Integer id) throws IOException {
       var sensors = sensorService.getByIdRoom(id);
       if (sensors == null  || sensors.isEmpty()) {
           roomCSVRecord.deleteFromCSV(id, files);
           roomMap.remove(id);
           return;
       }
       var sensorIds = sensors.stream().map(sensor -> sensor.getId()).toList();
       sensorService.delete(sensorIds);
       roomCSVRecord.deleteFromCSV(id, files);
       roomMap.remove(id);
    }

    @Override
    public Map<Integer, Room> getMap() {
        return new HashMap<>(roomMap);
    }
}
