package ecom_blog.controller;

import ecom_blog.service.MessageContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/messages")
public class AdminMessageController {

    private final MessageContactService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("messages", service.getAll());
        return "admin/messages";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("message", service.findById(id));
        return "admin/message-details";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        service.delete(id); // ✅ maintenant existe
        return "redirect:/admin/messages";
    }
}
