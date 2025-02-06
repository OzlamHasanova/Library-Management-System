package com.intelliacademy.orizonroute.librarymanagmentsystem.controller;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.BookNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.AuthorService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.BookService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.CategoryService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;

    @GetMapping
    public String listBooks(Model model) {
        List<BookDTO> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books/list";
    }

    @GetMapping("/new")
    public String showCreateBookForm(Model model) {
        model.addAttribute("book", new BookDTO());
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "books/create";
    }

    @PostMapping("/new")
    public String createBook(@ModelAttribute("book") BookDTO bookDTO,
                             @RequestParam("imageFile") MultipartFile file) {
        if (!file.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(file);
            bookDTO.setImage(imageUrl);
        }
        bookService.createBook(bookDTO);
        return "redirect:/books";
    }


    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        try {
            BookDTO book = bookService.getBookById(id);
            model.addAttribute("book", book);
            return "books/view";
        } catch (BookNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("edit/{id}")
    public String showUpdateBookForm(@PathVariable Long id, Model model) {
        try {
            BookDTO book = bookService.getBookById(id);
            model.addAttribute("id");
            model.addAttribute("book", book);
            return "books/update";
        } catch (BookNotFoundException e) {
            return "error/404";
        }
    }

    @PostMapping("edit/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute("book") BookDTO bookDTO) {
        try {
            bookService.updateBook(id, bookDTO);
            return "redirect:/books";
        } catch (BookNotFoundException e) {
            return "error/404";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return "redirect:/books";
        } catch (BookNotFoundException e) {
            return "error/404";
        }
    }

}
