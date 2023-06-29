package ua.lviv.iot.course.spring.project.protection_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Notification extends EntityCSV {
    private Integer id;
    private boolean enabled = false;
    private String message;

    @JsonIgnore
    @Override
    public String getHeaders() {
        return "id, enabled, message\n";
    }

    @Override
    public String toCSV() {
        return  String.valueOf(id) + "," + String.valueOf(enabled) + "," +  message;
    }
}
