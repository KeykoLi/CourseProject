package ua.lviv.iot.course.spring.project.protection_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.course.spring.project.protection_system.csv_record.CSVWriter;
import ua.lviv.iot.course.spring.project.protection_system.csv_record.HallwayCSVRecord;
import ua.lviv.iot.course.spring.project.protection_system.model.Hallway;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
@Service
public class HallwayService implements TService<Hallway> {
    private final Hallway hallwayObj = new Hallway();

    private final HallwayCSVRecord hallwayCSVRecord = new HallwayCSVRecord();
    private final CSVWriter csvWriter = new CSVWriter();
    private final File[] files = csvWriter.getFileOfThisEntityFromCurrentMonth(hallwayObj);
    private final SensorService sensorService = new SensorService();
    private  Map<Integer, Hallway> hallwayMap;
    private AtomicInteger idCounter;

    @Autowired
    public HallwayService() {
        this.hallwayMap = hallwayCSVRecord.readFromCSV(new File(csvWriter.getMonthDirectoryPath(hallwayObj)));
        this.idCounter = new AtomicInteger(hallwayCSVRecord.getIdOfLastObjInMap(hallwayMap));

    }

    @Override
    public Hallway getById(final Integer id) {
        return hallwayMap.get(id);
    }

    @Override
    public Collection<Hallway> getAll() {
        return hallwayMap.values();
    }

    @Override
    public Hallway create(final Hallway hallway) throws IOException {
        String fileName = csvWriter.getFilePath(hallway);
        hallway.setId(idCounter.incrementAndGet());
        hallwayMap.put(hallway.getId(), hallway);
        hallwayCSVRecord.writeToCSV(hallway, fileName);
        return hallway;
    }

    @Override
    public Hallway update(final Integer id, final Hallway hallway) throws IOException {
        if (hallwayMap.containsKey(id)) {
            hallway.setId(id);
            hallwayCSVRecord.changeObjByID(id, hallway, files);
            hallwayMap.put(id, hallway);
            return hallway;
        }
        return null;
    }

    @Override
    public void delete(final Integer id) throws IOException {
        var sensors = sensorService.getByIdHallway(id);
        if (sensors == null  || sensors.isEmpty()) {
            hallwayCSVRecord.deleteFromCSV(id, files);
            hallwayMap.remove(id);
            return;
        }
        var sensorIds = sensors.stream().map(sensor -> sensor.getId()).toList();
        sensorService.delete(sensorIds);
        hallwayCSVRecord.deleteFromCSV(id, files);
        hallwayMap.remove(id);
    }

    @Override
    public Map<Integer, Hallway> getMap() {
        return new HashMap<>(hallwayMap);
    }
}
