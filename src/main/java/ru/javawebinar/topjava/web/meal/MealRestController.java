package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;


@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProfileRestController profileRestController;

    @Autowired
    private MealService service;


    public List<MealWithExceed> getAll() {
        log.info("getAll");
        return MealsUtil.getWithExceeded(service.getAll(profileRestController.get().getId()), 2000);
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id,profileRestController.get().getId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        try {
            checkNew(meal);
        }
        catch (IllegalArgumentException e){
            update(meal,meal.getId());
        }
        return service.save(meal, profileRestController.get().getId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, profileRestController.get().getId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        checkIdConsistent(meal, id);
        service.update(meal, profileRestController.get().getId());
    }
}