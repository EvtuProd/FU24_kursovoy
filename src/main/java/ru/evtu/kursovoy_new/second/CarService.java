package ru.evtu.kursovoy_new.second;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.evtu.kursovoy_new.telegram.TelegramBotService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для управления автомобилями.
 *
 * Этот класс предоставляет методы для выполнения операций над автомобилями,
 * таких как поиск, добавление, обновление и удаление. Он также отправляет
 * уведомления через Telegram о изменениях в моделях автомобилей.
 */
@Service
public class CarService {

    private final TelegramBotService telegramBotService;

    @Autowired
    private CarRepository CarRepository;

    // Конструктор для внедрения зависимости
    public CarService(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    /**
     * Получает список всех автомобилей.
     *
     * @return список всех автомобилей.
     */
    public List<Car> findAll() {
        return CarRepository.findAll(); // Возвращаем все автомобили без дополнительных проверок
    }

    /**
     * Находит автомобили по названию марки.
     *
     * @param title название марки (может быть null)
     * @return список автомобилей, соответствующих заданному названию.
     */
    public List<Car> findByTitle(String title) {
        return CarRepository.findByBrandContainingIgnoreCase(title);
    }

    /**
     * Находит автомобили по диапазону дат регистрации.
     *
     * @param startDate дата начала диапазона
     * @param endDate дата окончания диапазона
     * @return список автомобилей, зарегистрированных в указанный диапазон дат.
     */
    public List<Car> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return CarRepository.findByRegDateBetween(startDate, endDate);
    }

    /**
     * Добавляет новый автомобиль.
     *
     * @param car объект автомобиля, который необходимо добавить.
     */
    public void addCar(Car car) {
        car.setRegDate(LocalDate.now()); // Установка текущей даты как даты регистрации
        Car savedCar = CarRepository.save(car); // Сохранение автомобиля в базе данных

        // Уведомление о добавлении
        telegramBotService.sendCarAdditionNotification(
                savedCar.getBrand(),
                savedCar.getReleaseYear(),
                savedCar.getRegDate().toString(),
                savedCar.getOwnerName()
        );
    }

    /**
     * Находит автомобиль по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор автомобиля.
     * @return объект автомобиля, соответствующий заданному идентификатору.
     * @throws RuntimeException если автомобиль не найден.
     */
    public Car findById(Long id) {
        return CarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found"));
    }

    /**
     * Обновляет информацию об автомобиле.
     *
     * @param id уникальный идентификатор автомобиля.
     * @param carDetails объект автомобиля с обновлённой информацией.
     * @throws EntityNotFoundException если автомобиль не найден.
     */
    public void updateCar(Long id, Car carDetails) {
        Car existingCar = CarRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Машина не найдена с ID: " + id));

        // Сохранение старых значений для проверки изменений
        String oldOwnerName = existingCar.getOwnerName();
        String oldBrand = existingCar.getBrand();
        int oldYear = existingCar.getReleaseYear();
        String oldRegDate = existingCar.getRegDate().toString(); // Преобразуем LocalDate в String

        // Обновление данных автомобиля
        existingCar.setBrand(carDetails.getBrand());
        existingCar.setReleaseYear(carDetails.getReleaseYear());
        existingCar.setRegDate(carDetails.getRegDate());
        existingCar.setOwnerName(carDetails.getOwnerName());

        // Сохраняем обновлённые данные в базе
        CarRepository.save(existingCar);

        // Проверяем, какие поля были изменены и отправляем уведомление, если есть изменения
        String changedField = determineChangedField(oldOwnerName, oldBrand, oldYear, oldRegDate, existingCar);
        if (!changedField.isEmpty()) {
            telegramBotService.sendCarUpdateNotification(
                    existingCar.getBrand(),
                    existingCar.getReleaseYear(),
                    existingCar.getRegDate().toString(),
                    existingCar.getOwnerName(),
                    changedField
            );
        }
    }

    /**
     * Удаляет автомобиль по ID с проверкой существования и отправляет уведомление об удалении.
     *
     * @param id уникальный идентификатор автомобиля.
     * @throws EntityNotFoundException если автомобиль с заданным ID не найден.
     */
    public void deleteCarById(Long id) {
        // Проверяем, существует ли автомобиль
        Car carToDelete = CarRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Автомобиль с ID " + id + " не найден."));

        // Уведомляем через TelegramBotService об удалении
        telegramBotService.sendCarDeletionNotification(
                carToDelete.getBrand(),
                carToDelete.getRegDate() != null ? carToDelete.getRegDate().toString() : "не указана",
                carToDelete.getOwnerName() != null ? carToDelete.getOwnerName() : "не указан"
        );

        // Удаляем автомобиль из базы данных
        CarRepository.deleteById(id);
    }

