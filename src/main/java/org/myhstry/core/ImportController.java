/**
 * 
 */
package org.myhstry.core;

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
	
	private final StorageService storageService;

    @Autowired
    public ImportController(StorageService storageService) {
        this.storageService = storageService;
    }

	@GetMapping("/import")
    public String index(Model model){
		return "import";
    }
	
	@PostMapping("/import")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws Exception {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/import";
    }
}
