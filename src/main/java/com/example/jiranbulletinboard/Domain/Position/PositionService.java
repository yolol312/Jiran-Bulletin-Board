package com.example.jiranbulletinboard.Domain.Position;

import com.example.jiranbulletinboard.Domain.Title.TitleEntity;
import com.example.jiranbulletinboard.Domain.Title.TitleRepository;
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
