package ru.skillbox.diploma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.dto.RegistrationDto;
import ru.skillbox.diploma.dto.NewUserDataDto;
import ru.skillbox.diploma.exception.EmailExistsException;
import ru.skillbox.diploma.model.Captcha;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CaptchaService captchaService;

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

    public RegistrationDto registerNewUser(NewUserDataDto newUserDataDto) throws EmailExistsException {
        Map<String, String> errors = new HashMap<>();

        if (findUserByEmail(newUserDataDto.getEmail()) == null){
            if (!newUserDataDto.getName().matches("^[A-Za-zА-Яа-я]\\w{2,25}$")){
                errors.put("name", "Имя указано неверно");
            }
            if(newUserDataDto.getPassword().length() < 6){
                errors.put("password", "Пароль короче 6-ти символов");
            }
            Captcha captcha = captchaService.findBySecretCode(newUserDataDto.getCaptchaSecret());
            System.out.println("--------------- captcha " + captcha.toString());
            if (captcha == null){
                logger.debug("captcha Код не найден в БД");
            }
            if (!captcha.getCode().equals(newUserDataDto.getCaptcha())){
                errors.put("captcha", "Код с картинки введён неверно");
            }

            if (errors.size() > 0){
                return new RegistrationDto(false, errors);
            }

            User newUser = new User(
                    (byte) 0,
                    newUserDataDto.getName(),
                    newUserDataDto.getEmail(),
                    new BCryptPasswordEncoder().encode(newUserDataDto.getPassword())
            );
            userRepository.save(newUser);

            logger.debug(newUser.toString());
            return new RegistrationDto(true, null);
        }

        errors.put("email", "Этот e-mail уже зарегистрирован");
        return new RegistrationDto(false, errors);
//        throw new EmailExistsException(newUserDataDto.getEmail());
    }
}
