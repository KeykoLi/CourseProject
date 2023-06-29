package ua.lviv.iot.course.spring.project.protection_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Hallway  extends  EntityCSV {
    private Integer id;
    private String name;
    private int numberOfDoors;

    @JsonIgnore
    @Override
    public String getHeaders() {
        return "id, name, numberOfDoors,\n";
    }

    @Override
    public String toCSV() {
        return String.join(",", String.valueOf(id), name, String.valueOf(numberOfDoors));
    }
}
