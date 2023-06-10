package org.example.util;

import org.example.dao.BookDao;
import org.example.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator
{
	private final BookDao bookDao;

	@Autowired
	public BookValidator(BookDao bookDao)
	{
		this.bookDao = bookDao;
	}

	@Override
	public boolean supports(Class<?> clazz)
	{
		return Book.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors)
	{
		Book book = (Book) target;

		if (bookDao.bookExists(book))
			errors.reject("book.exists", "This book is already exists");
	}
}
