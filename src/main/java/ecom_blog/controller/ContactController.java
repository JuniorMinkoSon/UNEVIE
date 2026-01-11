package ecom_blog.controller;

import ecom_blog.model.MessageContact;
import ecom_blog.service.MessageContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final MessageContactService service;

    @PostMapping("/contact/send")
    public String envoyer(@ModelAttribute MessageContact message) {

        service.save(message); // sauvegarde en BDD

        return "redirect:/confirmation"; // redirection propre
    }
}
