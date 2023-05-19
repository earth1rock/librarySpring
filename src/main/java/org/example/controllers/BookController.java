package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dao.BookDao;
import org.example.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController
{

	private final BookDao bookDao;

	@Autowired
	public BookController(BookDao bookDao)
	{
		this.bookDao = bookDao;
	}

	@GetMapping()
	public String books(Model model)
	{
		model.addAttribute("books", bookDao.books());
		return "/books/books";
	}

	@GetMapping("/new")
	public String newBook(@ModelAttribute("book") Book book)
	{
		return "/books/new";
	}

	@PostMapping("/new")
	public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult)
	{
		if (bindingResult.hasErrors())
			return "redirect:/books/new";

		bookDao.save(book);
		return "redirect:/books";
	}

	@GetMapping("/book/{id}")
	public String bookInfo(@PathVariable("id") int id, Model model)
	{
		model.addAttribute("book", bookDao.getBook(id));
		return "/books/book";
	}
}
