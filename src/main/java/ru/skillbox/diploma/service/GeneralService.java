package ru.skillbox.diploma.service;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diploma.dto.RegistrationDto;
import ru.skillbox.diploma.model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeneralService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegistrationDto changeProfile(String name,
                                         String email,
                                         String password)
    {
        LOGGER.trace("uploadProfile: " + name + " " + email + " " + password);
        Map<String, String> errors = checkErrors(name, email, password, null);
        if (errors.size() > 0){
            return new RegistrationDto(false, errors);
        }
        changeProfileData(name, email, password, null);
        return new RegistrationDto(true, null);
    }

    public RegistrationDto changeProfileWithPhoto(String name,
                                                  String email,
                                                  String password,
                                                  MultipartFile photo)
    {
        LOGGER.trace("uploadProfileWithPhoto: " + photo.getOriginalFilename() + " "
                + photo.getSize() + photo.getContentType());
        LOGGER.trace(name + " " + email + " " + password);
        Map<String, String> errors = checkErrors(name, email, password, photo);
        if (errors.size() > 0){
            return new RegistrationDto(false, errors);
        }
        changeProfileData(name, email, password, photo);
        return new RegistrationDto(true, null);
    }

    private Map<String, String> checkErrors(String name, String email,
                                            String password, MultipartFile photo) {
        Map<String, String> errors = new HashMap<>();
        if (email != null && userService.findUserByEmail(email) != null) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if(photo != null) {
            LOGGER.debug("photo's size is " + photo.getSize() * 0.00000095367432 + " MB");
            if (photo.getSize() * 0.00000095367432 > 5_000_000) {
                errors.put("photo", "Фото слишком большое, нужно не более 5 Мб");
            }
        }
        if(name != null && !name.matches("^([a-zA-ZА-Яа-я]{2,}\\s?([a-zA-ZА-Яа-я]{2,})?)")){
            errors.put("name", "Имя указано неверно");
        }
        if(password != null && password.length() < 6){
            errors.put("password", "Пароль короче 6-ти символов");
        }
        return errors;
    }

    private void changeProfileData(String name, String email, String password, MultipartFile photo) {
        boolean isChanged = false;
        User curUser = authService.getCurUser();
        int userId = curUser.getId();
        LOGGER.info("changeProfileData for userId " + userId);
        if (name != null){
            if(!name.trim().equals(curUser.getName())){
                LOGGER.trace("userId " + userId + "change name from " + curUser.getName() + " to " + name);
                curUser.setName(name.trim());
                isChanged = true;
            }
        }
        if (email != null) {
            if (!email.trim().equals(curUser.getEmail())) {
                LOGGER.trace("userId " + userId + "change email from " + curUser.getEmail() + " to " + email);
                curUser.setEmail(email.trim());
                if (!isChanged){
                    isChanged = true;
                }
            }
        }
        if (password != null) {
                LOGGER.trace("userId " + userId + "change password from " + curUser.getPassword() + " to " + password);
                curUser.setPassword(passwordEncoder.encode(password));
                if (!isChanged){
                    isChanged = true;
                }
        }
        if(photo != null){
            LOGGER.trace("userId " + userId + " upload photo" + photo.getOriginalFilename());
            if (!isChanged){
                isChanged = true;
            }
            uploadPhoto(curUser, photo);
        }
        if (isChanged) {
            userService.save(curUser);
        }
    }

    private void uploadPhoto(User curUser, MultipartFile photoFile) {
        String fileName = StringUtils.cleanPath(photoFile.getOriginalFilename());
        String uploadDir = "src/main/resources/static/img/userPhoto/" + curUser.getId();
        saveFile(uploadDir, fileName, photoFile);
        curUser.setPhoto(fileName);
    }

    private boolean saveFile(String uploadDir, String fileName, MultipartFile multipartFile) {
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            try {
                Files.createDirectories(uploadPath);
            }catch (IOException ioex){
//                throw new IOException("Could not create directory: " + uploadPath, ioex);
                ioex.printStackTrace();
            }
        }

        try(InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Files.copy " + filePath.toString());
//            File imgPath = new File(filePath.toString());
//            BufferedImage img = ImageIO.read(imgPath);
//            BufferedImage scaledImg  = resizeImage(img, 36, 36);
//            String fileExtension = fileName.substring(fileName.indexOf('H'));
//            ImageIO.write(scaledImg, fileExtension, imgPath);
//            LOGGER.info("resizeImage " + filePath.toString());
        }
        catch (IOException ioex){
//            throw new IOException("Could not save image file: " + fileName, ioex);
            ioex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth,
                                      int targetHeight) throws Exception {
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC,
                Scalr.Mode.AUTOMATIC, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }

}
