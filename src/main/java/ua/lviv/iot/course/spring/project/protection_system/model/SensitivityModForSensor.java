package ua.lviv.iot.course.spring.project.protection_system.model;

public enum SensitivityModForSensor {
    HIGH, //— датчик не реагує на кота (зріст до 25 см).
    MEDIUM, //— не реагує на маленького собаку (зріст до 35 см).
    LOW //— не реагує на тварин зростом до 50 см.
}
