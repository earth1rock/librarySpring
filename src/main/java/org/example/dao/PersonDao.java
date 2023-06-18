package org.example.dao;

import org.example.models.Person;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class PersonDao
{
	private final SessionFactory sessionFactory;

	@Autowired
	public PersonDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Transactional(readOnly = true)
	public List<Person> people()
	{
		return sessionFactory.getCurrentSession().createQuery("select p from Person p", Person.class).getResultList();
	}

	@Transactional(readOnly = true)
	public Person getPerson(int id)
	{
		return sessionFactory.getCurrentSession().get(Person.class, id);
	}

	@Transactional(readOnly = true)
	public Person getPersonWithBooks(int id)
	{
		Person person = sessionFactory.getCurrentSession().get(Person.class, id);
		Hibernate.initialize(person.getBooks());
		return person;
	}

	@Transactional
	public void save(Person person)
	{
		sessionFactory.getCurrentSession().persist(person);
	}

	@Transactional
	public void update(Person person)
	{
		sessionFactory.getCurrentSession().merge(person);
	}

	@Transactional
	public void delete(int id)
	{
		sessionFactory.getCurrentSession().remove(getPerson(id));
	}

	@Transactional(readOnly = true)
	public boolean personExists(Person personToFind)
	{
		return sessionFactory.getCurrentSession().contains(personToFind);
	}
}
