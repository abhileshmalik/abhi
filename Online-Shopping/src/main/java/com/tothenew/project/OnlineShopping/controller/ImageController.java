package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.entities.ImageDocument;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.BadRequestException;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.repos.ProductRepository;
import com.tothenew.project.OnlineShopping.repos.ProductVariationRepository;
import com.tothenew.project.OnlineShopping.services.ImageDocumentService;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

@Api(value="Image Controller APIs")
@RestController
public class ImageController {

    @Autowired
    private UserDaoService userDaoService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private ImageDocumentService imageDocumentService;

    Logger logger = LoggerFactory.getLogger(ImageController.class);


    private static String UPLOADED_FOLDER = "/home/abhilesh/Documents/Project/Online-Shopping/images/";


    @ApiOperation(value = "Upload user profile picture")
    @PostMapping("/profile/uploadImage")
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

                ImageDocument imageDocument = new ImageDocument(file.getOriginalFilename(),file.getContentType(),file.getBytes());
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


                        File newPath = new File(UPLOADED_FOLDER + "/products/" + pid);
                        if (!newPath.exists()) {
                            if (newPath.mkdir()) {
                               logger.info("********** New Directory Created for Product **********");
                            } else {
                                logger.error("********** Failed to create directory for Product **********");
                            }
                        }

                        Path path = Paths.get(newPath.toString() + "/" + pid + "." + fileExtension);
                        Files.write(path, bytes);

                        ImageDocument imageDocument = new ImageDocument(file.getOriginalFilename(),file.getContentType(),file.getBytes());
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

