package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.entities.ImageDocument;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.BadRequestException;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.repos.ProductRepository;
import com.tothenew.project.OnlineShopping.services.ImageDocumentService;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;

@Api(value="Image Controller APIs")
@RestController
public class ImageController {

    @Autowired
    private UserDaoService userDaoService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageDocumentService imageDocumentService;


    private static String UPLOADED_FOLDER = "/home/abhilesh/Documents/Project/Online-Shopping/images/";

/*    @GetMapping("/customer/uploadImage")
    public String index() {
        return "upload";
    }*/

    @ApiOperation(value = "Upload user profile picture")
    @PostMapping("/profile/uploadImage")
    //@RequestMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> uploadProfileImage(@RequestParam("file") MultipartFile file) throws IOException {

        User user = userDaoService.getLoggedInUser();
        Long uid = user.getUser_id();

        if (file.isEmpty()) {
            throw new ResourceNotFoundException("Upload Image");
        }

        try {
            String fileExtension = null;

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();

            fileExtension = file.getOriginalFilename().split("\\.")[1];

            if (fileExtension.equals("jpeg") || fileExtension.equals("jpg")
                    || fileExtension.equals("png") || fileExtension.equals("bmp")) {
                Path path = Paths.get(UPLOADED_FOLDER + "/users/" + uid + "." + fileExtension);
                Files.write(path, bytes);

                ImageDocument imageDocument = new ImageDocument();
                imageDocument.setFilename(uid.toString() + "." + fileExtension);
                imageDocument.setPath(path.toString());
                imageDocument.setCreatedOn(new Date());
                imageDocument.setCreatedBy(user);

                String message = imageDocumentService.saveImage(imageDocument, uid);

                return new ResponseEntity<>(message, HttpStatus.CREATED);
            } else {
                throw new BadRequestException("Invalid file format, Kindly use .jpg, .jpeg, png, .bmp format" +
                        " for uploading image");
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }


    @ApiOperation(value = "Upload Product picture by product seller")
    @PostMapping("/product/uploadImage/{pid}")
    public ResponseEntity<Object> uploadProductImage(@RequestParam("file") MultipartFile file,
                                                     @PathVariable Long pid) throws IOException {

        if (file.isEmpty()) {
            throw new ResourceNotFoundException("Upload Image");
        }

        User seller = userDaoService.getLoggedInUser();
        Long sellerid = seller.getUser_id();

        Optional<Product> optionalProduct = productRepository.findById(pid);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            Long sid = product.getSeller().getUser_id();

            if (sid.equals(sellerid)) {
                try {
                    String fileExtension = null;

                    // Get the file and save it somewhere
                    byte[] bytes = file.getBytes();

                    fileExtension = file.getOriginalFilename().split("\\.")[1];

                    if (fileExtension.equals("jpeg") || fileExtension.equals("jpg")
                            || fileExtension.equals("png") || fileExtension.equals("bmp")) {

                        Path path = Paths.get(UPLOADED_FOLDER + "/products/" + pid + "." + fileExtension);
                        Files.write(path, bytes);

                        ImageDocument imageDocument = new ImageDocument();
                        imageDocument.setFilename(pid.toString() + "." + fileExtension);
                        imageDocument.setPath(path.toString());
                        imageDocument.setCreatedOn(new Date());
                        imageDocument.setCreatedBy(seller);

                        String message = imageDocumentService.saveProductImage(imageDocument, pid);

                        return new ResponseEntity<>(message, HttpStatus.CREATED);

                    } else {
                        throw new BadRequestException("Invalid file format, Kindly use .jpg, .jpeg, png, .bmp format" +
                                " for uploading image");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }

            } else {
                    throw new BadRequestException("Product not associated to Logged in Seller");
                }

        } else {
            throw new ResourceNotFoundException("Product not found with requested ID");
        }
    }


}
