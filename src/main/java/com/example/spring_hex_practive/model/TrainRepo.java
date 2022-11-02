package com.example.spring_hex_practive.model;

import com.example.spring_hex_practive.model.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepo extends JpaRepository<Train, String> {
    Train findByTrainNo(int trainNo);
    Train findByUuid(String uuid);
}
