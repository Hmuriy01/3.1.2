package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.configs.UsersCreate;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        UsersCreate usersCreate = new UsersCreate(userRepository);
        usersCreate.createUsers();
    }

    @Transactional
    public User findUserByUserName(String username) {

        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {

        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }
        return user;
    }

    public Optional<User> findById(long id) {

        return userRepository.findById(id);
    }

    public void save(User user) {
        User extracted = userRepository.findByUsername(user.getUsername());
        if (extracted != null) {
            throw new IllegalArgumentException("User already exists!");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

    public void edit(User user) {
        User extracted = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("Edited User not exists!"));
        extracted.setUsername(user.getUsername());
        extracted.setAge(user.getAge());
        extracted.setEmail(user.getEmail());
        extracted.setRoles(user.getRoles());
        if (!passwordEncoder.matches(extracted.getPassword(), user.getPassword())) {
            extracted.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    public void remove(long id) {
        userRepository.deleteById(id);
    }
}
