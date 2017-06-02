package com.askdog.web.utils;

import com.askdog.common.exception.CommonException;
import com.askdog.common.utils.QRCodeGenerator;
import com.askdog.web.exception.WebException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.askdog.web.exception.WebException.Error.QR_CODE_GENERATE_FAILED;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

public class QrCodeWriter {

    public static void writeQrCode(HttpServletResponse response, int width, int height, String codeUrl) throws WebException {
        try {
            QRCodeGenerator.QRCodeImage qrImage = QRCodeGenerator.generate(codeUrl, width, height);
            response.setContentType(IMAGE_PNG_VALUE);
            qrImage.writeToStream("png", response.getOutputStream());
        } catch (CommonException | IOException e) {
            throw new WebException(QR_CODE_GENERATE_FAILED, e);
        }
    }

}
