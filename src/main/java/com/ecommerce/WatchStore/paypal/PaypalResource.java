package com.ecommerce.WatchStore.paypal;


 import com.ecommerce.WatchStore.DTO.BillDTO;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.stereotype.Service;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.RestController;

 import com.paypal.api.payments.Links;
 import com.paypal.api.payments.Payment;
 import com.paypal.base.rest.PayPalRESTException;

 @RestController
 @RequestMapping("/api/Paypal")
 public class PaypalResource {

     @Autowired
     private PaypalService paypalService;

     @PostMapping("pay")
     public ResponseEntity<?> createPayment(@RequestBody BillDTO order) {
         try {
             Double totalPrice = (double) order.getTotalPrice();
             Payment payment = paypalService.createPayment(totalPrice, "USD", "Paypal",
                     "ORDER", order.getDescription(), "http://localhost:8080/api/Paypal/cancel",
                     "http://localhost:8080/api/Paypal/success");
             for(Links link: payment.getLinks()) {
                 if(link.getRel().equals("approval_url")) {
                     return ResponseEntity.ok(link.getHref());
                 }
             }
         } catch (PayPalRESTException e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating payment.");
         }
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment approval URL not found.");
     }

     @GetMapping("cancel")
     public ResponseEntity<String> cancelPay() {
         return ResponseEntity.ok("Payment canceled.");
     }

     @GetMapping("success")
     public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
         try {
             Payment payment = paypalService.executePayment(paymentId, payerId);
             if (payment.getState().equals("approved")) {
                 return ResponseEntity.ok("Payment successful.");
             }
         } catch (PayPalRESTException e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error executing payment.");
         }
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment not approved.");
     }
 }