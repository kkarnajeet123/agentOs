package com.charter.agentOs.controller;

import com.charter.agentOs.model.Response.PointStatementTotal;
import com.charter.agentOs.model.Response.PointsStatementMonthlyResponse;
import com.charter.agentOs.service.PointCalculatorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/purchaseHistory")
public class PurchaseController {
    @Autowired
    private PointCalculatorServiceImpl pointCalculatorService;
    @GetMapping("/{customerId}/total")
    public ResponseEntity<?> getPointTotal(@PathVariable String customerId,
                                           @RequestParam(required = false) String date){

        return pointCalculatorService.calculateCustomerPointPerMonth(Integer.parseInt(customerId),date);

    }

    @GetMapping("/customer/{customerId}/totalPoint")
    public ResponseEntity<PointStatementTotal> getTotalPointOfCustomer(@PathVariable String customerId, @RequestParam (required = false) String date){
        ResponseEntity<PointsStatementMonthlyResponse> initialResponse= pointCalculatorService.calculateCustomerPointPerMonth(Integer.parseInt(customerId),date);
        Map<Long, Map<Month, Integer>> customerPointMap= initialResponse.getBody().getData();
       return pointCalculatorService.customerPointMap(customerPointMap);
    }

    @GetMapping("/customer/totalPoint")
    public ResponseEntity<?> getTotalPointForAllCustomer(@RequestParam(required = false) String date){
        ResponseEntity<PointsStatementMonthlyResponse> initialResponse=  pointCalculatorService.calculateAllCustomerPointPerMonth(date);
        Map<Long,Map<Month, Integer>> customerPointMap = initialResponse.getBody().getData();
      return  pointCalculatorService.calculateEachCustomerPoints(customerPointMap);
    }

    @GetMapping("/allCustomer/total")
    public ResponseEntity<?> getPointTotalForAll(
            @RequestParam(required = false) String date){

        return pointCalculatorService.calculateAllCustomerPointPerMonth(date);

    }

}
