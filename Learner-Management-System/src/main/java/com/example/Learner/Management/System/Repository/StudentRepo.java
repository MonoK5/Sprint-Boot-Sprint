package com.example.Learner.Management.System.repository;

import com.example.Learner.Management.System.Entities.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentRepo extends CrudRepository <Student, Long>{
    List<Student> findByName(String name);
    List<Student> findByScoreGreaterThan(int score);
    List<Student> findByGrade(int grade);

}
