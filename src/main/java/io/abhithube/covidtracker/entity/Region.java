package io.abhithube.covidtracker.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "region")
@Getter
@Setter
@NoArgsConstructor
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "region", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<DailyStats> dailyStats = new ArrayList<>();

    public Region(String name) {
        this.name = name;
    }

    public void addToDailyStats(DailyStats dailyStats) {
        this.dailyStats.add(dailyStats);
        dailyStats.setRegion(this);
    }
}
