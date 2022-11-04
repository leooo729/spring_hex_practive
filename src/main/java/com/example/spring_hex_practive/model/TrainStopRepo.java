package com.example.spring_hex_practive.model;

import com.example.spring_hex_practive.model.entity.TrainStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainStopRepo extends JpaRepository<TrainStop, String> {

    List<TrainStop> findByTrainUuid(String trainUuid);
    @Query(value = "SELECT TRAIN_UUID FROM TRAIN_STOP WHERE NAME=?1 ORDER BY TIME ASC",nativeQuery = true)
    List<String> findTrainUuidByName(String name);
    @Query(value = "SELECT SEQ FROM TRAIN_STOP WHERE NAME=?1 AND TRAIN_UUID=?2 ",nativeQuery = true)
    int findTrainStopSeq(String name,String uuid);
}
