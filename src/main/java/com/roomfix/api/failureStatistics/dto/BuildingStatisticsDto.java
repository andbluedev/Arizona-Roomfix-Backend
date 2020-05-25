package com.roomfix.api.failureStatistics.dto;

import lombok.Data;

@Data
public class BuildingStatisticsDto {
    private String buildingName;
    private int closedFailuresSum;
    private int onGoingFailuresSum;
    private int unresolvedFailuresSum;
    private int uselessFailuresSum;
}
