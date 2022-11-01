package com.example.spring_hex_practive.model;

import com.example.spring_hex_practive.controller.dto.response.Stops;
import com.example.spring_hex_practive.model.entity.TrainStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainStopRepo extends JpaRepository<TrainStop, String> {
    @Query(value = "SELECT NAME,TIME FROM TRAIN_STOP WHERE TRAIN_UUID=?1",nativeQuery = true)
    List<String> findNameAndTimeByTrainUuid(String trainUuid);

    List<TrainStop> findByTrainUuid(String trainUuid);
}
