package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private static Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private static Set<Role> set=new HashSet<Role>();
    private static Set<Role> set2=new HashSet<Role>();
    static {
        set.add(Role.ROLE_USER);
        set2.add(Role.ROLE_ADMIN);
        repository.put(1,new User(1,"sasha", "spider@", "1234", 2000, true,set));
        repository.put(2,new User(2,"gleb", "gleb@", "1234556", 2000, true,set2));
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if(user.isNew())
            user.setId(counter.incrementAndGet());
        repository.put(user.getId(),user);
        return user;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> list=new ArrayList<>(repository.values());
        list.sort(Comparator.comparing(User::getName));
        return list;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.values().stream()
                .filter(u->u.getEmail().equals(email))
                .findFirst()
                .orElse(null);


    }
}
