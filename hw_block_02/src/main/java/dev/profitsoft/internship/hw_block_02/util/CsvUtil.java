package dev.profitsoft.internship.hw_block_02.util;

import dev.profitsoft.internship.hw_block_02.dto.info.StudentInfoDto;
import lombok.experimental.UtilityClass;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@UtilityClass
public class CsvUtil {

    /**
     * Transfer information about students to csv format
     *
     * @param students list with students
     * @return a CSV representation of the students
     */
    public byte[] generateReport(List<StudentInfoDto> students) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outputStream), CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("username", "firstName", "lastName");
            for (StudentInfoDto student : students) {
                csvPrinter.printRecord(student.getUsername(), student.getFirstName(), student.getLastName());
            }
            csvPrinter.flush();

            return outputStream.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
