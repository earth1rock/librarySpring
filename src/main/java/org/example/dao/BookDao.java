package org.example.dao;

import org.example.models.Book;
import org.example.models.Person;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Component
@RequestMapping("/books")
public class BookDao
{
	private final SessionFactory sessionFactory;

	@Autowired
	public BookDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Transactional(readOnly = true)
	public List<Book> books()
	{
		return sessionFactory.getCurrentSession().createQuery("select b from Book b", Book.class).getResultList();
	}

	@Transactional
	public void save(Book book)
	{
		sessionFactory.getCurrentSession().persist(book);
	}

	@Transactional(readOnly = true)
	public Book getBook(int id)
	{
		return sessionFactory.getCurrentSession().get(Book.class, id);
	}

	@Transactional
	public void update(Book updatedBook)
	{
		sessionFactory.getCurrentSession().merge(updatedBook);
	}

	@Transactional
	public void delete(int id)
	{
		sessionFactory.getCurrentSession().remove(getBook(id));
	}

	@Transactional
	public void setOwner(int bookId, Person owner)
	{
		getBook(bookId).setOwner(owner);
	}

	@Transactional
	public void releaseOwner(int bookId)
	{
		getBook(bookId).setOwner(null);
	}

	@Transactional(readOnly = true)
	public boolean bookExists(Book bookToFind)
	{
		return sessionFactory.getCurrentSession().contains(bookToFind);
	}
}
