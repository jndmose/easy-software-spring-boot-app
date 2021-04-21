package com.moses.easysoftwarespringbootapp;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.moses.easysoftwarespringbootapp.exception.LoanNotFoundException;

@RestController
public class LoanRequestController {
	@Autowired
	private LoanRepository loanRepo;
	private ObjectMapper objectMapper = new ObjectMapper();

	/*
	 * Post request with localhost:8090/loanRequest sample with { "transactionId":
	 * "MERT443456465", "amount" : "6000", "interest" : "750", "loanCode" : "123",
	 * "loanDate" : "2021-04-29", "loanDueDate" : "2018-05-23", "customerId" :
	 * "EP-0001" }
	 * 
	 */

	// Create a REST endpoint to receive loans request.

	@PostMapping("/loanRequest")
	public ResponseEntity<LoanRequest> restRequest(@RequestBody @Valid LoanRequest loanRequest) {

		return new ResponseEntity<>(loanRepo.save(loanRequest), HttpStatus.OK);
	}

	// List all loans where customer Id is EP-0001,

	@GetMapping(value = "/loanInfo/{customerId}", produces = "application/json")
	public ResponseEntity<List<LoanRequest>> loanDetails(@PathVariable(value = "customerId") String customerId) {

		Optional<List<LoanRequest>> loanRequest = loanRepo.findAllByCustomerId(customerId);
		// Gson gson = new Gson();
		// String responseJson = gson.toJson(loanRequest);

		if (loanRequest.isPresent()) {
			return new ResponseEntity<>(loanRequest.get(), HttpStatus.OK);
		}

		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(value = "loanInfoData/{id}", produces = "application/json")
	public ResponseEntity<LoanRequest> loanDetailsById(@PathVariable(value = "id") Long id) {

		Optional<LoanRequest> loanRequest = loanRepo.findById(id);

		if (loanRequest.isPresent()) {
			return new ResponseEntity<>(loanRequest.get(), HttpStatus.OK);
		}

		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	// create an API endpoint to implement loan patch

	@PatchMapping(path = "loanPatch/{id}", consumes = "application/json-patch+json")
	public ResponseEntity<LoanRequest> updateLoan(@PathVariable Long id, @RequestBody JsonPatch patch) {
		try {
			LoanRequest loanRequest = loanRepo.findById(id).orElseThrow(LoanNotFoundException::new);
			LoanRequest loanPatched = applyPatchToCustomer(patch, loanRequest);
			loanRepo.save(loanPatched);

			return ResponseEntity.ok(loanPatched);
		} catch (LoanNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (JsonPatchException | JsonProcessingException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private LoanRequest applyPatchToCustomer(JsonPatch patch, LoanRequest targetLoan)
			throws JsonPatchException, JsonProcessingException {
		JsonNode patched = patch.apply(objectMapper.convertValue(targetLoan, JsonNode.class));
		return objectMapper.treeToValue(patched, LoanRequest.class);
	}

	// create an API endpoint to implement loan put

	@RequestMapping(method = RequestMethod.PUT, value = "loanPut/{id}")
	public ResponseEntity<LoanRequest> updateLoan(@RequestBody LoanRequest loanRequest, @PathVariable long id) {
		Optional<LoanRequest> loan = loanRepo.findById(id);
		if (loan.isPresent()) {
			LoanRequest ourLoan = loan.get();
			ourLoan.setAmount(loanRequest.getAmount());
			ourLoan.setCustomerId(loanRequest.getCustomerId());
			ourLoan.setInterest(loanRequest.getInterest());
			ourLoan.setLoanCode(loanRequest.getLoanCode());
			ourLoan.setLoanDate(loanRequest.getLoanDate());
			ourLoan.setLoanDueDate(loanRequest.getLoanDueDate());
			ourLoan.setTransactionId(loanRequest.getTransactionId());
			if (loanRepo.save(ourLoan).getId().equals(id)) {
				return new ResponseEntity<>(ourLoan, HttpStatus.OK);
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}
