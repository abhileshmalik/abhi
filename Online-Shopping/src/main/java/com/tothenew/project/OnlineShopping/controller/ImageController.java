package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    private static String UPLOADED_FOLDER = "/images/";

/*    @GetMapping("/customer/uploadImage")
    public String index() {
        return "upload";
    }*/

    @PostMapping("/customer/uploadImage")
    public String userProfileimage (@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new ResourceNotFoundException("Upload Image");
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

    /*        redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");*/

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Upload Successful";

    }



}
