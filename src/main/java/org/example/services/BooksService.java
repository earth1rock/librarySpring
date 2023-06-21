package org.example.services;

import org.example.models.Book;
import org.example.models.Person;
import org.example.repositories.BooksRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService
{
	private final BooksRepository booksRepository;

	@Autowired
	public BooksService(BooksRepository booksRepository)
	{
		this.booksRepository = booksRepository;
	}

	public Page<Book> findAll(int page, int booksPerPage)
	{
		return booksRepository.findAll(PageRequest.of(page, booksPerPage));
	}

	public Book findOneById(int id)
	{
		return booksRepository.findById(id).orElse(null);
	}

	public Book findOneByIdWithOwner(int id)
	{
		Optional<Book> optionalBook = booksRepository.findById(id);
		if (optionalBook.isEmpty())
			return null;

		Hibernate.initialize(optionalBook.get().getOwner());
		return optionalBook.get();
	}

	@Transactional
	public void save(Book book)
	{
		booksRepository.save(book);
	}

	@Transactional
	public void update(Book book)
	{
		booksRepository.save(book);
	}

	@Transactional
	public void delete(int id)
	{
		booksRepository.deleteById(id);
	}

	@Transactional
	public void setOwner(int bookId, Person owner)
	{
		findOneById(bookId).setOwner(owner);
	}

	@Transactional
	public void releaseOwner(int bookId)
	{
		findOneById(bookId).setOwner(null);
	}

	public boolean exists(Book book)
	{
		return booksRepository.existsByTitleAndAuthor(book.getTitle(), book.getAuthor());
	}
}
