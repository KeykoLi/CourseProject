package ua.lviv.iot.course.spring.project.protection_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.course.spring.project.protection_system.csv_record.CSVWriter;
import ua.lviv.iot.course.spring.project.protection_system.csv_record.SensorCSVRecord;
import ua.lviv.iot.course.spring.project.protection_system.model.Sensor;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SensorService implements TService<Sensor> {
    private final Sensor sensorObj = new Sensor();

    private final SensorCSVRecord sensorCSVRecord = new SensorCSVRecord();
    private final CSVWriter csvWriter = new CSVWriter();
    private final File[] files = csvWriter.getFileOfThisEntityFromCurrentMonth(sensorObj);

    private  Map<Integer, Sensor> sensorMap;
    private AtomicInteger idCounter;

    @Autowired
    public SensorService() {
        this.sensorMap = sensorCSVRecord.readFromCSV(new File(csvWriter.getMonthDirectoryPath(sensorObj)));
        this.idCounter = new AtomicInteger(sensorCSVRecord.getIdOfLastObjInMap(sensorMap));

    }

    @Override
    public Sensor getById(final Integer id) {
        return sensorMap.get(id);
    }

    @Override
    public Collection<Sensor> getAll() {
        return sensorMap.values();
    }

    public Collection<Sensor> getByIdRoom(final Integer idRoom) {
        return sensorMap.values()
                .stream()
                .filter(sensor -> sensor.getIdRoom().equals(idRoom))
                .toList();
    }

    public Collection<Sensor> getByIdHallway(final Integer idHallway) {
        return sensorMap.values()
                .stream()
                .filter(sensor -> sensor.getIdHallway().equals(idHallway))
                .toList();

    }

    @Override
    public Sensor create(final Sensor entity) throws IOException {
        String fileName = csvWriter.getFilePath(entity);
        entity.setId(idCounter.incrementAndGet());
        sensorMap.put(entity.getId(), entity);
        sensorCSVRecord.writeToCSV(entity, fileName);
        return entity;
    }

    @Override
    public Sensor update(final Integer key, final Sensor entity) throws IOException {
        if (sensorMap.containsKey(key)) {
            entity.setId(key);
            sensorCSVRecord.changeObjByID(key, entity, files);
            sensorMap.put(key, entity);
            return entity;
        }
        return null;
    }

    @Override
    public void delete(final Integer key) throws IOException {
        sensorCSVRecord.deleteFromCSV(key, files);
        sensorMap.remove(key);
    }

    public void delete(final Collection<Integer> ids) throws IOException {
        if (ids == null) {
            return;
        }
        for (Integer id: ids) {
            delete(id);
        }
    }

    @Override
    public Map<Integer, Sensor> getMap() {
        return new HashMap<>(sensorMap);
    }
}

