package com.charter.agentOs.model.Response;

import lombok.Data;

import java.util.Map;
@Data
public class TotalPointsStatementForEachCustomer {

    private String error;
    private Map<Long, Double> data;
    private String warnings;
}
