package ecom_blog.service;

import ecom_blog.model.Setting;
import ecom_blog.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;

    public String getSetting(String key, String defaultValue) {
        return settingRepository.findByKey(key)
                .map(Setting::getValue)
                .orElse(defaultValue);
    }

    public boolean isMaintenanceMode() {
        return "true".equalsIgnoreCase(getSetting("maintenance_mode", "false"));
    }

    @Transactional
    public void setSetting(String key, String value) {
        Setting setting = settingRepository.findByKey(key)
                .orElse(new Setting(key, value));
        setting.setValue(value);
        settingRepository.save(setting);
    }

    @Transactional
    public void setMaintenanceMode(boolean active) {
        setSetting("maintenance_mode", String.valueOf(active));
    }
}
