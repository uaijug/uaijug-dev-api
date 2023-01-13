package br.com.uaijug.uaijugdevapi.controller;

import br.com.uaijug.uaijugdevapi.util.PDFGenerator;
import br.com.uaijug.uaijugdevapi.util.QRCodeGenerator;
import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Controller
public class QrCodeController {
    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/static/img/QRCode.png";

    @GetMapping("/qrcode")
    public String getQRCode(Model model) {
        String medium = "https://rogeriofontes.medium.com/";
        String github = "https://github.com/rogeriofontes";
        String associateCode = "https://github.com/rogeriofontes/" + UUID.randomUUID().toString().substring(0, 6);

        byte[] image = new byte[0];
        byte[] image1 = new byte[0];
        try {

            // Generate and Return Qr Code in Byte Array
            image = QRCodeGenerator.getQRCodeImage(medium, 250, 250);

            image1 = QRCodeGenerator.getQRCodeImage(associateCode, 250, 250);

            // Generate and Save Qr Code Image in static/image folder
            QRCodeGenerator.generateQRCodeImage(github, 250, 250, QR_CODE_IMAGE_PATH);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        // Convert Byte Array into Base64 Encode String
        String qrcode = Base64.getEncoder().encodeToString(image);
        String qrcode1 = Base64.getEncoder().encodeToString(image1);

        model.addAttribute("medium", medium);
        model.addAttribute("github", github);
        model.addAttribute("qrcode", qrcode);

        model.addAttribute("associateCode", associateCode);
        model.addAttribute("qrcode1", qrcode1);

        return "qrcode";
    }

    @GetMapping(value = "/export",
            produces = MediaType.APPLICATION_PDF_VALUE)
    //http://localhost:8080/export?text=knowledgefactory
    public ResponseEntity<InputStreamResource> employeeReport
            (@RequestParam(defaultValue = "4DP099#") String text)
            throws IOException, DocumentException, WriterException {
        String associateCode = "https://github.com/rogeriofontes/";

        byte[] pngData = QRCodeGenerator.getQRCodeImage(associateCode + text, 0, 0);

        InputStreamResource inputStreamResource =
                PDFGenerator.InputStreamResource(pngData);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; "
                + "filename=my-file.pdf");

        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(inputStreamResource);
    }
}
