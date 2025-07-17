package com.prashant.junit.testing.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prashant.junit.testing.model.TeacherDetails;

import java.util.Optional;


@Repository
public interface TeacherRepo extends JpaRepository<TeacherDetails, Integer> {

	boolean existsByTeacherEmail(String teacherEmail);
	
	Optional<TeacherDetails> findByTeacherEmail(String teacherEmail);
	
	Page<TeacherDetails> findAll(Pageable pageable);
	
	

}
