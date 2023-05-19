package org.example.dao;

import org.example.models.Book;
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
		return jdbcTemplate.queryForObject("select * from book where id=?", new BeanPropertyRowMapper<>(Book.class), id);
	}
}
