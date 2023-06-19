package org.example.controllers;

import jakarta.validation.Valid;
import org.example.models.Person;
import org.example.services.PeopleService;
import org.example.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/people")
public class PeopleController
{

	private final PeopleService peopleService;

	private final PersonValidator personValidator;

	@Autowired
	public PeopleController(PeopleService peopleService, PersonValidator personValidator)
	{
		this.peopleService = peopleService;
		this.personValidator = personValidator;
	}

	// Return NULL value from input in HTML-form instead of empty string ("") by default
	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		StringTrimmerEditor editor = new StringTrimmerEditor(true);
		binder.registerCustomEditor(String.class, editor);
	}

	@GetMapping()
	public String people(Model model)
	{
		model.addAttribute("people", peopleService.findAll());
		return "/people/people";
	}

	@GetMapping("/person/{id}")
	public String personInfo(@PathVariable("id") int personId, Model model)
	{
		Person person = peopleService.findOneByIdWithBooks(personId);
		if (person == null)
		{
			model.addAttribute("personId", personId);
			return "/people/noPerson";
		}

		model.addAttribute("person", person);
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
		personValidator.validate(person, bindingResult);

		if (bindingResult.hasErrors())
			return "/people/new";

		peopleService.save(person);
		return "redirect:/people";
	}

	// TODO probably need to change to GetMapping
	//  Now PostMapping used to trim question mark in url
	@PostMapping("/person/{id}/edit")
	public String editPersonPage(@PathVariable("id") int personId, Model model)
	{
		model.addAttribute("person", peopleService.findOneById(personId));
		return "/people/edit";
	}

	@PatchMapping("/person/{id}/edit")
	public String editPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult)
	{
		personValidator.validate(person, bindingResult);

		if (bindingResult.hasErrors())
			return "/people/edit";

		peopleService.update(person);
		return "redirect:/people/person/{id}";
	}

	@DeleteMapping("/person/{id}/delete")
	public String deletePerson(@PathVariable("id") int personId)
	{
		peopleService.delete(personId);
		return "redirect:/people";
	}
}
