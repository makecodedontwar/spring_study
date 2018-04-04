package rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rest.domain.User;
import rest.persistance.Storage;

import java.net.URI;
import java.util.List;


@RestController
public class UserController {

    private Storage storage;

    @Autowired
    public UserController(Storage storage) {
        this.storage = storage;
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
        User save = storage.save(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(save.getId()).toUri();

        return ResponseEntity.created(uri).build();
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
