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
public class Sensor extends EntityCSV {
    private Integer id;
    private Integer idRoom;
    private Integer idHallway;
    private SensorType type;
    private boolean statusActive;
    private int batteryPower;
    private SensitivityModForSensor sensitivity;
    private Notification notification;

    @JsonIgnore
    @Override
    public String getHeaders() {
        return "id, idRoom, idHallway, type, statusActive, "
                + "batteryPower, sensitivity, notification \n";
    }
    @Override
    public String toCSV() {
        return String.valueOf(id) + "," + String.valueOf(idRoom) + "," + String.valueOf(idHallway)
                + "," + String.valueOf(type) + "," + String.valueOf(statusActive) + ","
                + String.valueOf(batteryPower) + "," + String.valueOf(sensitivity) + ","
                + String.valueOf(notification.getId());
    }
}
