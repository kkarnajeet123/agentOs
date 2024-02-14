package com.charter.agentOs.model.Response;

import java.time.Month;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class PointsStatementMonthlyResponse {
    private String error;
    private Map<Long,Map<Month, Integer>> data ;
    private String warnings;
}
