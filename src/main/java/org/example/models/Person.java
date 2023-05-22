package org.example.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Person
{
	private int id;

	@NotEmpty(message = "First name should be not empty")
	@Size(min = 2, max = 40, message = "Fist name should be between 2 and 40 characters")
	private String firstName;

	@NotEmpty(message = "Last name should be not empty")
	@Size(min = 2, max = 40, message = "Last name should be between 2 and 40 characters")
	private String lastName;

	@Nullable
	@Size(min = 2, max = 40, message = "Middle name should be between 2 and 40 characters")
	private String middleName;

	@Range(min = 1900, max = 2023, message = "Year of birth should be between 1900 and 2023")
	//TODO replace min and max ages from file or other way
	private int yearOfBirth;

	private final List<Book> books = new ArrayList<>();

	public Person()
	{
	}

	public Person(int id, String firstName, String lastName, @Nullable String middleName, int yearOfBirth)
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

	public String getMiddleName()
	{
		return middleName;
	}

	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}

	public int getYearOfBirth()
	{
		return yearOfBirth;
	}

	public void setYearOfBirth(int yearOfBirth)
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
