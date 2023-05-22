package org.example.models;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class Book
{

	private int id;

	@NotEmpty(message = "Title should be not empty")
	@Length(max = 100)
	private String title;

	@NotEmpty(message = "Author should be not empty")
	private String author;

	@Range(max = 2023, message = "Year should be not greater 2023")
	private int year;

	private Person owner;

	public Book()
	{
	}

	public Book(int id, String title, String author, int year)
	{
		this.id = id;
		this.title = title;
		this.author = author;
		this.year = year;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public Person getOwner()
	{
		return owner;
	}

	public void setOwner(Person owner)
	{
		this.owner = owner;
	}
}
