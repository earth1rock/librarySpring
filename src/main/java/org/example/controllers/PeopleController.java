package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dao.PersonDao;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String personInfo(@PathVariable("id") int personId, Model model)
	{
		model.addAttribute("person", personDao.getPerson(personId));
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

	// TODO probably need to change to GetMapping
	//  Now PostMapping used to trim question mark in url
	@PostMapping("/person/{id}/edit")
	public String editPersonPage(@PathVariable("id") int personId, Model model)
	{
		model.addAttribute("person", personDao.getPerson(personId));
		return "/people/edit";
	}

	@PatchMapping("/person/{id}/edit")
	public String editPerson(@PathVariable("id") int personId, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult)
	{
		if (bindingResult.hasErrors())
			return "/people/edit";

		personDao.update(personId, person);
		return "redirect:/people/person/{id}";
	}

	@DeleteMapping("/person/{id}/delete")
	public String deletePerson(@PathVariable("id") int personId)
	{
		personDao.delete(personId);
		return "redirect:/people";
	}
}
