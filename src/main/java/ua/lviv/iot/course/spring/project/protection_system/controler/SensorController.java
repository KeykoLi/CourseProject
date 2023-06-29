package ua.lviv.iot.course.spring.project.protection_system.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.course.spring.project.protection_system.model.Notification;
import ua.lviv.iot.course.spring.project.protection_system.model.Sensor;
import ua.lviv.iot.course.spring.project.protection_system.service.NotificationService;
import ua.lviv.iot.course.spring.project.protection_system.service.SensorService;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping(path = "/sensors")
public class SensorController implements TController<Sensor> {
    private final SensorService sensorService;
    private final NotificationService notificationService;

    @Autowired
    public SensorController(final SensorService sensorService, final NotificationService notificationService) {
        this.sensorService = sensorService;
        this.notificationService = notificationService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getById(@PathVariable final Integer id) {
        Sensor service = sensorService.getById(id);
        if (service != null) {
            return ResponseEntity.ok(service);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<Collection<Sensor>> getAll() {
        Collection<Sensor> sensors = sensorService.getAll();
        if (sensors.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sensors);
    }

    private Notification createNotificationFromRequest(final Notification requestNotification) throws IOException {
        return notificationService.create(requestNotification);
    }

    @Override
    @PostMapping
    public ResponseEntity<Sensor> create(@RequestBody final Sensor sensor) throws IOException {
        Notification notification = createNotificationFromRequest(sensor.getNotification());
        sensor.setNotification(notification);
        Sensor sensorS = sensorService.create(sensor);
        return ResponseEntity.status(HttpStatus.CREATED).body(sensorS);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<Sensor> update(@PathVariable final Integer id,
                                               @RequestBody final Sensor sensor) throws IOException {
        if (!sensorService.getMap().containsKey(id)) {
            return ResponseEntity.noContent().build();
        }
        Sensor sensorS = sensorService.update(id, sensor);
        return ResponseEntity.ok(sensorS);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Sensor> delete(@PathVariable final Integer id) throws IOException {
        if (!sensorService.getMap().containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        sensorService.delete(id);
        return ResponseEntity.ok().build();
    }
}
