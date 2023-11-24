package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.EmailResponse;
import com.ecommerce.WatchStore.Repositories.EmailResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailResponseService {

    @Autowired
    public EmailResponseRepository emailResponseRepository;

    @Autowired
    EmailService emailService;
    public List<EmailResponse> getAll(){
        return  emailResponseRepository.findAll();
    }

    public void deleteAll(){
        emailResponseRepository.deleteAll();
    }
    public EmailResponse saveEmailResponse(EmailResponse emailResponse)
    {
        System.out.println("Email: "+ emailResponse.getEmail());
            emailService.scheduleEmailSending(0,2, emailResponse.getEmail() );
        return emailResponseRepository.save(emailResponse);
    }
}
