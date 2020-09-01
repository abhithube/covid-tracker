package io.abhithube.covidtracker.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_stats")
@Getter
@Setter
@NoArgsConstructor
public class DailyStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "cases")
    private long cases;
    @Column(name = "daily_cases")
    private long dailyCases;
    @Column(name = "deaths")
    private long deaths;
    @Column(name = "daily_deaths")
    private long dailyDeaths;
    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    public DailyStats(LocalDate date, long cases, long deaths) {
        this.date = date;
        this.cases = cases;
        this.deaths = deaths;
    }
}
