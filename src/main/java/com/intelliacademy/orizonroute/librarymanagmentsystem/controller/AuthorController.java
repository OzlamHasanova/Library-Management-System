package com.intelliacademy.orizonroute.librarymanagmentsystem.controller;

import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Author;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public String listAuthors(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        return "author/list";
    }

    @GetMapping("/new")
    public String createAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "author/create";
    }

    @PostMapping("/new")
    public String saveAuthor(@ModelAttribute("author") Author author) {
        authorService.saveAuthor(author);
        return "redirect:/list";
    }

    @GetMapping("/edit/{id}")
    public String editAuthorForm(@PathVariable Long id, Model model) {
        Optional<Author> author = authorService.getAuthorById(id);
        if (author.isPresent()) {
            model.addAttribute("author", author.get());
            return "author/edit";
        } else {
            return "404";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateAuthor(@PathVariable Long id, @ModelAttribute("author") Author author) {
        author.setId(id); // Ensure the correct ID is set for update
        authorService.saveAuthor(author);
        return "redirect:author/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return "redirect:author/list";
    }


}
