package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.entities.Document;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    @Autowired
    private UserDaoService userDaoService;

    private static String UPLOADED_FOLDER = "images/";

/*    @GetMapping("/customer/uploadImage")
    public String index() {
        return "upload";
    }*/

    @PostMapping("/uploadImage")
    //@RequestMapping(consumes = "application/json", produces = "application/json")
    public Long uploadImage (@RequestParam("file") MultipartFile file) throws IOException {

        User user = userDaoService.getLoggedInUser();

        if (file.isEmpty()) {
            throw new ResourceNotFoundException("Upload Image");
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            Document document = new Document();
            document.setFilename(file.getOriginalFilename());
            document.setPath(UPLOADED_FOLDER);
            document.setCreatedOn(new Date());
            document.setCreatedBy(user);

            return document.getId();



    /*        redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");*/

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }



}
