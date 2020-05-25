package com.roomfix.api.failureStatistics;

import com.roomfix.api.building.Building;
import com.roomfix.api.building.BuildingRepository;
import com.roomfix.api.failure.Failure;
import com.roomfix.api.failure.FailureRepository;
import com.roomfix.api.failureStatistics.dto.BuildingStatisticsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/statistics")
public class FailureStatisticsController {

    private final BuildingRepository buildingRepository;

    private final FailureRepository failureRepository;



    @Autowired
    public FailureStatisticsController(BuildingRepository buildingRepository, FailureRepository failureRepository){
        this.buildingRepository = buildingRepository;
        this.failureRepository=failureRepository;
    }


    @GetMapping("/building/summary")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<BuildingStatisticsDto> getSummaryForAllBuildings(){
        ArrayList<BuildingStatisticsDto> buildingStatisticsDtoArrayList = new ArrayList<>();
        List<Building> buildingList = this.buildingRepository.findAll();
        List<Failure> failureList = this.failureRepository.findAll();
        for (Building building: buildingList
             ) {
            BuildingStatisticsDto buildingStatisticsDto = new BuildingStatisticsDto();
            long buildingId = building.getId();
            buildingStatisticsDto.setBuildingName(building.getName());
            for (Failure failure:failureList){
                if (failure.getRoom().getBuilding().getId()==buildingId){
                    switch (failure.getState()){
                        case UN_RESOLVED:
                            int newUnresolvedCount = buildingStatisticsDto.getUnresolvedFailuresSum();
                            newUnresolvedCount+=1;
                            buildingStatisticsDto.setUnresolvedFailuresSum(newUnresolvedCount);
                            break;


                        case ONGOING:
                            int newOngoingCount = buildingStatisticsDto.getOnGoingFailuresSum();
                            newOngoingCount+=1;
                            buildingStatisticsDto.setOnGoingFailuresSum(newOngoingCount);
                            break;

                        case CLOSED:
                            int newClosedCount = buildingStatisticsDto.getClosedFailuresSum();
                            newClosedCount+=1;
                            buildingStatisticsDto.setClosedFailuresSum(newClosedCount);
                            break;

                        case USELESS:
                            int newUselessCount = buildingStatisticsDto.getUselessFailuresSum();
                            newUselessCount+=1;
                            buildingStatisticsDto.setUselessFailuresSum(newUselessCount);
                            break;
                    }
                }
            }
            buildingStatisticsDtoArrayList.add(buildingStatisticsDto);
        }




        return buildingStatisticsDtoArrayList;
    }

    //@Query("SELECT count (failures) from Failure failures where(failures.state = 'CLOSED' ) AND (failures.room.building.id = :buildingId)")
  //  long countClosedFailuresByBuildingId(long buildingId);

}
