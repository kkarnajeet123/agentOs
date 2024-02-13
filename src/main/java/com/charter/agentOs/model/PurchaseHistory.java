package com.charter.agentOs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistory {
    private String product;
    private double purchasePrice;
    private Date purchaseDate;
}
