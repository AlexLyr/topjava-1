package ru.javawebinar.topjava.web.user;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.User;

@Controller
public class ProfileRestController extends AbstractUserController {
    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User get() {
        return super.get(userId);
    }

    public void delete() {
        super.delete(userId);
    }

    public void update(User user) {
        super.update(user, userId);
    }
}