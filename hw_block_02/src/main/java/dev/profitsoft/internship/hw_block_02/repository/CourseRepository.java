package dev.profitsoft.internship.hw_block_02.repository;

import dev.profitsoft.internship.hw_block_02.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    void deleteByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, Long id);
}