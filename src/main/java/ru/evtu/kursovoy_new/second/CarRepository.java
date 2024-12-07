package ru.evtu.kursovoy_new.second;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByBrandContainingIgnoreCase(String title);
    List<Car> findByRegDateBetween(LocalDate startDate, LocalDate endDate);
    List<Car> findByBrandIgnoreCase(String brand);
    List<Car> findByReleaseYearEquals(Integer releaseDate);
    List<Car> findByOwnerNameEquals(String ownerName);
}



