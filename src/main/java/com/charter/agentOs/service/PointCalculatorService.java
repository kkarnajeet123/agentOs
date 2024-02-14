package com.charter.agentOs.service;

import com.charter.agentOs.model.Response.PointStatementTotal;
import com.charter.agentOs.model.Response.PointsStatementMonthlyResponse;
import com.charter.agentOs.model.Response.TotalPointsStatementForEachCustomer;
import org.springframework.http.ResponseEntity;

import java.time.Month;
import java.util.Map;

public interface PointCalculatorService {

    ResponseEntity<PointsStatementMonthlyResponse> calculateCustomerPointPerMonth(int customerId, String statementDate);
    ResponseEntity<PointsStatementMonthlyResponse> calculateAllCustomerPointPerMonth(String statementDate);
    ResponseEntity<TotalPointsStatementForEachCustomer> calculateEachCustomerPoints(Map<Long, Map<Month, Integer>> customerPointMap );
    ResponseEntity<PointStatementTotal> customerPointMap(Map<Long, Map<Month, Integer>> customerPointMap);
}
