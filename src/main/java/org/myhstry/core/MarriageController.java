package org.myhstry.core;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.myhstry.db.MarriageRepository;
import org.myhstry.db.PersonRepository;
import org.myhstry.model.Marriage;
import org.myhstry.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MarriageController {

	@Autowired
	MarriageRepository marriageRepository;
	
	@Autowired
	PersonRepository personRepository;
	
	@GetMapping("/marriages")
	public String index(Model model) {
		model.addAttribute("marriages", marriageRepository.findAll());
		return "marriages";
	}
	
	@GetMapping("/marriages/add")
    public String add(Model model){
		List<Person> wives = personRepository.findAllByGender("female");
		model.addAttribute("wives", wives);
		
		List<Person> husbands = personRepository.findAllByGender("male");
		model.addAttribute("husbands", husbands);
		
		return "addMarriage";
    }
	
	@PostMapping("/marriages/add")
    public RedirectView create(@RequestParam Map<String, String> body) throws ParseException {
        Person wive = null;
        if (body.get("wive").length() > 0) {
			int wiveId = Integer.parseInt(body.get("wive"));
			wive = personRepository.findById(wiveId).get();
		}
		
        Person husband = null;
		if (body.get("husband").length() > 0) {
			int husbandId = Integer.parseInt(body.get("husband"));
			husband = personRepository.findById(husbandId).get();
		}
		marriageRepository.save(new Marriage(husband, wive));
		
        return new RedirectView("/marriages");
    }
	
	@GetMapping("/marriages/delete/{id}")
    public RedirectView delete(@PathVariable int id){
        marriageRepository.deleteById(id);
        return new RedirectView("/marriages");
    }
	
	@GetMapping("/marriages/edit/{id}")
	public String edit(@PathVariable int id, Model model) {
		Optional<Marriage> marriage = marriageRepository.findById(id);
		if (marriage.isPresent())
			model.addAttribute("marriage", marriage.get());
		
		List<Person> wives = personRepository.findAllByGender("female");
		model.addAttribute("wives", wives);
		
		List<Person> husbands = personRepository.findAllByGender("male");
		model.addAttribute("husbands", husbands);
		
		return "editMarriage";
	}
	
	@PostMapping("/marriages/edit/{id}")
    public RedirectView updatePerson(@PathVariable int id,@RequestParam Map<String, String> body) throws ParseException {
		Marriage marriage = marriageRepository.findById(id).get();
		
		if (body.get("wife").length() > 0) {
			int wifeId = Integer.parseInt(body.get("wife"));
			Person wife = personRepository.findById(wifeId).get();
			marriage.setWife(wife);
		}
		
		if (body.get("husband").length() > 0) {
			int husbandId = Integer.parseInt(body.get("husband"));
			Person husband = personRepository.findById(husbandId).get();
			marriage.setHusband(husband);
		}
		
		marriageRepository.save(marriage);
        return new RedirectView("/marriages");
    }
}
