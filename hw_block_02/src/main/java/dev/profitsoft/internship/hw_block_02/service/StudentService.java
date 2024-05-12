package dev.profitsoft.internship.hw_block_02.service;

import dev.profitsoft.internship.hw_block_02.dto.StudentPage;
import dev.profitsoft.internship.hw_block_02.dto.StudentPageable;
import dev.profitsoft.internship.hw_block_02.dto.details.StudentDetailsDto;
import dev.profitsoft.internship.hw_block_02.dto.save.StudentSaveDto;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface StudentService {

    StudentDetailsDto create(StudentSaveDto studentSaveDto);

    StudentDetailsDto update(Long id, StudentSaveDto studentSaveDto);

    StudentDetailsDto findById(Long id);

    StudentPage findAll(StudentPageable studentPageable);

    byte[] getStudentReport(StudentPageable studentPageable);

    Map<String, String> upload(MultipartFile file) throws FileUploadException;

    void delete(Long id);
}
