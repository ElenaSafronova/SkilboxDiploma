package ru.skillbox.diploma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.Dto.RegistrationDto;
import ru.skillbox.diploma.Dto.newUserDataDto;
import ru.skillbox.diploma.exception.EmailExistsException;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

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

    public RegistrationDto registerNewUser(newUserDataDto newUserDataDto) throws EmailExistsException {
        Map<String, String> errors = new HashMap<>();

        if (findUserByEmail(newUserDataDto.getE_mail()) == null){

            //TODO: errors checked

            // errors.put("name", "Имя указано неверно");
//        errors.put("password", "Пароль короче 6-ти символов");
//        errors.put("captcha", "Код с картинки введён неверно");
//            if (errors.size() > 0){
//                return new RegistrationDto(false, errors);
//            }

            User newUser = new User(
                    (byte) 0,
                    newUserDataDto.getName(),
                    newUserDataDto.getE_mail(),
                    new BCryptPasswordEncoder().encode(newUserDataDto.getPassword())
            );
            userRepository.save(newUser);

            logger.debug(newUser.toString());
            return new RegistrationDto(true, null);
        }

        errors.put("email", "Этот e-mail уже зарегистрирован");
        return new RegistrationDto(false, errors);
//        throw new EmailExistsException(newUserDataDto.getE_mail());
    }
}