    /**
     * Находит автомобили по диапазону дат регистрации.
     *
     * @param startDate дата начала диапазона
     * @param endDate дата окончания диапазона
     * @return список автомобилей, зарегистрированных в указанный диапазон дат.
     */
    public List<Car> findByRegDateBetween(LocalDate startDate, LocalDate endDate) {
        return CarRepository.findByRegDateBetween(startDate, endDate);
    }

    /**
     * Находит автомобили по названию марки.
     *
     * @param brand название марки
     * @return список автомобилей, соответствующих заданной марке.
     */
    public List<Car> findByBrand(String brand) {
        return CarRepository.findByBrandIgnoreCase(brand);
    }

    /**
     * Находит автомобили по году выпуска.
     *
     * @param releaseYear год выпуска
     * @return список автомобилей, выпущенных в указанный год.
     */
    public List<Car> findByReleaseYear(Integer releaseYear) {
        return CarRepository.findByReleaseYearEquals(releaseYear);
    }

    /**
     * Находит автомобили по имени владельца.
     *
     * @param ownerName имя владельца
     * @return список автомобилей, принадлежащих указанному владельцу.
     */
    public List<Car> findByOwnerName(String ownerName) {
        return CarRepository.findByOwnerNameEquals(ownerName);
    }

    /**
     * Выполняет объединённый поиск автомобилей по нескольким критериям.
     *
     * @param brand марка автомобиля (может быть null)
     * @param startDate дата начала диапазона (может быть null)
     * @param endDate дата окончания диапазона (может быть null)
     * @param releaseYear год выпуска (может быть null)
     * @param ownerName имя владельца (может быть null)
     * @return список уникальных автомобилей, соответствующих критериям поиска.
     */
    public List<Car> search(String brand, LocalDate startDate, LocalDate endDate, Integer releaseYear, String ownerName) {
        List<Car> cars = new ArrayList<>();

        // Получаем результаты по каждому критерию поиска
        if (brand != null && !brand.isEmpty()) {
            cars.addAll(findByBrand(brand));
        }
        if (startDate != null && endDate != null) {
            cars.addAll(findByRegDateBetween(startDate, endDate));
        }
        if (releaseYear != null) {
            cars.addAll(findByReleaseYear(releaseYear));
        }
        if (ownerName != null && !ownerName.isEmpty()) {
            cars.addAll(findByOwnerName(ownerName));
        }

        // Удаляем дубликаты
        List<Car> distinctCars = cars.stream().distinct().toList();
        // Если результатов нет, вернем все автомобили
        if (distinctCars.isEmpty()) {
            return CarRepository.findAll(); // Возвращаем все автомобили
        }

        return distinctCars; // Возвращаем уникальные результаты поиска
    }

    /**
     * Получает статистику о количестве зарегистрированных автомобилей по дням за последнюю неделю.
     *
     * @return карта, где ключ - дата регистрации, значение - количество автомобилей, зарегистрированных в эту дату.
     */
    public Map<LocalDate, Long> getRegCarsCountByDay() {
        List<Car> allCars = CarRepository.findAll(); // Получаем все автомобили
        Map<LocalDate, Long> countMap = new HashMap<>();

        for (Car car : allCars) {
            LocalDate regDate = car.getRegDate(); // Получаем дату регистрации
            // Учитываем только за последние 7 дней
            if (regDate != null && regDate.isAfter(LocalDate.now().minusDays(7))) {
                countMap.put(regDate, countMap.getOrDefault(regDate, 0L) + 1);
            }
        }

        // Если необходимо, сюда можно добавить обработку, чтобы показать данные за 7 последних дней.
        return countMap;
    }

    /**
     * Определяет, какие поля автомобиля были изменены.
     *
     * @param oldOwnerName старое имя владельца
     * @param oldBrand старое название марки
     * @param oldYear старый год выпуска
     * @param oldRegDate старая дата регистрации
     * @param existingCar текущий объект автомобиля с обновлёнными данными
     * @return строка, указывающая, какие поля изменились (владелец, марка, год выпуска, дата регистрации)
     */
    private String determineChangedField(String oldOwnerName, String oldBrand, int oldYear, String oldRegDate, Car existingCar) {
        String changedField = "";
        if (!oldOwnerName.equals(existingCar.getOwnerName())) {
            changedField = "владелец";
        } else if (!oldBrand.equals(existingCar.getBrand())) {
            changedField = "марка";
        } else if (oldYear != existingCar.getReleaseYear()) {
            changedField = "год выпуска";
        } else if (!oldRegDate.equals(existingCar.getRegDate().toString())) {
            changedField = "дата регистрации";
        }
        return changedField;
    }
}
