package ru.skillbox.diploma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.model.Captcha;
import ru.skillbox.diploma.repository.CaptchaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CaptchaService {
    @Autowired
    private CaptchaRepository captchaRepository;

    public Captcha save(Captcha captcha){
        return captchaRepository.save(captcha);
    }

    public Optional<Captcha> findById(int id){
        return captchaRepository.findById(id);
    }

    public List<Captcha> findAll(){
        return (List<Captcha>) captchaRepository.findAll();
    }

    public void deleteCaptcha(LocalDateTime time) {
        captchaRepository.deleteBooks(time);
    }
}
