package ru.javawebinar.topjava.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

public class MealWithExceed extends BaseTo implements Serializable {
    private static final long SERIAL_VERSION_UID = 1L;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    private LocalDateTime dateTime;

    @NotBlank
    private String description;
    @NotNull
    private int calories;

    @NotNull
    private boolean exceed;

    public MealWithExceed() {

    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setExceed(boolean exceed) {
        this.exceed = exceed;
    }

    public MealWithExceed(@JsonProperty("id") Integer id,
                          @JsonProperty("dateTime") LocalDateTime dateTime,
                          @JsonProperty("description") String description,
                          @JsonProperty("calories") int calories,
                          @JsonProperty("exceed") boolean exceed) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.exceed = exceed;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExceed() {
        return exceed;
    }

    @Override
    public String toString() {
        return "MealWithExceed{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", exceed=" + exceed +
                '}';
    }
}