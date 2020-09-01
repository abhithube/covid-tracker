package io.abhithube.covidtracker.controller;

import io.abhithube.covidtracker.entity.Region;
import io.abhithube.covidtracker.entity.DailyStats;
import io.abhithube.covidtracker.service.DailyStatsService;
import io.abhithube.covidtracker.service.DataService;
import io.abhithube.covidtracker.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class HomeController {

    @Autowired
    private RegionService regionService;
    @Autowired
    private DailyStatsService dailyStatsService;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM");

    @GetMapping("/")
    public String getHome() {
        return "home";
    }

    @GetMapping("/region/{name}")
    public String getStateData(@PathVariable String name, Model model) {
        Region region = regionService.getRegion(name.replace('-', ' '));
        List<DailyStats> statsList = region.getDailyStats();

        long totalCases = statsList.get(statsList.size() - 1).getCases();
        long totalDeaths = statsList.get(statsList.size() - 1).getDeaths();

        List<LocalDate> dates = getWeeklyStats(statsList.stream().map(DailyStats::getDate).collect(Collectors.toList()));
        List<Long> cases = getWeeklyStats(statsList.stream().map(DailyStats::getCases).collect(Collectors.toList()));
        List<Long> deaths = getWeeklyStats(statsList.stream().map(DailyStats::getDeaths).collect(Collectors.toList()));

        model.addAttribute("region", region.getName());
        model.addAttribute("totalCases", NumberFormat.getIntegerInstance().format(totalCases));
        model.addAttribute("totalDeaths", NumberFormat.getIntegerInstance().format(totalDeaths));
        model.addAttribute("statsList", statsList);
        model.addAttribute("regionList", regionService.getAllRegions());

        model.addAttribute("dates", dates);
        model.addAttribute("cases", cases);
        model.addAttribute("deaths", deaths);

        model.addAttribute("monthsStr", dates.stream().map(dtf::format).distinct().collect(Collectors.toList()));
        model.addAttribute("monthsInt", dates.stream().map(LocalDate::getMonthValue).distinct().collect(Collectors.toList()));
        model.addAttribute("lastModified", DataService.lastModified);

        return "region";
    }

    @GetMapping("/date/{date}")
    public String getDataByDate(@PathVariable String date, Model model) {
        if (date.equals("latest")) date = DataService.latest.toString();
        List<DailyStats> statsList = dailyStatsService.getAllStats(LocalDate.parse(date));
        statsList.remove(0);
        long totalCases = statsList.stream().mapToLong(DailyStats::getCases).sum();
        long totalDeaths = statsList.stream().mapToLong(DailyStats::getDeaths).sum();

        model.addAttribute("statsList", statsList);
        model.addAttribute("date", date);
        model.addAttribute("totalCases", NumberFormat.getIntegerInstance().format(totalCases));
        model.addAttribute("totalDeaths", NumberFormat.getIntegerInstance().format(totalDeaths));
        model.addAttribute("lastModified", DataService.lastModified);
        model.addAttribute("latest", DataService.latest);

        return "date";
    }

    private <T> List<T> getWeeklyStats(List<T> list) {
        return IntStream.range(0, list.size()).filter(n -> n % 7 == 0).mapToObj(list::get).collect(Collectors.toList());
    }
}
