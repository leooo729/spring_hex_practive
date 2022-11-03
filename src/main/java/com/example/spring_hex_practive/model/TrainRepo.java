package com.example.spring_hex_practive.model;

import com.example.spring_hex_practive.model.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainRepo extends JpaRepository<Train, String> {
    Train findByTrainNo(int trainNo);
    Train findByUuid(String uuid);
    List<Train> findByTrainKind(String trainKind);

    @Query(value = "SELECT UUID FROM TRAIN WHERE TRAIN_NO=?1",nativeQuery = true)
    String findUuidByTrainNo(Integer trainNo);


}
