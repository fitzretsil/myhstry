/**
 * 
 */
package org.myhstry.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.myhstry.core.paf.Individual;
import org.myhstry.core.paf.LEndianRandomAccessFile;
import org.myhstry.core.paf.PAFFile;
import org.myhstry.db.PersonRepository;
import org.myhstry.model.Event;
import org.myhstry.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	public String handleFileUpload(Model model, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
			throws Exception {

		storageService.store(file);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		LEndianRandomAccessFile file2 = new LEndianRandomAccessFile("upload-dir/" + file.getOriginalFilename(), "r");

		PAFFile paf = new PAFFile(file2);
		
		List<Individual> individuals = paf.getIndividuals();
		for (Individual i : individuals) {
			Person p = new Person();
			p.setFirstname(i.givenName);
			p.setGender(i.gender.toString());
			p.setMiddlenames(i.middleName);
			p.setPrefix(i.namePrefix);
			p.setSuffix(i.nameSuffix);
			p.setSurname(i.surname);
			
			Event birth = new Event();
			try {
				String birthString = i.birth.date.trim().toLowerCase();
				String birthYear = i.birth.yearString;
				
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
				System.out.println("Error caught: " + e.getMessage());
			}
			
			p.setBirth(birth);
			
			personRepository.save(p);
		}
		
		model.addAttribute("individuals", paf.getIndividuals());
		model.addAttribute("families", paf.getFamilies());
		
		return "results";
	}
}
