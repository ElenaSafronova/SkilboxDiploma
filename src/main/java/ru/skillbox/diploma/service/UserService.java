package ru.skillbox.diploma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.model.entity.User;
import ru.skillbox.diploma.repository.UserRepository;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public User save(User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public User findByName(String name){
        return userRepository.findByName(name);
    }

    public User findUserByEmail(String email) {
        logger.info("findUserByEmail " + email);
        return userRepository.findByEmail(email);
    }

    public User findUserByPassword(String password) {
        return userRepository.findByPassword(password);
    }

    public User findUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public User findById(int id) {
        return userRepository.findById(id);
    }

    public User findByCode(String code) {
        return userRepository.findByCode(code);
    }

    public void updateUserName(int id, String name) {
        userRepository.updateName(id, name);
    }

}
