package com.charter.agentOs.model;

import lombok.Data;

import java.util.List;

@Data
public class CustomerInformation {

    private long customerId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<PurchaseHistory> purchaseHistory;
}
