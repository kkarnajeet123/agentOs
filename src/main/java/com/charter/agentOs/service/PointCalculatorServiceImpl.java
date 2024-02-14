package com.charter.agentOs.service;

import com.charter.agentOs.model.CustomerInformation;
import com.charter.agentOs.model.Response.PointStatementTotal;
import com.charter.agentOs.model.Response.PointsStatementMonthlyResponse;
import com.charter.agentOs.model.PurchaseHistory;
import com.charter.agentOs.model.Response.TotalPointsStatementForEachCustomer;
import com.charter.agentOs.util.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Service
public class PointCalculatorServiceImpl implements PointCalculatorService {

    @Autowired
    private DateConverter dateConverter;

    @Override
    public ResponseEntity<PointsStatementMonthlyResponse> calculateCustomerPointPerMonth(int customerId, String statementDate) {
        if (StringUtils.isEmpty(statementDate)) {
            statementDate = LocalDate.now()
                    .minusMonths(3)
                    .minusDays(LocalDate.now().getDayOfMonth()).toString();
        }
        PointsStatementMonthlyResponse response = new PointsStatementMonthlyResponse();
        CustomerInformation customerInformation = customerInformation(customerId);
        if (ObjectUtils.isEmpty(customerInformation) && CollectionUtils.isEmpty(customerInformation.getPurchaseHistory())) {
            response.setError("No Customer Found");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = LocalDate.parse(statementDate);
        if (currentDate.isBefore(startDate)) {
            response.setError("Statement Date is greater than the current date. Please provide earlier date");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.setData(getPointsForCustomer(customerInformation, startDate));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Map<Long, Map<Month, Integer>> getPointsForCustomer(CustomerInformation customerInformation, LocalDate startDate) {
        Map<Month, Integer> responseData = new LinkedHashMap<>();
        Map<Long, Map<Month, Integer>> response = new HashMap<>();
        sortByDate(customerInformation.getPurchaseHistory());
        customerInformation.getPurchaseHistory().stream().forEachOrdered(purchaseHistory -> {
            LocalDate purchasedDate = purchaseHistory.getPurchaseDate();
            if (purchasedDate.isAfter(startDate) &&
                    startDate.minusMonths(-3).isAfter(purchasedDate)) {
                Month key = purchasedDate.getMonth();
                if (responseData.containsKey(key)) {
                    responseData.put(key,
                            responseData.get(key) +
                                    calculateEachPricePoint(purchaseHistory.getPurchasePrice()));
                } else responseData.put(key
                        , calculateEachPricePoint(purchaseHistory.getPurchasePrice()));
            }
            response.put(customerInformation.getCustomerId(), responseData);
        });
        return response;
    }


    private void sortByDate(List<PurchaseHistory> purchaseHistoryList) {
        Comparator<PurchaseHistory> comparator = new Comparator<PurchaseHistory>() {
            @Override
            public int compare(PurchaseHistory o1, PurchaseHistory o2) {
                return o1.getPurchaseDate().compareTo(o2.getPurchaseDate());
            }
        };
        purchaseHistoryList.sort(comparator);
    }

    private Integer calculateEachPricePoint(double price) {
        int difference = 0;
        int pointEarned = 0;
        int priceRounded = Double.valueOf(price).intValue();
        if (price > 100) {
            difference = priceRounded - 100;
            pointEarned = 2 * difference;
        }
        if (price > 50) {
            difference = priceRounded - 50;
            pointEarned += 1 * difference;
        }

        return pointEarned;
    }

    @Override
    public ResponseEntity<PointsStatementMonthlyResponse> calculateAllCustomerPointPerMonth(String statementDate) {
        if (StringUtils.isEmpty(statementDate)) {
            statementDate = LocalDate.now()
                    .minusMonths(3)
                    .minusDays(LocalDate.now().getDayOfMonth()).toString();
        }
        PointsStatementMonthlyResponse response = new PointsStatementMonthlyResponse();
        List<CustomerInformation> customerInformationList = getCustomers();
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = LocalDate.parse(statementDate.toString());
        if (currentDate.isBefore(startDate)) {
            response.setError("Statement Date is greater than the current date. Please provide earlier date");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Map<Long, Map<Month, Integer>> responseData = new HashMap<>();
        customerInformationList.forEach(customerInformation -> {
            responseData.putAll(getPointsForCustomer(customerInformation, startDate));
        });

        response.setData(responseData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<PointStatementTotal> customerPointMap(Map<Long, Map<Month, Integer>> customerPointMap) {
        PointStatementTotal response = new PointStatementTotal();
        double sum = 0.0;
        List<Double> pointList = new ArrayList<>();
        customerPointMap.entrySet().stream().filter(Objects::nonNull).forEach(m -> {
            m.getValue().forEach((k,v)->{
               pointList.add(Double.valueOf(v));
            });
        });
        response.setData(getTotalPointValue(pointList));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    public ResponseEntity<TotalPointsStatementForEachCustomer> calculateEachCustomerPoints(Map<Long,Map<Month, Integer>> customerPointMap ) {
        TotalPointsStatementForEachCustomer response = new TotalPointsStatementForEachCustomer();
        Map<Long,Double> eachCustomerTotalPoints = new Hashtable<>();
        double sum = 0.0;
        List<Double> pointList = new ArrayList<>();

        customerPointMap.entrySet().stream().filter(Objects::nonNull).forEach(m -> {
            m.getValue().forEach((k,v)->{

                pointList.add(Double.valueOf(v));
            });
            eachCustomerTotalPoints.put(m.getKey(),getTotalPointValue(pointList));
        });
        response.setData(eachCustomerTotalPoints);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Double getTotalPointValue(List<Double> pointList){
        double sum=0.0;
        for(Double d: pointList){

            sum+=d;
        }
        return sum;
    }


    private List<CustomerInformation> getCustomers() {
        List<CustomerInformation> customerInformationList = new ArrayList<>();
        customerInformationList.add(customerInformation(1));
        customerInformationList.add(customerInformation(2));
        customerInformationList.add(customerInformation(0));
        return customerInformationList;
    }

    public CustomerInformation customerInformation(int customerId) {
        //This method provides mock data for the service and have 2 customer informations with purchases history

        CustomerInformation customerInformation = new CustomerInformation();
        customerInformation.setCustomerId(customerId);
        List<PurchaseHistory> purchaseHistoryList = new ArrayList<>();
        switch (customerId) {
            case 1:
                purchaseHistoryList.add(new PurchaseHistory("Iphone10", 999.00, LocalDate.now().minusMonths(3)));
                purchaseHistoryList.add(new PurchaseHistory("Iphone11", 99.00, LocalDate.now().minusMonths(1)));
                purchaseHistoryList.add(new PurchaseHistory("Iphone13", 1099.00, LocalDate.now()));
                purchaseHistoryList.add(new PurchaseHistory("Iphone13", 1099.00, LocalDate.now().minusMonths(2)));
                customerInformation.setPurchaseHistory(purchaseHistoryList);
                break;
            case 2:
                purchaseHistoryList.add(new PurchaseHistory("SamsungGalaxy-S24", 999.00, LocalDate.now().minusMonths(2)));
                purchaseHistoryList.add(new PurchaseHistory("SamsungGalaxy-S24-Ultra", 1099.00, LocalDate.now().minusMonths(5)));
                purchaseHistoryList.add(new PurchaseHistory("Iphone13Plus", 999.00, LocalDate.now().minusMonths(3)));
                customerInformation.setPurchaseHistory(purchaseHistoryList);
                break;
            default:
                purchaseHistoryList.add(new PurchaseHistory("SamsungGalaxy-S", 999.00, LocalDate.now().minusMonths(3)));
                purchaseHistoryList.add(new PurchaseHistory("Iphone15", 1099.00, LocalDate.now().minusMonths(2)));
                purchaseHistoryList.add(new PurchaseHistory("Iphone14Plus", 999.00, LocalDate.now()));
                customerInformation.setPurchaseHistory(purchaseHistoryList);
        }
        customerInformation.setPurchaseHistory(purchaseHistoryList);
        return customerInformation;
    }


}
