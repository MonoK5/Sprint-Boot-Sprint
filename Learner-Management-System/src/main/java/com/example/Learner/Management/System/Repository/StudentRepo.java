package com.example.Learner.Management.System.Repository;

import com.example.Learner.Management.System.Entities.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends CrudRepository <Student, Long>{


}
