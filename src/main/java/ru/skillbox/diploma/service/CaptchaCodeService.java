package ru.skillbox.diploma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.model.CaptchaCode;
import ru.skillbox.diploma.repository.CaptchaCodeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CaptchaCodeService {
    @Autowired
    private CaptchaCodeRepository captchaCodeRepository;

    public CaptchaCode save(CaptchaCode captcha){
        return captchaCodeRepository.save(captcha);
    }

    public Optional<CaptchaCode> findById(int id){
        return captchaCodeRepository.findById(id);
    }

    public List<CaptchaCode> findAll(){
        return (List<CaptchaCode>) captchaCodeRepository.findAll();
    }
}
