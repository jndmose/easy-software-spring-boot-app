package com.moses.easysoftwarespringbootapp;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoanRepository extends JpaRepository<LoanRequest, Long> {

	// Consider more better ways like paging and stuff
	@Query("select lr from  LoanRequest  lr where lr.customerId = :customer_id")
	Optional<List<LoanRequest>> findAllByCustomerId(@Param("customer_id") String customerId);

}
