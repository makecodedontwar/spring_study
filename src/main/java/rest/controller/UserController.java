package rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import rest.domain.User;
import rest.persistance.Storage;

import java.util.List;


@RestController
public class UserController {

    private Storage storage;

    private KafkaTemplate<String, User> kafkaTemplate;

    @Autowired
    public UserController(Storage storage, KafkaTemplate<String, User> kafkaTemplate) {
        this.storage = storage;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return storage.getAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return storage.getUser(id);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        storage.delete(id);
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody User user) {
        kafkaTemplate.send("user", user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity updateUser(@RequestBody User user, @PathVariable int id) {
        User storageUser = storage.getUser(id);
        if (storageUser == null) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        storage.save(user);
        return ResponseEntity.ok().build();
    }
}
