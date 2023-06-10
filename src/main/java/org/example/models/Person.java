package org.example.models;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Person
{
	private int id;

	@NotNull(message = "{person.firstName.notEmpty}")
	@Length(min = 2, max = 40, message = "{person.firstName.range}")
	private String firstName;

	@NotNull(message = "{person.lastName.notEmpty}")
	@Length(min = 2, max = 40, message = "{person.lastName.range}")
	private String lastName;

	@Nullable
	@Length(min = 2, max = 40, message = "{person.middleName.range}")
	private String middleName;

	@NotNull(message = "{person.yearOfBirth.notEmpty}")
	@Range(min = 1900, max = 2023, message = "{person.yearOfBirth.range}")
	private Integer yearOfBirth;

	private final List<Book> books = new ArrayList<>();

	public Person()
	{
	}

	public Person(int id, String firstName, String lastName, @Nullable String middleName, Integer yearOfBirth)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.yearOfBirth = yearOfBirth;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	@Nullable
	public String getMiddleName()
	{
		return middleName;
	}

	public void setMiddleName(@Nullable String middleName)
	{
		this.middleName = middleName;
	}

	public Integer getYearOfBirth()
	{
		return yearOfBirth;
	}

	public void setYearOfBirth(Integer yearOfBirth)
	{
		this.yearOfBirth = yearOfBirth;
	}

	public String getFullName()
	{
		return String.format("%s %s %s", firstName, middleName, lastName);
	}

	public boolean hasBooks()
	{
		return !books.isEmpty();
	}

	public void addBook(Book book)
	{
		books.add(book);
	}

	public List<Book> getBooks()
	{
		return books;
	}

	public void addBooks(List<Book> bookList)
	{
		books.addAll(bookList);
	}
}
