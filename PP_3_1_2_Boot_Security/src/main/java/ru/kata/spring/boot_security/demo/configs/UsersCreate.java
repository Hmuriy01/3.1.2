package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class UsersCreate {
    private final UserRepository userRepository;

    @Autowired
    public UsersCreate(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUsers() {
        Set<Role> temp = new HashSet<>();
        Role role = new Role("ADMIN");
        temp.add(role);
        role = new Role("USER");
        temp.add(role);
        User admin = new User("admin", 20, "admin@gmail.com",
                "admin", temp);
        admin.setPassword(new BCryptPasswordEncoder().encode(admin.getPassword()));
        userRepository.save(admin);
        temp.clear();
        role = new Role("USER");
        temp.add(role);
        User user = new User("user", 20, "user@gmail.com",
                "user", temp);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }
}
