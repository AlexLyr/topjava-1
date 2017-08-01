package ru.javawebinar.topjava.repository.jdbc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;


import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcMealRepositoryImpl implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertMeal;
    private final NamedParameterJdbcTemplate namedTemplate;

    @Autowired
    public JdbcMealRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedTemplate) {
        this.insertMeal = new SimpleJdbcInsert(dataSource)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
        this.namedTemplate = namedTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("user_id", userId)
                .addValue("date_time", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories());
        if (meal.isNew()) {
            Number number = insertMeal.executeAndReturnKey(map);
            meal.setId(number.intValue());
        } else {
           int afRows= namedTemplate.update("UPDATE meals SET  user_id=:user_id, date_time=:date_time," +
                    " description=:description,calories=:calories WHERE id=:id AND user_id=:user_id", map);

            if(afRows==0)
                meal=null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> list = jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND user_id=?", /*(resultSet, i) -> {
            int id1 =resultSet.getInt("id");
            LocalDateTime dateTime=resultSet.getTimestamp("date_time").toLocalDateTime();
            String desc=resultSet.getString("description");
            int cal=resultSet.getInt("calories");
            return new Meal(id1,dateTime,desc,cal);
        }*/ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(list);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=?",/*(resultSet, i) -> {
            int id1 =resultSet.getInt("id");
            LocalDateTime dateTime=resultSet.getTimestamp("date_time").toLocalDateTime();
            String desc=resultSet.getString("description");
            int cal=resultSet.getInt("calories");
            return new Meal(id1,dateTime,desc,cal);
        }*/ROW_MAPPER,userId).stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return getAll(userId).stream()
                .filter(m-> DateTimeUtil.isBetween(m.getDateTime(),startDate,endDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }


}
