package com.example.Learner.Management.System.service;

import com.example.Learner.Management.System.Entities.Student;

import java.util.List;

public interface StudentService {
    Student saveStudent(Student student);

    List<Student> getAllStudents();

    Student getStudentById(Long id);

    List<Student> getStudentsWithScoreGreaterThan(int score);

    void deleteStudent(Long id);

    List<Student> getStudentsByName(String name);

     double calculat-eAvg();

     void updateStudent(Student student);
}
