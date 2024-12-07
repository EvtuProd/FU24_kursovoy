package ru.evtu.kursovoy_new.second;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс, представляющий сущность марки автомобиля.
 *
 * Этот класс хранит информацию о марках автомобилей, включая их
 * код, название, кириллическое название, популярность и страну происхождения.
 */
@Entity
@Table(name = "car_brands")
@Getter
@Setter
public class CarBrand {

    /**
     * Уникальный идентификатор марки автомобиля.
     * Поле генерируется автоматически при добавлении новой марки в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Код марки автомобиля.
     * Это поле представляет собой уникальный код, используемый для идентификации марки.
     */
    private String brandCode; // brand_code

    /**
     * Название марки автомобиля.
     * Это поле содержит название марки на латинице.
     */
    private String brandName; // brand_name

    /**
     * Название марки автомобиля на кириллице.
     * Это поле используется для хранения кириллического варианта названия марки.
     */
    private String brandCyrillic; // brand_cyrillic

    /**
     * Популярность марки автомобиля.
     * Это поле используется для указания уровня популярности марки (например, рейтинг).
     */
    private Integer popularBrand; // popular_brand

    /**
     * Страна происхождения марки автомобиля.
     * Это поле содержит информацию о стране, в которой была основана марка.
     */
    private String country; // country

}
