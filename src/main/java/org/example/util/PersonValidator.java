package org.example.util;

import org.example.dao.PersonDao;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator
{
	private final PersonDao personDao;

	@Autowired
	public PersonValidator(PersonDao personDao)
	{
		this.personDao = personDao;
	}

	@Override
	public boolean supports(Class<?> clazz)
	{
		return Person.class.isAssignableFrom(clazz);
	}

	/*		 if person with same firstName, middleName and lastName exists in db, then reject/rejectValue
	 and one moment is a middleName marked as nullable, and it plays in unique constrains
	 how to process it correct?
			 If we specify empty middleName in html form, then data binding return empty string instead of NULL,
	 so it affects to @Nullable annotation in Person.java
	 */
	@Override
	public void validate(Object target, Errors errors)
	{
		Person person = (Person) target;

		if (personDao.personExists(person))
			errors.reject("person.exists", "This person is already exists");

	}
}
