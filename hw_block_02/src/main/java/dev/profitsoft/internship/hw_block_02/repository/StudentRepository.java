package dev.profitsoft.internship.hw_block_02.repository;

import dev.profitsoft.internship.hw_block_02.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    void deleteByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdIsNot(String username, Long id);
}