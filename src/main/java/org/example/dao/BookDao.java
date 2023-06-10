package org.example.dao;

import org.example.models.Book;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Component
@RequestMapping("/books")
public class BookDao
{
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public BookDao(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Book> books()
	{
		return jdbcTemplate.query("select * from book", new BeanPropertyRowMapper<>(Book.class));
	}

	public void save(Book book)
	{
		jdbcTemplate.update("insert into book(title, author, year) values (?, ? ,?)",
				book.getTitle(),
				book.getAuthor(),
				book.getYear());
	}

	public Book getBook(int id)
	{
		Book book = jdbcTemplate.queryForObject("select * from book where id=?", new BeanPropertyRowMapper<>(Book.class), id);
		if (book == null)
			return null;

		Person person = jdbcTemplate.query("select id, first_name, middle_name, last_name, year_of_birth from person join person_book pb on person.id = pb.id_person where id_book=?",
				rs -> {
					BeanPropertyRowMapper<Person> rowMapper = new BeanPropertyRowMapper<>(Person.class);
					return rs.next() ? rowMapper.mapRow(rs, rs.getRow()) : null;
				}, id);

		book.setOwner(person);
		return book;
	}

	public void update(int id, Book updatedBook)
	{
		jdbcTemplate.update("update book set title=?, author=?, year=? where id=?",
				updatedBook.getTitle(),
				updatedBook.getAuthor(),
				updatedBook.getYear(),
				id);
	}

	public void delete(int id)
	{
		jdbcTemplate.update("delete from book where id=?", id);
	}

	public void setOwner(int bookId, Person owner)
	{
		getBook(bookId).setOwner(owner);
		jdbcTemplate.update("insert into person_book values (?, ?)", owner.getId(), bookId);
	}

	public void releaseOwner(int bookId)
	{
		getBook(bookId).setOwner(null);
		jdbcTemplate.update("delete from person_book where id_book=?", bookId);
	}

	public boolean bookExists(Book bookToFind)
	{
		Book book = jdbcTemplate.query("select * from book where title=? and author=? and year=?",
				rs -> {
					BeanPropertyRowMapper<Book> rowMapper = new BeanPropertyRowMapper<>(Book.class);
					return rs.next() ? rowMapper.mapRow(rs, rs.getRow()) : null;
				},
				bookToFind.getTitle(),
				bookToFind.getAuthor(),
				bookToFind.getYear());

		return book != null;
	}
}
