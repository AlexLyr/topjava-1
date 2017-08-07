package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.DELETE, query = "DELETE from Meal m where m.id=:id AND m.user.id=:user_id"),
        @NamedQuery(name = Meal.GET_BETWEEN, query = "SELECT m FROM Meal m where  m.user.id=:user_id and m.dateTime>:startDate and m.dateTime<:endDate order by m.dateTime desc "),
        @NamedQuery(name = Meal.GET_ALL, query = "SELECT m from Meal m where m.user.id=:user_id order by m.dateTime desc "),
        @NamedQuery(name = Meal.GET, query = "SELECT m from Meal m where m.id=:id AND m.user.id=:user_id"),
        @NamedQuery(name = Meal.UPDATE, query = "UPDATE Meal m set m.dateTime=:dt,m.description=:dc,m.calories=:c WHERE m.id=:id and m.user.id=:user_id")
})

@Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "DATE_TIME"}, name = "MEALS_UNIQUE_USER_DATETIME_IDX")})
public class Meal extends BaseEntity {
    public static final String DELETE = "Meal.delete";
    public static final String GET_BETWEEN = "Meal.getBetween";
    public static final String GET_ALL = "Meal.getAll";
    public static final String GET = "Meal.get";
    public static final String UPDATE = "Meal.update";
    @Column(name = "DATE_TIME")
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "DESCRIPTION")
    @NotBlank
    private String description;
    @Column(name = "CALORIES")
    @NotNull
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
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

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
