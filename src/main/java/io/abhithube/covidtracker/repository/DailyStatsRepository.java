package io.abhithube.covidtracker.repository;

import io.abhithube.covidtracker.entity.Region;
import io.abhithube.covidtracker.entity.DailyStats;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyStatsRepository extends CrudRepository<DailyStats, Long> {
    List<DailyStats> findAllByDate(LocalDate date);

    DailyStats findByDateAndRegion(LocalDate date, Region region);
}
