package ua.lviv.iot.course.spring.project.protection_system.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.lviv.iot.course.spring.project.protection_system.model.Notification;
import ua.lviv.iot.course.spring.project.protection_system.service.NotificationService;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/notifications")
public class NotificationController implements TController<Notification> {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getById(@PathVariable final Integer id) {
        Notification notification = notificationService.getById(id);
        if (notification != null) {
            return ResponseEntity.ok(notification);
        }
        return ResponseEntity.notFound().build();
    }


    @Override
    @GetMapping
    public ResponseEntity<Collection<Notification>> getAll() {
        Collection<Notification> notifications = notificationService.getAll();
        if (notifications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notifications);
    }

    @Override
    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody final Notification entity) throws IOException {
        Notification notification = notificationService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<Notification> update(@PathVariable final Integer id,
                                               @RequestBody final Notification entity) throws IOException {
        if (!notificationService.getMap().containsKey(id)) {
            return ResponseEntity.noContent().build();
        }
        Notification notification = notificationService.update(id, entity);
        return ResponseEntity.ok(notification);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Notification> delete(@PathVariable final Integer id) throws IOException {
        if (!notificationService.getMap().containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        notificationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
