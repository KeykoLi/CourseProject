package ua.lviv.iot.course.spring.project.protection_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Room extends EntityCSV {
    private Integer id;
    private String name;
    private int numberOfWindows;

    @JsonIgnore
    @Override
    public String getHeaders() {
        return "id, name numberOfWindows,\n";
    }

    @Override
    public String toCSV() {
        return String.join(",", String.valueOf(id), name,
                String.valueOf(numberOfWindows));
    }

}
