package com.charter.agentOs.model.Response;

import com.charter.agentOs.util.Month;
import lombok.Data;

import java.util.Map;

@Data
public class PointsStatementMonthlyResponse {
    private String error;
    private Map<Month, Long> data;
    private String warnings;
}
