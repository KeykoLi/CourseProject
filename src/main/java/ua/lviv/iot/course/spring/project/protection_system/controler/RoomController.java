package ua.lviv.iot.course.spring.project.protection_system.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.course.spring.project.protection_system.model.Room;
import ua.lviv.iot.course.spring.project.protection_system.service.RoomService;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/rooms")
public class RoomController implements TController<Room> {
    private final RoomService roomService;

    @Autowired
    public RoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Room> getById(@PathVariable final Integer id) {
        Room room = roomService.getById(id);
        if (room != null) {
            return ResponseEntity.ok(room);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<Collection<Room>> getAll() {
        Collection<Room> rooms = roomService.getAll();
        if (rooms.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rooms);
    }

    @Override
    @PostMapping
    public ResponseEntity<Room> create(@RequestBody final Room room) throws IOException {
        Room createdRoom = roomService.create(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Room> update(@PathVariable final Integer id,
                                       @RequestBody final Room room) throws IOException {
        if (!roomService.getMap().containsKey(id)) {
            return ResponseEntity.noContent().build();
        }
        Room roomS = roomService.update(id, room);
        return ResponseEntity.ok(roomS);
    }


    @Override
    @DeleteMapping
    public ResponseEntity<Room> delete(@PathVariable final Integer id) throws IOException {
        if (!roomService.getMap().containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        roomService.delete(id);
        return ResponseEntity.ok().build();
    }
}
