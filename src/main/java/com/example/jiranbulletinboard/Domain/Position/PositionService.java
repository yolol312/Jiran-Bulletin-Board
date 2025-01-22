package com.example.jiranbulletinboard.Domain.Position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {
    @Autowired
    private PositionRepository positionRepository;

    public List<PositionEntity> getAllPositions() {
        return positionRepository.findAll();
    }
}
