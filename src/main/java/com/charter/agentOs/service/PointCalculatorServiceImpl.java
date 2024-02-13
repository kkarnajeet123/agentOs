package com.charter.agentOs.service;

import com.charter.agentOs.model.CustomerInformation;
import com.charter.agentOs.model.Response.PointStatementTotal;
import com.charter.agentOs.model.Response.PointsStatementMonthlyResponse;
import com.charter.agentOs.model.PurchaseHistory;
import com.charter.agentOs.util.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PointCalculatorServiceImpl implements PointCalculatorService{

    @Autowired
    private DateConverter dateConverter;

    @Override
    public ResponseEntity<PointsStatementMonthlyResponse> calculateCustomerPointPerMonth(int customerId) {
        PointsStatementMonthlyResponse response = new PointsStatementMonthlyResponse();
        CustomerInformation customerInformation = customerInformation(customerId);
        if(ObjectUtils.isEmpty(customerInformation) && CollectionUtils.isEmpty(customerInformation.getPurchaseHistory())){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Date date = new Date(LocalDate.now().toString());
        long currentDateEpoc = dateConverter.epocStringToDateConverter(date);
        customerInformation.getPurchaseHistory().stream().filter(Objects::nonNull).forEach(purchaseHistory -> {

            if(dateConverter.epocStringToDateConverter(purchaseHistory.getPurchaseDate())>currentDateEpoc){
                response.setData(null);

            }else{
                calculateEachPricePoint(purchaseHistory.getPurchasePrice());
            }

        });

        PointsStatementMonthlyResponse statementResponse = new PointsStatementMonthlyResponse();
        statementResponse.setData(null);
        statementResponse.setWarnings("Entered purchase date is greater than present date");
        statementResponse.setError("Bad Request");
        return new ResponseEntity<>(statementResponse, HttpStatus.BAD_REQUEST);

        //return null;
       // return total;


    }

    private double calculateEachPricePoint(double price){
        double difference=0.0;
        double pointEarned=0.0;
        if(price>100){
            difference=price-100;
            pointEarned = 2*difference;
            return pointEarned;
        }
        if(price>50) {
            difference = price -50;
            pointEarned = 1*difference;
            return pointEarned;
        }

        return pointEarned;
    }

    @Override
    public ResponseEntity<PointStatementTotal> calculateCustomerPointTotal(int customerId) {
        CustomerInformation customerInformationForTotal = customerInformation(customerId);
        List<Double> purchasePriceList = new ArrayList<>();
        if(!ObjectUtils.isEmpty(customerInformationForTotal) && !customerInformationForTotal.getPurchaseHistory().isEmpty())

            customerInformationForTotal.getPurchaseHistory().stream().filter(Objects::nonNull).forEach(f->{
             purchasePriceList.add(f.getPurchasePrice());
            });

        double sum=0.0;
        for(Double d: purchasePriceList){
            sum+=d;
        }
        PointStatementTotal pointStatementTotal = new PointStatementTotal();
        pointStatementTotal.setData(sum);
        return new ResponseEntity<>(pointStatementTotal, HttpStatus.OK);
    }


    public CustomerInformation customerInformation(int customerId){
        //This method provides mock data for the service and have 2 customer informations with purchases history

        CustomerInformation customerInformation = new CustomerInformation();
        customerInformation.setCustomerId(customerId);
        List<PurchaseHistory> purchaseHistoryList = new ArrayList<>();
        switch(customerId){
            case 1:
                purchaseHistoryList.add(new PurchaseHistory("Iphone10", 999.00, new Date()));
                purchaseHistoryList.add(new PurchaseHistory("Iphone11", 999.00, new Date()));
                purchaseHistoryList.add(new PurchaseHistory("Iphone13", 999.00, new Date()));
                customerInformation.setPurchaseHistory(purchaseHistoryList);
                break;
            case 2:
                purchaseHistoryList.add(new PurchaseHistory("SamsungGalaxy-S24", 999.00, new Date()));
                purchaseHistoryList.add(new PurchaseHistory("SamsungGalaxy-S24-Ultra", 1099.00, new Date()));
                purchaseHistoryList.add(new PurchaseHistory("Iphone13Plus", 999.00, new Date()));
                customerInformation.setPurchaseHistory(purchaseHistoryList);
                break;
            default:
                purchaseHistoryList.add(new PurchaseHistory("SamsungGalaxy-S", 999.00, new Date()));
                purchaseHistoryList.add(new PurchaseHistory("Iphone15", 1099.00, new Date()));
                purchaseHistoryList.add(new PurchaseHistory("Iphone14Plus", 999.00, new Date()));
                customerInformation.setPurchaseHistory(purchaseHistoryList);
        }
        customerInformation.setPurchaseHistory(purchaseHistoryList);
        return customerInformation;
    }
}
