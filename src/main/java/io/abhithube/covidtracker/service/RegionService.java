package io.abhithube.covidtracker.service;

import io.abhithube.covidtracker.entity.Region;
import io.abhithube.covidtracker.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    public List<Region> getAllRegions() {
        return (List<Region>) regionRepository.findAll();
    }

    public Region getRegion(String name) {
        return regionRepository.findByName(name);
    }
}