package rest.persistance;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import rest.domain.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class Storage {
    private Map<Integer, User> users;

    public Storage() {
        users = new HashMap<>();
        users.put(1, new User(1, "Victor"));
        users.put(2, new User(2, "Petr"));
        users.put(3, new User(3, "Ivan"));
    }

    @KafkaListener(topics = "user", groupId = "users")
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public void delete(int id) {
        users.remove(id);
    }

    public User getUser(int id) {
        return users.get(id);
    }

    public List<User> getAll() {
        return users
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
