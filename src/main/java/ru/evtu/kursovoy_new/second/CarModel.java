package ru.evtu.kursovoy_new.second;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс, представляющий сущность модели автомобиля.
 *
 * Этот класс хранит информацию о различных моделях автомобилей,
 * включая их код, название, классы, годы выпуска и связь с соответствующей маркой.
 */
@Entity
@Table(name = "car_models")
@Getter
@Setter
public class CarModel {

    /**
     * Уникальный идентификатор модели автомобиля.
     * Поле генерируется автоматически при добавлении новой модели в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Код модели автомобиля.
     * Это поле представляет собой уникальный код, используемый для идентификации модели.
     */
    private String modelCode; // model_code

    /**
     * Название модели автомобиля.
     * Это поле содержит название модели на латинице.
     */
    private String modelName; // model_name

    /**
     * Название модели автомобиля на кириллице.
     * Это поле используется для хранения кириллической версии названия модели.
     */
    private String modelCyrillic; // model_cyrillic

    /**
     * Класс автомобиля.
     * Это поле используется для указания класса автомобиля (например, SUV, седан и т.д.).
     */
    private String carClass; // car_class

    /**
     * Год, с которого доступна данная модель автомобиля.
     * Может быть null, если информация отсутствует.
     */
    private Integer yearFrom; // year_from

    /**
     * Год, до которого доступна данная модель автомобиля.
     * Может быть null, если информация отсутствует.
     */
    private Integer yearTo; // year_to

    /**
     * Связь модели автомобиля с маркой.
     * Это поле представляет собой внешний ключ на сущность CarBrand.
     */
    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private CarBrand carBrand; // Внешний ключ на CarBrand
}




