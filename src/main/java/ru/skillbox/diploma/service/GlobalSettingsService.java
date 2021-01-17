package ru.skillbox.diploma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.model.GlobalSetting;
import ru.skillbox.diploma.repository.GlobalSettingRepository;

import java.util.List;
import java.util.Optional;


@Service
public class GlobalSettingsService {
    @Autowired
    private GlobalSettingRepository globalSettingRepository;


    public Optional<GlobalSetting> findById(int id){
        return globalSettingRepository.findById(id);
    }

    public List<GlobalSetting> findAll(){
        return (List<GlobalSetting>) globalSettingRepository.findAll();
    }
}
