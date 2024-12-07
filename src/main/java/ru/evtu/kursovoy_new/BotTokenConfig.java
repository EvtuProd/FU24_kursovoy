package ru.evtu.kursovoy_new;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * Класс, представляющий конфигурацию токена бота.
 *
 * Этот класс используется для хранения токена и его загрузки
 * из JSON-файла с помощью библиотеки Jackson.
 */
public class BotTokenConfig {

    private String token;

    /**
     * Получает токен бота.
     *
     * @return токен бота.
     */
    public String getToken() {
        return token;
    }

    /**
     * Устанавливает токен бота.
     *
     * @param token токен бота, который необходимо установить.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Загружает конфигурацию токена из указанного файла.
     *
     * @param filePath путь к JSON-файлу, содержащему токен.
     * @return объект BotTokenConfig с загруженным токеном.
     * @throws IOException если возникает ошибка при чтении файла или парсинге JSON.
     */
    public static BotTokenConfig loadFromFile(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), BotTokenConfig.class);
    }
}



