/**
 * 
 */
package org.myhstry.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.myhstry.core.paf.Family;
import org.myhstry.core.paf.Individual;
import org.myhstry.core.paf.LEndianRandomAccessFile;
import org.myhstry.core.paf.PAFFile;
import org.myhstry.db.MarriageRepository;
import org.myhstry.db.PersonRepository;
import org.myhstry.model.Event;
import org.myhstry.model.Marriage;
import org.myhstry.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author fitzretsil
 *
 */
@Controller
public class ImportController {
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	MarriageRepository marriageRepository;

	private final StorageService storageService;

	@Autowired
	public ImportController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/import")
	public String index(Model model) {
		return "import";
	}
	
	@PostMapping("/import")
	public String ajaxFileUpload(Model model, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {
		storageService.store(file);
		
		model.addAttribute("filename", file.getOriginalFilename());
		
		return "import2";
	}
	
	@GetMapping("/paf1/{filename}")
	public ResponseEntity<?> numberOfRecords(Model model,@PathVariable String filename) throws IOException {
		AjaxResponse response = new AjaxResponse();
		
		LEndianRandomAccessFile file = new LEndianRandomAccessFile("upload-dir/" + filename, "r");
		
		PAFFile paf = new PAFFile(file);
		
		List<Individual> individuals = paf.getIndividuals();
		List<Family> families = paf.getFamilies();
		
		response.setStatus("Done");
		HashMap map = new HashMap();
		map.put("people", individuals.size());
		map.put("families", families.size());
		
		response.setData(map);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/paf2/{filename}/{value}")
	public ResponseEntity<?> importIndividuals(Model model, @PathVariable String filename, @PathVariable int value) throws IOException, ParseException {
		AjaxResponse response = new AjaxResponse();
		
		LEndianRandomAccessFile file = new LEndianRandomAccessFile("upload-dir/" + filename, "r");
		
		PAFFile paf = new PAFFile(file);
		
		List<Individual> individuals = paf.getIndividuals();
		
		for (int i = value; i < value+100; i++ ) {
			importIndividual(individuals.get(i));
		}
		
		response.setStatus("Done");
		HashMap map = new HashMap();
		map.put("processed", value+100);
		
		response.setData(map);
		
		return ResponseEntity.ok(response);
	}

	private void importIndividual(Individual i) throws ParseException {
		Person p = new Person();
		p.setPafId(i.id);
		if (i.middleName.length() == 0) {
			String[] names = i.givenName.trim().split("\\s+");
			p.setFirstname(names[0]);
			String middleNames = "";
			for (int j = 1; j < names.length; j++) {
				middleNames += names[j] + " ";
			}
			if (middleNames.length() > 0) p.setMiddlenames(middleNames.trim());
		} else {
			p.setFirstname(i.givenName);
			p.setMiddlenames(i.middleName);
		}
		p.setGender(i.gender.toString());
		p.setPrefix(i.namePrefix);
		p.setSuffix(i.nameSuffix);
		p.setSurname(i.surname);
		
		Event birth = new Event();
		try {
			String birthString = i.birth.date.trim().toLowerCase();
			String birthYear = i.birth.yearString;
			birth.setPlace(i.birth.place);
			
			if (birthString.equals(null)) {
				
			} else if (birthString.startsWith("fl")) {
				birth.setFl(true);
				
				birthYear = birthString.substring(2).trim();
				birth.setYear(Integer.parseInt(birthYear));
				
			} else {
				birth.setYear(Integer.parseInt(birthYear));
				
				birth.setDay(Integer.parseInt(i.birth.dayString));
				
				Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(i.birth.monthString);
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(date);			    
				birth.setMonth(cal.get(Calendar.MONTH)+1);
			}
			
		} catch (NumberFormatException | NullPointerException e) {
//			System.out.println("Error caught: " + e.getMessage());
		}
		
		p.setBirth(birth);
		
		Event death = new Event();
		try {
			String deathString = i.death.date.trim().toLowerCase();
			String deathYear = i.death.yearString;
			death.setPlace(i.death.place);
			
			if (deathString.equals(null)) {
				
			} else {
				death.setYear(Integer.parseInt(deathYear));
				
				death.setDay(Integer.parseInt(i.death.dayString));
				
				Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(i.death.monthString);
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(date);			    
			    death.setMonth(cal.get(Calendar.MONTH)+1);
			}
			
		} catch (NumberFormatException | NullPointerException e) {
//			System.out.println("Error caught: " + e.getMessage());
		}
		
		p.setDeath(death);
					
		if (i.famcIds != null && i.famcIds.size() > 0) {
			p.setPafFamilyId(i.famcIds.get(0));
		}
		
		personRepository.save(p);
//		people.add(p);
		
	}

	//	@PostMapping("/import")
	public String handleFileUpload(Model model, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
			throws Exception {
		
		List<Person> people = new ArrayList<Person>();

		storageService.store(file);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		LEndianRandomAccessFile file2 = new LEndianRandomAccessFile("upload-dir/" + file.getOriginalFilename(), "r");

		PAFFile paf = new PAFFile(file2);
		
		List<Individual> individuals = paf.getIndividuals();
		List<Family> families = paf.getFamilies();
		
		for (Individual i : individuals) {
			Person p = new Person();
			p.setPafId(i.id);
			if (i.middleName.length() == 0) {
				String[] names = i.givenName.trim().split("\\s+");
				p.setFirstname(names[0]);
				String middleNames = "";
				for (int j = 1; j < names.length; j++) {
					middleNames += names[j] + " ";
				}
				if (middleNames.length() > 0) p.setMiddlenames(middleNames.trim());
			} else {
				p.setFirstname(i.givenName);
				p.setMiddlenames(i.middleName);
			}
			p.setGender(i.gender.toString());
			p.setPrefix(i.namePrefix);
			p.setSuffix(i.nameSuffix);
			p.setSurname(i.surname);
			
			Event birth = new Event();
			try {
				String birthString = i.birth.date.trim().toLowerCase();
				String birthYear = i.birth.yearString;
				birth.setPlace(i.birth.place);
				
				if (birthString.equals(null)) {
					
				} else if (birthString.startsWith("fl")) {
					birth.setFl(true);
					
					birthYear = birthString.substring(2).trim();
					birth.setYear(Integer.parseInt(birthYear));
					
				} else {
					birth.setYear(Integer.parseInt(birthYear));
					
					birth.setDay(Integer.parseInt(i.birth.dayString));
					
					Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(i.birth.monthString);
				    Calendar cal = Calendar.getInstance();
				    cal.setTime(date);			    
					birth.setMonth(cal.get(Calendar.MONTH)+1);
				}
				
			} catch (NumberFormatException | NullPointerException e) {
//				System.out.println("Error caught: " + e.getMessage());
			}
			
			p.setBirth(birth);
			
			Event death = new Event();
			try {
				String deathString = i.death.date.trim().toLowerCase();
				String deathYear = i.death.yearString;
				death.setPlace(i.death.place);
				
				if (deathString.equals(null)) {
					
				} else {
					death.setYear(Integer.parseInt(deathYear));
					
					death.setDay(Integer.parseInt(i.death.dayString));
					
					Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(i.death.monthString);
				    Calendar cal = Calendar.getInstance();
				    cal.setTime(date);			    
				    death.setMonth(cal.get(Calendar.MONTH)+1);
				}
				
			} catch (NumberFormatException | NullPointerException e) {
//				System.out.println("Error caught: " + e.getMessage());
			}
			
			p.setDeath(death);
						
			if (i.famcIds != null && i.famcIds.size() > 0) {
				p.setPafFamilyId(i.famcIds.get(0));
			}
			
			personRepository.save(p);
			people.add(p);
		}
		
		for (Family family : families) {
			System.out.println("Importing " + family);
			Marriage marriage = new Marriage();
			marriage.setPafId(family.id);
			if (family.husbandId != null ) {
				int husbandId = Integer.parseInt(family.husbandId.substring(1)) - 1;
				marriage.setHusband(people.get(husbandId));
			}
			if (family.wifeId != null ) {
				int wifeId = Integer.parseInt(family.wifeId.substring(1)) - 1;
				marriage.setWife(people.get(wifeId));
			}
			
			if (family.marriage != null) {
				family.marriage.parseDateParts();
				Event wedding = new Event();
				try {
					System.out.println("Marriage happend on " + family.marriage);
					String dateString = family.marriage.date.trim().toLowerCase();
					String year = ""+family.marriage.yearInt;
					System.out.println("Date String: " + dateString + ", Year: " + year);
					wedding.setPlace(family.marriage.place);
					
					if (dateString.equals(null)) {
						
					} else {
						wedding.setYear(Integer.parseInt(year));
						
						wedding.setDay(Integer.parseInt(family.marriage.dayString));
						
						Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(family.marriage.monthString);
					    Calendar cal = Calendar.getInstance();
					    cal.setTime(date);			    
					    wedding.setMonth(cal.get(Calendar.MONTH)+1);
					}
					
				} catch (NumberFormatException | NullPointerException e) {
	//				System.out.println("Error caught: " + e.getMessage());
				}
				
				marriage.setWedding(wedding);
			}
			
			marriageRepository.save(marriage);
		}
		
		for (Person person : people) {
			if (person.getMother() == null && person.getFather() == null) {
				Marriage family = marriageRepository.findMarriageByPAFID(person.getPafFamilyId());
				if (family != null ) {
					person.setMother(family.getWife());
					person.setFather(family.getHusband());
					personRepository.save(person);
				}
			}
		}
		
		model.addAttribute("individuals", paf.getIndividuals());
		model.addAttribute("families", paf.getFamilies());
		
		return "results";
	}
}
