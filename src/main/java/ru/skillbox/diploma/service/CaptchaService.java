package ru.skillbox.diploma.service;

import com.github.cage.GCage;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.controller.ApiAuthController;
import ru.skillbox.diploma.model.Captcha;
import ru.skillbox.diploma.repository.CaptchaRepository;
import ru.skillbox.diploma.responce.CaptchaResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CaptchaService {
    @Autowired
    private CaptchaRepository captchaRepository;

    Logger logger = LoggerFactory.getLogger(CaptchaService.class);

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

    public CaptchaResponse generateCaptcha() throws IOException  {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        String captchaResponseStarting = "data:image/png;base64, ";
        int captchaW = 150;
        int captchaH = 35;
        int oldCaptchaHours = 1;

        DeleteCaptchaOlderThan(LocalDateTime.now()
                .minusHours(oldCaptchaHours)); // метод должен удалять устаревшие капчи из таблицы

        GCage gCage = new GCage();
        String code = gCage.getTokenGenerator().next();
        String uniqueKey = UUID.randomUUID().toString();
        String formatName = gCage.getFormat();

        BufferedImage image = resize(gCage.drawImage(code), captchaW, captchaH);
        ImageIO.write(image, formatName, os);
        String encodedString = Base64.getEncoder().encodeToString(os.toByteArray());

        save(new Captcha(code, uniqueKey));
        logger.trace("New DB element. Table captcha_codes: " + code +
                " secretCode: " + uniqueKey);

        return new CaptchaResponse(uniqueKey,
                captchaResponseStarting + encodedString);
    }

    private void DeleteCaptchaOlderThan(LocalDateTime time) {
        deleteCaptcha(time);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) throws IOException {
        return Thumbnails.of(img).size(newW, newH).asBufferedImage();
    }
}
