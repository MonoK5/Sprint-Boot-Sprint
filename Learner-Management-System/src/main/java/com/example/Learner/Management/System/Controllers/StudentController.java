//package Controllers;
//
//public class StudentController {
//}
//package com.example.Learner.Management.System.controller;

package com.example.Learner.Management.System.Controllers;
import com.example.Learner.Management.System.Entities.Student;
import com.example.Learner.Management.System.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {

        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {

        return studentService.getStudentById(id);


    }

    @GetMapping("/name/{name}")
    public List<Student> getStudentsByName(@PathVariable String name) {
        return studentService.getStudentsByName(name);
    }

    @GetMapping("/score-above/{score}")
    public List<Student> getStudentsWithScoreAbove(@PathVariable int score) {
        return studentService.getStudentsWithScoreGreaterThan(score);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping ("/average")
    public double calculateAverage () {
        return studentService.calculateAvg();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        Student existingStudent = studentService.getStudentById(id);
        if (existingStudent == null) {
            return ResponseEntity.notFound().build();  // 404 if student doesn't exist
        }

        // Set ID explicitly to avoid mismatch or missing ID in request body
        student.setId(id);

        studentService.updateStudent(student);
        return ResponseEntity.ok(student);
    }

}
