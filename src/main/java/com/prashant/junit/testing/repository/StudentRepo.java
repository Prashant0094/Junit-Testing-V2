package com.prashant.junit.testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prashant.junit.testing.model.StudentDetails;

@Repository
public interface StudentRepo extends JpaRepository<StudentDetails, Integer>{

}
