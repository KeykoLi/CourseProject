package ua.lviv.iot.course.spring.project.protection_system.csv_record;

import java.io.*;
import java.util.Map;

public interface TCSVRecord<T> {

    void writeToCSV(T entity, String pathToFiles) throws IOException;

    void deleteFromCSV(Integer id, File[] files) throws IOException;

    void changeObjByID(Integer id, T entity, File[] files) throws IOException;

    Map<Integer, T> readFromCSV(File dir);

    Integer getIdOfLastObjInMap(Map<Integer, T> entities);

    T getObjFromCVS(String csvLine);
}
