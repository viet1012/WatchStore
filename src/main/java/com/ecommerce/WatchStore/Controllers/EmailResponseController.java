package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.EmailResponse;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.EmailResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/Email-response")
@RestController
public class EmailResponseController {

    @Autowired
    public EmailResponseService emailResponseService;

    @GetMapping("/GetAll")
    public ResponseEntity<ResponseWrapper<List<EmailResponse>>> getAll() {
        List<EmailResponse> emailResponses = emailResponseService.getAll();
        ResponseWrapper<List<EmailResponse>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "List Email retrieved successfully", true, emailResponses);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/DeleteAll")
    public ResponseEntity<ResponseWrapper<String>> deleteAll() {
        emailResponseService.deleteAll();
        ResponseWrapper<String> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Delete Email  successfully", true, "");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/Create")
    public ResponseEntity<ResponseWrapper<EmailResponse>> SaveEmail(@RequestBody EmailResponse emailResponse) {
        EmailResponse newEmailResponse = emailResponseService.saveEmailResponse(emailResponse);
        ResponseWrapper<EmailResponse> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Email retrieved successfully", true, newEmailResponse);
        return ResponseEntity.ok().body(response);
    }


}
