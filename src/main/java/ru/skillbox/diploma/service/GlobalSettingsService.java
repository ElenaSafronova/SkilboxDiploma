package ru.skillbox.diploma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.model.GlobalSetting;
import ru.skillbox.diploma.repository.GlobalSettingRepository;
import ru.skillbox.diploma.value.GlobalSettingValue;

import java.util.*;


@Service
public class GlobalSettingsService {
    @Autowired
    private GlobalSettingRepository globalSettingRepository;

    public GlobalSetting findById(int id){
        return globalSettingRepository.findById(id);
    }

    public List<GlobalSetting> findAll(){
        return (List<GlobalSetting>) globalSettingRepository.findAll();
    }

    public GlobalSetting findByCode(String codeName) {
        return globalSettingRepository.findByCode(codeName);
    }

    public GlobalSetting findByName(String name) {
        return globalSettingRepository.findByName(name);
    }

    public Map<String, Boolean> getSettings() {
        Map<String, Boolean> settings = new HashMap<>();
        Iterator<GlobalSetting> iter = globalSettingRepository.findAll().iterator();
        while(iter.hasNext())
        {
            GlobalSetting curSettings = iter.next();
            if (curSettings.getValue().equals(GlobalSettingValue.YES.name())) {
                settings.put(curSettings.getCode(), true);
            } else if (curSettings.getValue().equals(GlobalSettingValue.NO.name())) {
                settings.put(curSettings.getCode(), false);
            }
        }
        return settings;
    }

    public void setSettings(Map<String, Boolean> settings) {
        settings.forEach((k, v) -> {
//            System.out.println("Key: " + k + " Value: " + v);
            GlobalSetting curSetting = findByCode(k);
            if (v.equals(true)) {
                curSetting.setValue(GlobalSettingValue.YES.name());
            } else {
                curSetting.setValue(GlobalSettingValue.NO.name());
            }
            globalSettingRepository.save(curSetting);
        });
    }

}
