package io.abhithube.covidtracker.service;

import io.abhithube.covidtracker.entity.DailyStats;
import io.abhithube.covidtracker.repository.DailyStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyStatsService {
    @Autowired
    private DailyStatsRepository dailyStatsRepository;

    public List<DailyStats> getAllStats(LocalDate date) {
        return dailyStatsRepository.findAllByDate(date);
    }
}