package ua.lviv.iot.course.spring.project.protection_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.course.spring.project.protection_system.csv_record.CSVWriter;
import ua.lviv.iot.course.spring.project.protection_system.model.Notification;
import ua.lviv.iot.course.spring.project.protection_system.csv_record.NotificationCSVRecord;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NotificationService implements TService<Notification> {
    private final Notification notificationObj = new Notification();

    private final NotificationCSVRecord notificationCSVRecord = new NotificationCSVRecord();
    private final CSVWriter csvWriter = new CSVWriter();
    private final File[] files = csvWriter.getFileOfThisEntityFromCurrentMonth(notificationObj);

    private Map<Integer, Notification> notificationMap;
    private AtomicInteger idCounter;

    @Autowired
    public NotificationService() {
        this.notificationMap = notificationCSVRecord.readFromCSV(new File(csvWriter.getMonthDirectoryPath(notificationObj)));
        this.idCounter = new AtomicInteger(notificationCSVRecord.getIdOfLastObjInMap(notificationMap));
    }

    @Override
    public Notification getById(final Integer id) {
        return notificationMap.get(id);
    }

    @Override
    public Collection<Notification> getAll() {
        return notificationMap.values();
    }

    @Override
    public Notification create(final Notification notification) throws IOException {
        String fileName = csvWriter.getFilePath(notification);
        notification.setId(idCounter.incrementAndGet());
        notificationMap.put(notification.getId(), notification);
        notificationCSVRecord.writeToCSV(notification, fileName);
        return notification;
    }

    @Override
    public Notification update(final Integer id, final Notification entity) throws IOException {
        if (notificationMap.containsKey(id)) {
            entity.setId(id);
            notificationCSVRecord.changeObjByID(id, entity, files);
            notificationMap.put(id, entity);
            return entity;
        }
        return null;
    }

    @Override
    public void delete(final Integer id) throws IOException {
        notificationCSVRecord.deleteFromCSV(id, files);
        notificationMap.remove(id);
    }

    @Override
    public Map<Integer, Notification> getMap() {
        return new HashMap<>(notificationMap);
    }

}
