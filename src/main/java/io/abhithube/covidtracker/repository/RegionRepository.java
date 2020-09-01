package io.abhithube.covidtracker.repository;

import io.abhithube.covidtracker.entity.Region;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends CrudRepository<Region, Long> {
    Region findByName(String name);
}
