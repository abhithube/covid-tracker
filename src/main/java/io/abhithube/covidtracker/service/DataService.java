package io.abhithube.covidtracker.service;

import io.abhithube.covidtracker.entity.Region;
import io.abhithube.covidtracker.entity.DailyStats;
import io.abhithube.covidtracker.repository.DailyStatsRepository;
import io.abhithube.covidtracker.repository.RegionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class DataService {
    public static final String COUNTRY_ALL_DATA_URL = "https://raw.githubusercontent.com/nytimes/covid-19-data/master/us.csv";
    private static final String STATE_LIVE_DATA_URL = "https://raw.githubusercontent.com/nytimes/covid-19-data/master/live/us-states.csv";
    private static final String COUNTRY_LIVE_DATA_URL = "https://raw.githubusercontent.com/nytimes/covid-19-data/master/live/us.csv";
    private static final String STATE_ALL_DATA_URL = "https://raw.githubusercontent.com/nytimes/covid-19-data/master/us-states.csv";

    public static Date lastModified = new Date();
    public static LocalDate latest;

    public static final Map<String, DailyStats> regionPrevStats = new HashMap<>();

    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private DailyStatsRepository dailyStatsRepository;

    @PostConstruct
    public void initialize() throws IOException, InterruptedException {
        regionPrevStats.put("United States", new DailyStats(LocalDate.now(), 0, 0));
        regionRepository.save(new Region("United States"));

        for (CSVRecord record : fetchData(STATE_LIVE_DATA_URL)) {
            LocalDate date = LocalDate.parse(record.get("date"));
            regionPrevStats.put(record.get("state"), new DailyStats(date, 0, 0));
            regionRepository.save(new Region(record.get("state")));
        }

        updateDB(fetchData(COUNTRY_ALL_DATA_URL));
        updateDB(fetchData(STATE_ALL_DATA_URL));
        getLiveData();
    }

    // pull live data in the afternoon and evening
    @Scheduled(cron = "0 0 12,18 * * *")
    public void getLiveData() throws IOException, InterruptedException {
        updateDB(fetchData(COUNTRY_LIVE_DATA_URL));
        updateDB(fetchData(STATE_LIVE_DATA_URL));
        lastModified = new Date();
    }

    public Iterable<CSVRecord> fetchData(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader reader = new StringReader(response.body());
        return CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
    }

    private void updateDB(Iterable<CSVRecord> records) {
        for (CSVRecord record : records) {
            String regionStr;
            try {
                regionStr = record.get("state");
            } catch (IllegalArgumentException e) {
                regionStr = "United States";
            }

            LocalDate date = LocalDate.parse(record.get("date"));
            long cases = Long.parseLong(record.get("cases"));
            long deaths = Long.parseLong(record.get("deaths"));

            Region region = regionRepository.findByName(regionStr);
            DailyStats newStats = dailyStatsRepository.findByDateAndRegion(date, region);
            if (newStats == null) newStats = new DailyStats(date, cases, deaths);

            DailyStats prevStats = regionPrevStats.get(regionStr);
            newStats.setDailyCases(cases - prevStats.getCases());
            newStats.setDailyDeaths(deaths - prevStats.getDeaths());
            regionPrevStats.replace(regionStr, newStats);

            region.addToDailyStats(newStats);
            regionRepository.save(region);
            latest = date;
        }
    }
}
