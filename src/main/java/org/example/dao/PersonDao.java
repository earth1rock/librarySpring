package org.example.dao;

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
		return jdbcTemplate.queryForObject("select * from person where id=?", new BeanPropertyRowMapper<>(Person.class), id);
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
}
