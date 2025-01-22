package com.example.jiranbulletinboard.Domain.Vo.Title;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/titles")
public class TitleController {

    @Autowired
    private TitleService titleService;

    @GetMapping
    public List<TitleEntity> getAllTitles() {
        return titleService.getAllTitles();
    }
}