package com.roomfix.api.failureStatistics;

import com.roomfix.api.building.Building;
import com.roomfix.api.building.BuildingRepository;
import com.roomfix.api.common.exceptionhandling.exception.ResourceNotFoundException;
import com.roomfix.api.failure.Failure;
import com.roomfix.api.failure.FailureRepository;
import com.roomfix.api.failureStatistics.dto.StatisticsDto;
import com.roomfix.api.room.Room;
import com.roomfix.api.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/statistics")
public class FailureStatisticsController {
    private final BuildingRepository buildingRepository;
    private final FailureRepository failureRepository;
    private final RoomRepository roomRepository;


    @Autowired
    public FailureStatisticsController(RoomRepository roomRepository, BuildingRepository buildingRepository, FailureRepository failureRepository) {
        this.buildingRepository = buildingRepository;
        this.failureRepository = failureRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/building/summary")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<StatisticsDto> getSummaryForAllBuildings() {
        return getSummary("building", false, 0);
    }

    @GetMapping("/building/{id}/summary")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<StatisticsDto> getSummaryForBuilding(@PathVariable("id") long buildingId) {
        return getSummary("building", true, buildingId);
    }

    @GetMapping("/room/summary")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<StatisticsDto> getSummaryForAllRooms() {
        return getSummary("room", false, 0);
    }

    @GetMapping("/room/{id}/summary")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<StatisticsDto> getSummaryForRoom(@PathVariable("id") long roomId) {
        return getSummary("room", true, roomId);
    }

    private ArrayList<StatisticsDto> getSummary(String type, boolean byId, long id) {
        ArrayList<StatisticsDto> statisticsDtoArrayList = new ArrayList<>();
        List<Failure> failureList = this.failureRepository.findAll();
        if (type.equals("building") && !byId) {
            List<Building> buildingList = this.buildingRepository.findAll();
            for (Building building : buildingList
            ) {
                StatisticsDto statisticsDto = new StatisticsDto();
                long buildingId = building.getId();
                statisticsDto.setDtoname(building.getName());
                failuresCount(statisticsDto, failureList, buildingId, true);
                statisticsDtoArrayList.add(statisticsDto);
            }
        }
        if (type.equals("room") && !byId) {
            List<Room> roomList = this.roomRepository.findAll();
            for (Room room : roomList
            ) {
                StatisticsDto statisticsDto = new StatisticsDto();
                long roomId = room.getId();
                statisticsDto.setDtoname(room.getNumber());
                failuresCount(statisticsDto, failureList, roomId, false);
                statisticsDtoArrayList.add(statisticsDto);

            }
        }
        if (type.equals("building") && byId) {
            Building building = this.buildingRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
            StatisticsDto statisticsDto = new StatisticsDto();
            statisticsDto.setDtoname(building.getName());
            failuresCount(statisticsDto, failureList, id, true);
            statisticsDtoArrayList.add(statisticsDto);

        }

        if (type.equals("room") && byId) {
            Room room = this.roomRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
            StatisticsDto statisticsDto = new StatisticsDto();
            statisticsDto.setDtoname(room.getNumber());
            failuresCount(statisticsDto, failureList, id, false);
            statisticsDtoArrayList.add(statisticsDto);

        }

        return statisticsDtoArrayList;
    }

    private void failuresCount(StatisticsDto statisticsDto, List<Failure> failureList, long elementId, boolean elementIsBuilding) {
        long failurePlacementId;
        for (Failure failure : failureList) {
            if (elementIsBuilding) {
                failurePlacementId = failure.getRoom().getBuilding().getId();
            } else {
                failurePlacementId = failure.getRoom().getId();
            }
            if (failurePlacementId == elementId) {
                switch (failure.getState()) {
                    case UN_RESOLVED:
                        int newUnresolvedCount = statisticsDto.getUnresolvedFailuresSum();
                        newUnresolvedCount += 1;
                        statisticsDto.setUnresolvedFailuresSum(newUnresolvedCount);
                        break;

                    case ONGOING:
                        int newOngoingCount = statisticsDto.getOnGoingFailuresSum();
                        newOngoingCount += 1;
                        statisticsDto.setOnGoingFailuresSum(newOngoingCount);
                        break;

                    case CLOSED:
                        int newClosedCount = statisticsDto.getClosedFailuresSum();
                        newClosedCount += 1;
                        statisticsDto.setClosedFailuresSum(newClosedCount);
                        break;

                    case USELESS:
                        int newUselessCount = statisticsDto.getUselessFailuresSum();
                        newUselessCount += 1;
                        statisticsDto.setUselessFailuresSum(newUselessCount);
                        break;
                }
            }
        }
    }
}
