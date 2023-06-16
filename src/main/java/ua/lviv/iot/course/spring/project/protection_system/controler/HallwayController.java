package ua.lviv.iot.course.spring.project.protection_system.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.course.spring.project.protection_system.model.Hallway;
import ua.lviv.iot.course.spring.project.protection_system.service.HallwayService;

import java.io.IOException;
import java.util.Collection;
@RestController
@RequestMapping(path = "/hallways")
public class HallwayController implements TController<Hallway> {
    private final HallwayService hallwayService;

    @Autowired
    public HallwayController(final HallwayService hallwayService) {
        this.hallwayService = hallwayService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Hallway> getById(@PathVariable final Integer id) {
        Hallway hallway = hallwayService.getById(id);
        if (hallway != null) {
            return ResponseEntity.ok(hallway);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<Collection<Hallway>> getAll() {
        Collection<Hallway> hallways = hallwayService.getAll();
        if (hallways.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(hallways);
    }

    @Override
    @PostMapping
    public ResponseEntity<Hallway> create(@RequestBody final Hallway hallway) throws IOException {
        Hallway createdRoom = hallwayService.create(hallway);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }


    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Hallway> update(@PathVariable final Integer id,
                                       @RequestBody final Hallway hallway) throws IOException {
        if (!hallwayService.getMap().containsKey(id)) {
            return ResponseEntity.noContent().build();
        }
        Hallway hallwayC = hallwayService.update(id, hallway);
        return ResponseEntity.ok(hallwayC);
    }


    @Override
    @DeleteMapping
    public ResponseEntity<Hallway> delete(@PathVariable final Integer id) throws IOException {
        if (!hallwayService.getMap().containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        hallwayService.delete(id);
        return ResponseEntity.ok().build();
    }
}
