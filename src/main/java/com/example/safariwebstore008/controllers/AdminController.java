package com.example.safariwebstore008.controllers;

import com.example.safariwebstore008.dto.ProductDto;
import com.example.safariwebstore008.models.Product;
import com.example.safariwebstore008.models.User;
import com.example.safariwebstore008.services.AdminService;
import com.example.safariwebstore008.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping(path = "/api/admin/")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @RequestMapping(value = "registerDispatchRider", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody User dispatchRider) throws Exception {
        return ResponseEntity.ok(adminService.createDispatchRider(dispatchRider));
    }

    @RequestMapping(value = "createProduct", method = RequestMethod.POST)
    public ResponseEntity<?> createProduct(@RequestBody ProductDto product) {
        return ResponseEntity.ok(adminService.createProduct(product));
    }

    @RequestMapping(value = "uploadImage", method = RequestMethod.POST)
    public ResponseEntity<?> uploadImage(@RequestPart MultipartFile image, @RequestParam("id") Product product) {
        String url = cloudinaryService.uploadFile(image);
        Long productId = product.getId();
        return ResponseEntity.ok(adminService.saveToDB(url, productId));
    }

    @RequestMapping(value = "updateProduct/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDto product) {
        return ResponseEntity.ok(adminService.updateProduct(id, product));
    }

    @RequestMapping(value = "deleteProduct/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
