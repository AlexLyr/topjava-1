package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }


    public Meal get(int id) {
        int userId = AuthorizedUser.id();
        log.info("get meal {} for userId={}", id, userId);
        return service.get(id, userId);
    }

    @RequestMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") int id) {
        int userId = AuthorizedUser.id();
        log.info("delete meal {} for userId={}", id, userId);
        service.delete(id, userId);
        return "redirect:/meals";
    }

    @RequestMapping(value = "meals", method = RequestMethod.GET)
    public String getAll(Model model) {
        int userId = AuthorizedUser.id();
        log.info("getAll for userId={}", userId);
        model.addAttribute("meals", MealsUtil.getWithExceeded(service.getAll(userId), AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }

    @RequestMapping(value = "meals/create", method = RequestMethod.POST)
    public String create(@ModelAttribute("meal") Meal meal) {
        int userId = AuthorizedUser.id();
        log.info("create {} for userId={}", meal, userId);
        if (meal.getId() == null) {
            service.create(meal, userId);
        } else {
            log.info("update {} with id={} for userId={}", meal, meal.getId(), userId);
            service.update(meal, userId);
        }
        return "redirect:/meals";
    }

    @RequestMapping(value = "update")
    public String update(@RequestParam("id") int id, Model model) {
        int userId = AuthorizedUser.id();
        if (id == 0) {
            Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
            model.addAttribute("meal", meal);
        } else {
            Meal meal = service.get(id, userId);
            assureIdConsistent(meal, id);
            model.addAttribute("meal", meal);
        }
        return "mealForm";
    }


    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    @RequestMapping(value = "filter", method = RequestMethod.POST)
    public String getBetween(Model model, HttpServletRequest request) {
        int userId = AuthorizedUser.id();
        LocalTime startTime=null, endTime=null;
        LocalDate startDate=null,endDate=null;
        if(!request.getParameter("startTime").equals("")&&!request.getParameter("endTime").equals("")) {
             startTime = LocalTime.parse(request.getParameter("startTime"));
             endTime = LocalTime.parse(request.getParameter("endTime"));
        }
        if(!request.getParameter("startDate").equals("")&&!request.getParameter("endDate").equals("")) {
             startDate = LocalDate.parse(request.getParameter("startDate"));
             endDate = LocalDate.parse(request.getParameter("endDate"));
        }
        log.info("getBetween dates({} - {}) time({} - {}) for userId={}", startDate, endDate, startTime, endTime, userId);
        List<MealWithExceed> list = MealsUtil.getFilteredWithExceeded(
                service.getBetweenDates(
                        startDate != null ? startDate : DateTimeUtil.MIN_DATE,
                        endDate != null ? endDate : DateTimeUtil.MAX_DATE, userId),
                startTime != null ? startTime : LocalTime.MIN,
                endTime != null ? endTime : LocalTime.MAX,
                AuthorizedUser.getCaloriesPerDay()
        );
        model.addAttribute("meals", list);
        return "meals";
    }
}