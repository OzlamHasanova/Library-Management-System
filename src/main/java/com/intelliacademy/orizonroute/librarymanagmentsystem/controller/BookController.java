package com.intelliacademy.orizonroute.librarymanagmentsystem.controller;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.BookNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.BookService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.impl.CategoryServiceImpl;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.CloudinaryService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.impl.AuthorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorServiceImpl authorService;
    private final CategoryServiceImpl categoryService;
    private final CloudinaryService cloudinaryService;

    @GetMapping
    public String listBooks(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "8") int size,
                            @RequestParam(defaultValue = "title") String sortBy,
                            Model model) {
        Page<BookDTO> bookPage = bookService.getAllBooks(page, size, sortBy);

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", bookPage.getNumber());
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("hasNext", bookPage.hasNext());
        model.addAttribute("hasPrevious", bookPage.hasPrevious());
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
