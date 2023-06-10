package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dao.BookDao;
import org.example.dao.PersonDao;
import org.example.models.Book;
import org.example.models.Person;
import org.example.util.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/books")
public class BookController
{

	private final BookDao bookDao;
	private final PersonDao personDao;

	private final BookValidator bookValidator;

	@Autowired
	public BookController(BookDao bookDao, PersonDao personDao, BookValidator bookValidator)
	{
		this.bookDao = bookDao;
		this.personDao = personDao;
		this.bookValidator = bookValidator;
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
		bookValidator.validate(book, bindingResult);

		if (bindingResult.hasErrors())
			return "/books/new";

		bookDao.save(book);
		return "redirect:/books";
	}

	 /*
	 	Need to have different names to escape overlapping 'id' field in PathVariable and ModelAttribute
	 Otherwise, the order of owner selection will be caused by Book.id
	 So @PathVariable("id") → @PathVariable("bookId") and @GetMapping("/book/{id}") → @GetMapping("/book/{bookId}")
	 https://github.com/spring-projects/spring-framework/issues/13987
	 	The second way to fix this is to add 'model.addAttribute("person", new Person())" directly, but that looks like a bad way
	 	Probably, the best way to fix it is a change '@ModelAttribute("person")' to '@ModelAttribute(name = "person", binding = false)'
	 to disable data binding
	 */
	@GetMapping("/book/{id}")
	public String bookInfo(@PathVariable("id") int bookId, @ModelAttribute(name = "person", binding = false) Person person, Model model)
	{
		model.addAttribute("book", bookDao.getBook(bookId));
		model.addAttribute("people", personDao.people());
		return "/books/book";
	}

	@PostMapping("/book/{id}/edit")
	public String editBookPage(@PathVariable("id") int bookId, Model model)
	{
		model.addAttribute("book", bookDao.getBook(bookId));
		return "/books/edit";
	}

	@PatchMapping("/book/{id}/edit")
	public String editBook(@PathVariable("id") int bookId, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult)
	{
		bookValidator.validate(book, bindingResult);

		if (bindingResult.hasErrors())
			return "/books/edit";

		bookDao.update(bookId, book);
		return "redirect:/books/book/{id}";
	}

	@PostMapping("/book/{id}/setOwner")
	public String setOwner(@PathVariable("id") int bookId, @ModelAttribute("person") Person owner)
	{
		bookDao.setOwner(bookId, owner);
		return "redirect:/books/book/{id}";
	}

	@PostMapping("/book/{id}/releaseOwner")
	public String releaseOwner(@PathVariable("id") int bookId)
	{
		bookDao.releaseOwner(bookId);
		return "redirect:/books/book/{id}";
	}

	@DeleteMapping("/book/{id}/delete")
	public String deleteBook(@PathVariable("id") int bookId)
	{
		bookDao.delete(bookId);
		return "redirect:/books";
	}
}
