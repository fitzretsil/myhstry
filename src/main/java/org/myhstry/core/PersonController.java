package org.myhstry.core;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.myhstry.db.MarriageRepository;
import org.myhstry.db.PersonRepository;
import org.myhstry.model.Marriage;
import org.myhstry.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PersonController {
	
	@Autowired
	PersonRepository repository;
	
	@Autowired
	MarriageRepository marriages;
	
	@GetMapping("/person")
    public String index(Model model){
		List<Person> people = repository.findAll();
//		for (Person person : people) {
//			if (person.getMother() == null && person.getFather() == null) {
//				Marriage temp = marriages.findMarriageByPAFID(person.getPafFamilyId());
//				if (temp != null ) {
//					person.setMother(temp.getWife());
//					person.setFather(temp.getHusband());
//				}
//			}
//		}
		model.addAttribute("list", people);
		return "person";
    }
	
	//-------------------
    //var jsonStructureObject = [{
    //    head: 'A',
    //    id: 'aa',
    //    contents: 'A Contents',
    //    children: [
    //        {
    //            head: 'A-1',
    //            id: 'a1',
    //            contents: 'A-1 Contents',
    //            children: [
    //                { head: 'A-1-1', id: 'a11', contents: 'A-1-1 Contents' }
    //            ]
    //        },
    //        {
    //            head: 'A-2',
    //            id: 'a2',
    //            contents: 'A-2 Contents',
    //            children: [
    //                { head: 'A-2-1', id: 'a21', contents: 'A-2-1 Contents' },
    //                { head: 'A-2-2', id: 'a22', contents: 'A-2-2 Contents' }
    //            ]
    //        }
    //    ]
    //}];
    ////-----------
    //$("#tree").jHTree({
    //    callType: 'obj',
    //    structureObj: jsonStructureObject
    //});
	@GetMapping(path = "/person/view/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sayHello(@PathVariable int id)
    {
         //Get data from service layer into entityList.

		HashMap<String, Object> map = new HashMap<>();
	    map.put("head", "Parent");
	    map.put("id","1");
	    map.put("contents", "Person's Name");
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }
	
	@GetMapping("/person/add")
    public String addPerson(Model model){
		List<Person> mothers = repository.findAllByGender("female");
		model.addAttribute("mothers", mothers);
		
		List<Person> fathers = repository.findAllByGender("male");
		model.addAttribute("fathers", fathers);
		
		return "addPerson";
    }
	
	@PostMapping("/person/add")
    public RedirectView createPerson(@RequestParam Map<String, String> body) throws ParseException {
		String firstname = body.get("firstname");
        String middlenames = body.get("middlenames");
        String surname = body.get("surname");
        String gender = body.get("gender");
        String birth = body.get("birth");
        String death = body.get("death");
        
        Person mother = null;
        if (body.get("mother").length() > 0) {
			int motherId = Integer.parseInt(body.get("mother"));
			mother = repository.findById(motherId).get();
		}
		
        Person father = null;
		if (body.get("father").length() > 0) {
			int fatherId = Integer.parseInt(body.get("father"));
			father = repository.findById(fatherId).get();
		}
        repository.save(new Person(firstname, middlenames, surname, gender, birth, death, mother, father));
        return new RedirectView("/person");
    }
	
	@GetMapping("/person/edit")
	public String editPerson(@RequestParam(name="id", required=false) int id, Model model) {
		Optional<Person> person = repository.findById(id);
		if (person.isPresent())
			model.addAttribute("person", person.get());
		
		List<Person> mothers = repository.findAllByGender("female");
		model.addAttribute("mothers", mothers);
		
		List<Person> fathers = repository.findAllByGender("male");
		model.addAttribute("fathers", fathers);
		
		return "editPerson";
	}
	
	@PostMapping("/person/edit/{id}")
    public RedirectView updatePerson(@PathVariable int id,@RequestParam Map<String, String> body) throws ParseException {
		Person person = repository.findById(id).get();
		
		if (body.get("firstname").length() > 0)
			person.setFirstname(body.get("firstname"));
		
		if (body.get("middlenames").length() > 0)
			person.setMiddlenames(body.get("middlenames"));
		
		if (body.get("surname").length() > 0)
			person.setSurname(body.get("surname"));
		
		if (body.get("gender").length() > 0)
			person.setGender(body.get("gender"));
		
//		if (body.get("birth").length() > 0)
//			person.setBirth(body.get("birth"));
		
//		if (body.get("death").length() > 0)
//			person.setDeath(body.get("death"));
		
		if (body.get("mother").length() > 0) {
			int motherId = Integer.parseInt(body.get("mother"));
			Person mother = repository.findById(motherId).get();
			person.setMother(mother);
		}
		
		if (body.get("father").length() > 0) {
			int fatherId = Integer.parseInt(body.get("father"));
			Person father = repository.findById(fatherId).get();
			person.setFather(father);
		}
		
        repository.save(person);
        return new RedirectView("/person");
    }
	
	@GetMapping("/person/delete/{id}")
    public RedirectView delete(@PathVariable int id){
        repository.deleteById(id);
        return new RedirectView("/person");
    }

}
