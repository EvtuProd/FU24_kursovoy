package ru.evtu.kursovoy_new.second;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    List<CarModel> findByCarBrand_BrandCode(String brandCode); // Метод для поиска моделей по коду марки
}

