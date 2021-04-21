package com.moses.easysoftwarespringbootapp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity()
@TableGenerator(name = "tab", initialValue = 0, allocationSize = 50)
//@JsonIgnoreProperties(value = { "id" })
@Table(name = "loan_request")
public class LoanRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tab")
	private Long id;
	@Column(name = "transactionid", nullable = false)
	private String transactionId;
	@Column(name = "amount", nullable = false)
	private Double amount;
	@Column(name = "interest", nullable = false)
	private Double interest;
	// should be a foreign key to loans table, not implementing it now for brevity
	@Column(name = "loan_code", nullable = false)
	private Integer loanCode;
	@Column(name = "loan_date", nullable = false)
	private Date loanDate;
	@Column(name = "loandue_date", nullable = false)
	private Date loanDueDate;
	
	// This should be a foreign key customer table, not implementing it now for brevity
	private String customerId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getInterest() {
		return interest;
	}

	public void setInterest(Double interest) {
		this.interest = interest;
	}

	public Integer getLoanCode() {
		return loanCode;
	}

	public void setLoanCode(Integer loanCode) {
		this.loanCode = loanCode;
	}

	public Date getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}

	public Date getLoanDueDate() {
		return loanDueDate;
	}

	public void setLoanDueDate(Date loanDueDate) {
		this.loanDueDate = loanDueDate;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return "LoanRequest [id=" + id + ", transactionId=" + transactionId + ", amount=" + amount + ", interest="
				+ interest + ", loanCode=" + loanCode + ", loanDate=" + loanDate + ", loanDueDate=" + loanDueDate
				+ ", customerId=" + customerId + "]";
	}
	
	
	
	

}