    @ApiOperation(value = "Upload Product-Variation picture by product seller")
    @PostMapping("/product/variant/uploadImage/{vid}")
    public ResponseEntity<Object> uploadProductVariationImage(@RequestParam("file") MultipartFile file,
                                                              @PathVariable Long vid) throws IOException {

        if (file.isEmpty()) {
            throw new ResourceNotFoundException("Upload Image");
        }

        User seller = userDaoService.getLoggedInUser();
        Long sellerid = seller.getUser_id();

        Optional<ProductVariation> optionalProductVariation = productVariationRepository.findById(vid);
        if (optionalProductVariation.isPresent()) {

            ProductVariation productVariation = optionalProductVariation.get();
            Long pid = productVariation.getProduct().getProduct_id();
            Long sid = productVariation.getProduct().getSeller().getUser_id();

            Optional<Product> optionalProduct = productRepository.findById(pid);
            if (optionalProduct.isPresent()) {

                if (sid.equals(sellerid)) {

                    try {
                        String fileExtension = null;

                        // Get the file and save it somewhere
                        byte[] bytes = file.getBytes();
                        fileExtension = file.getOriginalFilename().split("\\.")[1];

                        if (fileExtension.equals("jpeg") || fileExtension.equals("jpg")
                                || fileExtension.equals("png") || fileExtension.equals("bmp")) {

                            File newPath = new File(UPLOADED_FOLDER + "/products/" + pid + "/variations");
                            if (!newPath.exists()) {
                                if (newPath.mkdir()) {
                                    logger.info("********** New Directory Created for Product-Variation **********");
                                } else {
                                    logger.error("********** Failed to create directory for Product-Variation **********");
                                }
                            }

                            Path path = Paths.get(newPath.toString() + "/" + vid + "." + fileExtension);
                            Files.write(path, bytes);

                            ImageDocument imageDocument = new ImageDocument(file.getOriginalFilename(), file.getContentType(), file.getBytes());
                            imageDocument.setFilename(vid.toString() + "." + fileExtension);
                            imageDocument.setPath(path.toString());
                            imageDocument.setCreatedOn(new Date());
                            imageDocument.setCreatedBy(seller);

                            String message = imageDocumentService.saveProductVariationImage(imageDocument, vid);

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
        } else {
            throw new ResourceNotFoundException("Invalid Product-Variation Id");
        }
    }


    @ApiOperation(value = "Download user profile picture")
    @GetMapping("/download/ProfileImage")
    public ResponseEntity<Resource> downloadProfileImage() {

        User user = userDaoService.getLoggedInUser();
        Long imageid = user.getProfileImageId();

        // Load file from database
        ImageDocument imageDocument = imageDocumentService.downloadUserImage(imageid);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageDocument.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageDocument.getFilename() + "\"")
                .body(new ByteArrayResource(imageDocument.getData()));
    }


    @ApiOperation(value = "Download a Product Image")
    @GetMapping("/download/product/image/{pid}")
    public ResponseEntity<Resource> downloadProductImage(@PathVariable Long pid) {

        Optional<Product> optionalProduct = productRepository.findById(pid);
        if(optionalProduct.isPresent()) {

            Product product = optionalProduct.get();
            if (product.getIsActive() && !product.getDeleted()) {

                Long imageid = product.getImageId();
                if (imageid==null){
                    throw new ResourceNotFoundException("Image Not found for the requested product");
                }

                Long sellerid = product.getSeller().getUser_id();

                User user = userDaoService.getLoggedInUser();
                Long sid = user.getUser_id();
                String role = user.getRole();

                if (role.equals("ROLE_SELLER"))
                {
                    if (sid.equals(sellerid)) {

                        // Load file from database
                        ImageDocument imageDocument = imageDocumentService.downloadUserImage(imageid);

                        return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(imageDocument.getFileType()))
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageDocument.getFilename() + "\"")
                                .body(new ByteArrayResource(imageDocument.getData()));

                    }
                    else {
                        throw new BadRequestException("You are not authorized to view this Product");
                    }
                } else {

                    // Load file from database
                    ImageDocument imageDocument = imageDocumentService.downloadUserImage(imageid);

                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(imageDocument.getFileType()))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageDocument.getFilename() + "\"")
                            .body(new ByteArrayResource(imageDocument.getData()));

                    }

            } else {
                throw new ResourceNotFoundException("Product is unavailable at the moment");
            }
        } else {
            throw new ResourceNotFoundException("Invalid Product ID");
        }

    }


    @ApiOperation(value = "Download a Product-Variation Image")
    @GetMapping("/download/product/variation/image/{vid}")
    public ResponseEntity<Resource> downloadProductVariationImage(@PathVariable Long vid) {

        Optional<ProductVariation> optionalProductVariation = productVariationRepository.findById(vid);

        if (optionalProductVariation.isPresent()) {

            ProductVariation productVariation = optionalProductVariation.get();

            if (productVariation.getIs_active()) {

                Long imageid = productVariation.getImageId();
                if (imageid == null) {
                    throw new ResourceNotFoundException("Image Not found for the requested Product-Variation");
                }

                Long sellerid = productVariation.getProduct().getSeller().getUser_id();

                User user = userDaoService.getLoggedInUser();
                Long sid = user.getUser_id();
                String role = user.getRole();

                if (role.equals("ROLE_SELLER"))
                {
                    if (sid.equals(sellerid)) {

                        // Load file from database
                        ImageDocument imageDocument = imageDocumentService.downloadUserImage(imageid);

                        return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(imageDocument.getFileType()))
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageDocument.getFilename() + "\"")
                                .body(new ByteArrayResource(imageDocument.getData()));

                    }
                    else {
                        throw new BadRequestException("You are not authorized to view this Product Variation");
                    }
                } else {

                    // Load file from database
                    ImageDocument imageDocument = imageDocumentService.downloadUserImage(imageid);

                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(imageDocument.getFileType()))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageDocument.getFilename() + "\"")
                            .body(new ByteArrayResource(imageDocument.getData()));

                }

            } else {
                throw new ResourceNotFoundException("Product-Variation is unavailable at the moment");
            }

        } else {
            throw new ResourceNotFoundException("Invalid Product-Variation Id");
        }
    }

}
