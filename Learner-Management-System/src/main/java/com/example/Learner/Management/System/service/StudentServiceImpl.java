package com.example.Learner.Management.System.service;

import com.example.Learner.Management.System.Entities.Student;
import com.example.Learner.Management.System.Repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{

    private final StudentRepo repo;

    @Autowired
    public StudentServiceImpl(StudentRepo studentRepo){
        this.repo = studentRepo;
    }

    @Override
    public Student saveStudent(Student student) {
        return repo.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return (List<Student>) repo.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return  repo.findById(id).orElse(null);
    }

    @Override
    public List<Student> getStudentsWithScoreGreaterThan(int score) {
        return repo.findByScoreGreaterThan(score);
    }

    @Override
    public void deleteStudent(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Student> getStudentsByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public double calculateAvg() {

        List<Student> students = (List<Student>) repo.findAll();

        if (students.isEmpty()) {
            return 0.0;
        }

        return students.stream()
                .mapToDouble(Student::getScore)
                .average()
                .orElse(0.0);
    }

   @Override
    public void updateStudent(Student updatedStudent) {
        Optional<Student> optional = repo.findById(updatedStudent.getId());
        if (optional.isPresent()) {
            Student student = optional.get();

            student.setName(updatedStudent.getName());
            student.setScore(updatedStudent.getScore());

            repo.save(student);
        } else {
            System.out.println("Student not found.");
        }
    }


}
