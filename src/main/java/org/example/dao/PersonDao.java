package org.example.dao;

import org.example.models.Book;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonDao
{

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public PersonDao(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Person> people()
	{
		return jdbcTemplate.query("select * from person", new BeanPropertyRowMapper<>(Person.class));
	}

	public Person getPerson(int id)
	{
		Person person = jdbcTemplate.queryForObject("select * from person where id=?", new BeanPropertyRowMapper<>(Person.class), id);
		if (person == null)
			return null;

		List<Book> books = jdbcTemplate.query("select book.id, book.title, book.author, book.year from book " +
						"inner join person_book as pb on book.id = pb.id_book where pb.id_person=?",
				(rs, rowNum) -> {
					Book book = new Book();
					book.setId(rs.getInt(1));
					book.setTitle(rs.getString(2));
					book.setAuthor(rs.getString(3));
					book.setYear(rs.getInt(4));
					book.setOwner(person);
					return book;
				}, id);
		person.addBooks(books);
		return person;
	}

	public void save(Person person)
	{
		jdbcTemplate.update("insert into person(first_name, middle_name, last_name, year_of_birth) values (?, ?, ?, ?)",
				person.getFirstName(),
				person.getMiddleName(),
				person.getLastName(),
				person.getYearOfBirth());
	}

	public void update(int id, Person person)
	{
		jdbcTemplate.update("update person set first_name=?, middle_name=?, last_name=?, year_of_birth=? where id=?",
				person.getFirstName(),
				person.getMiddleName(),
				person.getLastName(),
				person.getYearOfBirth(),
				id);
	}

	public void delete(int id)
	{
		jdbcTemplate.update("delete from person where id=?", id);
	}

	public boolean personExists(Person personToFind)
	{
		Person person = jdbcTemplate.query("select * from person where first_name=? and middle_name = ? and last_name=? and year_of_birth=?",
				rs -> {
					BeanPropertyRowMapper<Person> rowMapper = new BeanPropertyRowMapper<>(Person.class);
					return rs.next() ? rowMapper.mapRow(rs, rs.getRow()) : null;
				},
				personToFind.getFirstName(),
				personToFind.getMiddleName(),
				personToFind.getLastName(),
				personToFind.getYearOfBirth());

		return person != null;
	}
}
