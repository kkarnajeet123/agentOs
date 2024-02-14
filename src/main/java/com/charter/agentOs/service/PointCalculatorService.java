package com.charter.agentOs.service;

import com.charter.agentOs.model.Response.PointStatementTotal;
import com.charter.agentOs.model.Response.PointsStatementMonthlyResponse;
import org.springframework.http.ResponseEntity;

public interface PointCalculatorService {

    ResponseEntity<PointsStatementMonthlyResponse> calculateCustomerPointPerMonth(int customerId, String statementDate);
    ResponseEntity<PointsStatementMonthlyResponse> calculateAllCustomerPointPerMonth(String statementDate);
}
