package ru.evtu.kursovoy_new.second;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для управления операциями, связанными с автомобилями.
 *
 * Этот класс обрабатывает запросы, связанные с созданием, обновлением, удалением и поиском автомобилей,
 * а также отображает различные страницы, связанные с автомобилями.
 */
@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService CarService; // Сервис для работы с автомобилями

    @Autowired
    private CarBrandRepository carBrandRepository; // Репозиторий для работы с марками автомобилей

    /**
     * Отображает страницу со списком всех автомобилей.
     *
     * @param model модель, в которую добавляется список автомобилей
     * @return имя шаблона для отображения списка автомобилей
     */
    @GetMapping
    public String getAllCars(Model model) {
        List<Car> cars = CarService.findAll();
        model.addAttribute("Cars", cars);
        return "second/car-list"; // Шаблон для списка автомобилей
    }

    /**
     * Отображает форму для добавления нового автомобиля.
     *
     * @param model модель, в которую добавляется новый объект Car и список марок
     * @return имя шаблона для отображения формы добавления автомобиля
     */
    @GetMapping("/add")
    public String showAddCarForm(Model model) {
        model.addAttribute("car", new Car()); // Создаем новый объект Car и добавляем его в модель
        model.addAttribute("brands", carBrandRepository.findAll()); // Получаем список марок
        return "second/add-car"; // Возвращаем имя шаблона для рендеринга
    }

    /**
     * Отображает страницу поиска автомобилей.
     *
     * @return имя шаблона для отображения формы поиска автомобиля
     */
    @GetMapping("/search")
    public String showSearchPage() {
        return "second/search-car";
    }

    /**
     * Выполняет поиск автомобилей по заданным параметрам.
     *
     * @param title      название автомобиля (может быть null)
     * @param startDate  дата начала периода поиска (может быть null)
     * @param endDate    дата окончания периода поиска (может быть null)
     * @return список автомобилей, соответствующих заданным параметрам; возвращается в формате JSON
     */
    @PostMapping("/search")
    @ResponseBody // Указывает, что возвращается JSON
    public List<Car> searchCars(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        List<Car> cars;

        if (title != null && !title.isEmpty()) {
            cars = CarService.findByTitle(title);
        } else if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            try {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                cars = CarService.findByDateRange(start, end);
            } catch (DateTimeParseException e) {
                // Логирование ошибки может быть добавлено здесь
                return new ArrayList<>(); // Возвращаем пустой список в случае ошибки
            }
        } else {
            cars = CarService.findAll();
        }

        return cars; // Возвращаем список автомобилей
    }

    private final Map<LocalDate, Integer> issuedCars = new HashMap<>();

    /**
     * Конструктор контроллера, инициализирующий данные о количестве выданных автомобилей.
     * Данные генерируются случайным образом для последующих 30 дней.
     */
    public CarController() {
        for (int i = 0; i < 30; i++) { // Пример данных за последние 30 дней
            issuedCars.put(LocalDate.now().minusDays(i), (int) (Math.random() * 10)); // Случайное количество
        }
    }

    /**
     * Получает статистику по количеству выданных автомобилей за последние 7 дней.
     *
     * @return массив, содержащий количество выданных автомобилей за последние 7 дней
     */
    @GetMapping("/statistics")
    public int[] getCarsIssuedLastWeek() {
        LocalDate today = LocalDate.now();
        int[] CarsIssued = new int[7];

        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            // Используем .getOrDefault для возврата 0, если данных нет
            CarsIssued[i] = issuedCars.getOrDefault(date, 0);
        }

        return CarsIssued; // Возвращаем массив
    }


    /**
     * Отображает страницу с гистограммой данных о регистрации автомобилей.
     *
     * @param model модель, в которую добавляются данные для графика
     * @return имя шаблона для отображения гистограммы
     */
    @GetMapping("/histogram")
    public String showHistogram(Model model) {
        // Получите данные для графика
        Map<LocalDate, Long> carsCount = CarService.getRegCarsCountByDay(); // Использовать экземпляр
        model.addAttribute("booksCount", carsCount); // Добавьте в модель
        return "second/car_histogram"; // Верните имя нового шаблона
    }


    /**
     * Отображает страницу с информацией о проекте.
     *
     * @param model модель, доступная на странице
     * @return имя шаблона для отображения страницы "О проекте"
     */
    @GetMapping("/about")
    public String showAbout(Model model) {
        return "second/about";
    }
}




