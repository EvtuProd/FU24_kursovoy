package ru.evtu.kursovoy_new.second;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Класс, представляющий сущность автомобиля в базе данных.
 *
 * Этот класс используется для хранения информации о различных автомобилях,
 * включая их марку, год выпуска, дату регистрации и имя владельца.
 */
@Entity
@Table(name = "cars")
@Getter
@Setter
public class Car {

    /**
     * Уникальный идентификатор автомобиля.
     * Это поле генерируется автоматически при добавлении нового автомобиля в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Марка автомобиля.
     * Это поле обязательно для заполнения.
     */
    @NotNull
    private String brand;

    /**
     * Год выпуска автомобиля.
     * Это поле обязательно для заполнения.
     */
    @NotNull
    private Integer releaseYear;

    /**
     * Дата регистрации автомобиля.
     * Может быть нулевой, если информация о регистрации отсутствует.
     */
    private LocalDate regDate;

    /**
     * Имя владельца автомобиля.
     * Может быть нулевым, если информация о владельце отсутствует.
     */
    private String ownerName;

}
