package ru.evtu.kursovoy_new.second;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * REST-контроллер для обработки операций с автомобилями.
 * Предоставляет API для выполнения различных операций, таких как добавление, обновление,
 * удаление и получение информации об автомобилях.
 */
@RestController
@RequestMapping("cars/api") // Базовый путь для API
public class CarApiController {

    @Autowired
    private CarModelRepository carModelRepository; // Репозиторий для работы с моделями автомобилей

    @Autowired
    private CarBrandRepository carBrandRepository; // Репозиторий для работы с марками автомобилей

    private final Map<LocalDate, Integer> issuedCars = new HashMap<>(); // Хранит количество выданных автомобилей по дням

    /**
     * Конструктор, инициализирующий данные о количестве выданных автомобилей за последние 30 дней.
     */
    public CarApiController() {
        for (int i = 0; i < 30; i++) {
            issuedCars.put(LocalDate.now().minusDays(i), (int) (Math.random() * 10));
        }
    }

    /**
     * Ищет автомобили по заданным параметрам.
     *
     * @param brand       марка автомобиля (может быть null)
     * @param ownerName   имя владельца автомобиля (может быть null)
     * @param startDate   дата начала поиска (может быть null)
     * @param endDate     дата окончания поиска (может быть null)
     * @param releaseYear год выпуска автомобиля (может быть null)
     * @return список автомобилей, соответствующих заданным параметрам
     */
    @PostMapping("/search")
    public List<Car> searchCars(@RequestParam(required = false) String brand,
                                @RequestParam(required = false) String ownerName,
                                @RequestParam(required = false) LocalDate startDate,
                                @RequestParam(required = false) LocalDate endDate,
                                @RequestParam(required = false) Integer releaseYear) {
        return CarService.search(brand, startDate, endDate, releaseYear, ownerName);
    }

    /**
     * Получает количество выданных автомобилей за последние 7 дней.
     *
     * @return массив, содержащий количество выданных автомобилей за последние 7 дней
     */
    @GetMapping("/statistics") // Указываем маршрут для получения данных
    public int[] getCarsIssuedLastWeek() {
        LocalDate today = LocalDate.now();
        int[] CarsIssued = new int[7];

        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            CarsIssued[i] = issuedCars.getOrDefault(date, 0);
        }

        return CarsIssued; // Возвращаем массив
    }

    /**
     * Удаляет автомобиль по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор автомобиля
     * @return ответ с сообщением об успешном удалении или ошибке
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        try {
            CarService.deleteCarById(id);
            return ResponseEntity.ok("Машина успешно удалена.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Машина не найдена.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении машины: " + e.getMessage());
        }
    }

    @Autowired
    private CarService CarService; // Сервис для работы с автомобилями

    /**
     * Обновляет информацию об автомобиле.
     *
     * @param id         уникальный идентификатор автомобиля
     * @param carDetails обновлённые данные автомобиля
     * @return ответ с сообщением об успешном обновлении или ошибке
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCar(@PathVariable Long id, @RequestBody Car carDetails) {
        try {
            CarService.updateCar(id, carDetails);
            return ResponseEntity.ok("Машина успешно обновлена.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Машина не найдена.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при обновлении машины: " + e.getMessage());
        }
    }

    /**
     * Возвращает список всех автомобилей.
     *
     * @return список всех автомобилей в формате JSON
     */
    @GetMapping("/search")
    @ResponseBody // Указывает, что возвращается JSON
    public List<Car> searchCars() {
        List<Car> allCars = CarService.findAll(); // Получаем все машины
        return new ArrayList<>(allCars); // Возвращаем отфильтрованные машины в формате JSON
    }

    /**
     * Добавляет новый автомобиль.
     *
     * @param car объект автомобиля, который необходимо добавить
     * @return ответ с сообщением о результате операции
     */
    @PostMapping("/add")
    public ResponseEntity<?> addCar(@ModelAttribute Car car) {
        try {
            CarService.addCar(car);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Автомобиль успешно добавлен!")); // Успешный ответ в формате JSON
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Ошибка при добавлении автомобиля: " + e.getMessage()));
        }
    }

    /**
     * Получает количество зарегистрированных автомобилей по дням.
     *
     * @return карта с количеством зарегистрированных автомобилей по дням
     */
    @GetMapping("/reg-count")
    public ResponseEntity<Map<LocalDate, Long>> getRegCountByDay() {
        Map<LocalDate, Long> issuedBooksCount = CarService.getRegCarsCountByDay();
        return ResponseEntity.ok(issuedBooksCount);
    }

    /**
     * Возвращает список моделей автомобилей по коду марки.
     *
     * @param brandCode код марки автомобиля
     * @return список моделей автомобилей, соответствующих заданному коду марки
     */
    @GetMapping("/models")
    @CrossOrigin(origins = "http://localhost:3000") // Указать ваш фронтенд URL
    @ResponseBody
    public List<CarModel> getModels(@RequestParam String brandCode) {
        return carModelRepository.findByCarBrand_BrandCode(brandCode);
    }

    /**
     * Возвращает список всех автомобильных марок.
     *
     * @return список всех марок автомобилей в формате JSON
     */
    @GetMapping("/brands")
    @CrossOrigin(origins = "http://localhost:3000") // Указать ваш фронтенд URL
    public ResponseEntity<List<CarBrand>> getAllBrands() {
        List<CarBrand> brands = carBrandRepository.findAll(); // Получаем все марки из БД
        return ResponseEntity.ok(brands); // Возвращаем марки в формате JSON
    }
}
