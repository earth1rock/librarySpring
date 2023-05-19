package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dao.PersonDao;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController
{

	private final PersonDao personDao;

	//TODO validator

	@Autowired
	public PeopleController(PersonDao personDao)
	{
		this.personDao = personDao;
	}

	@GetMapping()
	public String people(Model model)
	{
		model.addAttribute("people", personDao.people());
		return "/people/people";
	}

	@GetMapping("/person/{id}")
	public String personInfo(@PathVariable int id, Model model)
	{
		model.addAttribute("person", personDao.getPerson(id));
		return "/people/person";
	}

	@GetMapping("/new")
	public String newPerson(@ModelAttribute("person") Person person)
	{
		return "/people/new";
	}

	@PostMapping("/new")
	public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult)
	{
		if (bindingResult.hasErrors())
			return "/people/new";

		personDao.save(person);
		return "redirect:/people";
	}
}
