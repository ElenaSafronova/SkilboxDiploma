package ru.skillbox.diploma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.Dto.RegistrationDto;
import ru.skillbox.diploma.Dto.newUserDataDto;
import ru.skillbox.diploma.controller.ApiAuthController;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    private User save(User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    private User findByName(String name){
        return userRepository.findByName(name);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByPassword(String password) {
        return userRepository.findByPassword(password);
    }

    public User findUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public User findById(int i) {
        return userRepository.findById(i);
    }

    public RegistrationDto registerNewUser(newUserDataDto newUserDataDto) {
        if (findUserByEmail(newUserDataDto.getE_mail()) == null){
            User newUser = new User(
                    (byte) 0,
                    newUserDataDto.getName(),
                    newUserDataDto.getE_mail(),
                    newUserDataDto.getPassword());
            userRepository.save(newUser);
            logger.debug(newUser.toString());
            return new RegistrationDto(true, null);
        }
        Map<String, String> errors = new HashMap<>();
        errors.put("email", "Этот e-mail уже зарегистрирован");
        errors.put("name", "Имя указано неверно");
        errors.put("password", "Пароль короче 6-ти символов");
        errors.put("captcha", "Код с картинки введён неверно");
        return new RegistrationDto(false, errors);
    }
}
