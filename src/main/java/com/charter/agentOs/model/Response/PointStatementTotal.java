package com.charter.agentOs.model.Response;

import lombok.Data;

import java.util.List;

@Data
public class PointStatementTotal {

    private List<Object> error;
    private double data;
    private List<Object> warnings;
}
