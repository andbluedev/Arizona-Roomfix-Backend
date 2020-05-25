package com.roomfix.api.failureStatistics.dto;

import lombok.Data;

@Data
public class StatisticsDto {
    private String dtoname;
    private int closedFailuresSum;
    private int onGoingFailuresSum;
    private int unresolvedFailuresSum;
    private int uselessFailuresSum;
}
