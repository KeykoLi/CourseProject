package ua.lviv.iot.course.spring.project.protection_system.csv_record;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.course.spring.project.protection_system.model.Notification;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVWriterTest{
     String wrongPath = "src/test/exp_record/wrong.csv";
     String correctPath = "src/test/exp_record/file1.csv";
     String toEmpty = "src/test/exp_record/file2.csv";;

    CSVWriter csvWriter;
    Notification notification;

    @BeforeEach
    void setUp() {
        csvWriter = new CSVWriter();
        notification = new Notification();

    }

    @Test
    void checkFileEx() {
        boolean result = csvWriter.checkFileEx(correctPath);

        assertTrue(result);
    }

    @Test
    void checkFileNoEx() {
        boolean result = csvWriter.checkFileEx(wrongPath);

        assertFalse(result);
    }

    @Test
    void getMonthDiPath() {
        String path = csvWriter.getMonthDirectoryPath(notification);

        assertNotNull(path);
        assertTrue(path.endsWith(String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1)));
    }

    @Test
    void getFilePerCurrentMonthsFromExistingDir() {
        File[] files = csvWriter.getFileOfThisEntityFromCurrentMonth(notification);

        assertNotNull(files);
        assertTrue(files.length >= 1);
    }

    @Test
    void readLinesFromExFile() throws IOException {
        File file = new File(correctPath);

        List<String> lines = csvWriter.readStrFromFile(file);

        assertEquals(1, lines.size());
    }

    @Test
    void readLinesFromNonExile() throws IOException {
        File file = new File(toEmpty);

        List<String> lines = csvWriter.readStrFromFile(file);

        assertEquals(0, lines.size());
    }

    @Test
    void returnsCorrectPath() {
        String filePath = csvWriter.getFilePath(notification);

        assertNotNull(filePath);
        assertTrue(filePath.endsWith(String.format("%s-%s.csv", notification.getClass().getSimpleName(), CSVWriter.getCurrentDate())));
    }
}