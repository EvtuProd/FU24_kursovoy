package ru.evtu.kursovoy_new.telegram;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.evtu.kursovoy_new.BotTokenConfig;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

/**
 * Сервис для взаимодействия с Telegram API.
 *
 * Этот класс наследуется от TelegramLongPollingBot и предоставляет методы для отправки уведомлений
 * и обработки обновлений от пользователей.
 */
@Service
public class TelegramBotService extends TelegramLongPollingBot {

    private final String botUsername = "VKtoTGconvertbot";
    private final String botToken;

    /**
     * Конструктор, загружающий токен из конфигурационного файла.
     *
     * @throws IOException если возникает ошибка при загрузке конфигурации
     */
    public TelegramBotService() throws IOException {
        BotTokenConfig config = BotTokenConfig.loadFromFile("/Users/entukhachevskiy/IdeaProjects/kursovoy_NEW/src/main/java/ru/evtu/kursovoy_new/bot_token.json");
        this.botToken = config.getToken();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                // Выводим chatId в консоль
                System.out.println("Chat ID: " + message.getChatId());
            }
        }
    }

    /**
     * Отправляет уведомление о обновлении информации об автомобиле.
     *
     * @param carBrand      марка автомобиля
     * @param carYear       год выпуска автомобиля
     * @param registrationDate дата регистрации автомобиля
     * @param ownerName     имя владельца автомобиля
     * @param changedField  название поля, которое было изменено
     */
    public void sendCarUpdateNotification(String carBrand, int carYear, String registrationDate, String ownerName, String changedField) {
        String chatId = "248217884";

        String messageText = String.format(
                "Автомобиль отредактирован:\nМарка: %s\nГод выпуска: %d\nДата регистрации: %s\nВладелец: %s\nИзменено: %s",
                carBrand, carYear, registrationDate, ownerName, changedField
        );

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);

        try {
            execute(message); // Отправка сообщения в Telegram
            System.out.println("Сообщение отправлено: " + messageText);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка при отправке сообщения: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Отправляет уведомление о добавлении автомобиля.
     *
     * @param carBrand       марка автомобиля
     * @param carYear        год выпуска автомобиля
     * @param registrationDate дата регистрации автомобиля
     * @param ownerName      имя владельца автомобиля
     */
    public void sendCarAdditionNotification(String carBrand, int carYear, String registrationDate, String ownerName) {
        String chatId = "248217884";

        String messageText = String.format(
                "Автомобиль добавлен:\nМарка: %s\nГод выпуска: %d\nДата регистрации: %s\nВладелец: %s",
                carBrand, carYear, registrationDate, ownerName
        );

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);

        try {
            execute(message); // Отправка сообщения в Telegram
            System.out.println("Сообщение отправлено: " + messageText);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка при отправке сообщения: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Отправляет уведомление об удалении автомобиля.
     *
     * @param carBrand       марка автомобиля
     * @param registrationDate дата регистрации автомобиля
     * @param ownerName      имя владельца автомобиля
     */
    public void sendCarDeletionNotification(String carBrand, String registrationDate, String ownerName) {
        String chatId = "248217884";

        String messageText = String.format(
                "Автомобиль удален:\nМарка: %s\nДата регистрации: %s\nВладелец: %s",
                carBrand, registrationDate, ownerName
        );

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);

        try {
            execute(message); // Отправка сообщения в Telegram
            System.out.println("Сообщение отправлено: " + messageText);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка при отправке сообщения: " + e.getMessage());
            e.printStackTrace();
        }
    }
}




