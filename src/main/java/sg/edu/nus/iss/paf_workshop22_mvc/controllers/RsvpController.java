package sg.edu.nus.iss.paf_workshop22_mvc.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import sg.edu.nus.iss.paf_workshop22_mvc.models.Rsvp;
import sg.edu.nus.iss.paf_workshop22_mvc.services.RsvpService;

@Controller
public class RsvpController {
    
    @Autowired
    RsvpService rsvpSvc;

    @GetMapping("/rsvps")
    public String getAllRsvp(Model model) throws ParseException{

        List<Rsvp> rsvpList = rsvpSvc.getAllRsvp();
        model.addAttribute("rsvpList", rsvpList);
        return "allRsvp";

    }

    @GetMapping("/rsvp/findbyname")
    public String getRsvpByName(@RequestParam String name, Model model) throws ParseException{

        List<Rsvp> rsvpList = rsvpSvc.findRsvpByName(name);
        model.addAttribute("rsvpList", rsvpList);
        return "allRsvp";

    }

    @GetMapping("/rsvp/add")
    public String getRsvpPage(Model model){

        model.addAttribute("rsvp", new Rsvp());
        return "addrsvp";
    }

    @GetMapping("/rsvp/updatepage/{email}")
    public String getRsvpUpdatePage(@PathVariable("email") String email, Model model){

        Rsvp rsvp = rsvpSvc.findByEmail(email);
        model.addAttribute("rsvp", rsvp);
        return "updatersvp";
    }

    @PostMapping("/rsvp/add")
    public String addRsvp(@Valid Rsvp rsvp, BindingResult result){

        if(result.hasErrors()){
            return "addrsvp";
        }

        Boolean saved = rsvpSvc.addRsvp(rsvp); 
        System.out.println(saved);

        return "redirect:/rsvps";
    }

    @PostMapping("/rsvp/update")
    public String updateRsvp(@Valid @ModelAttribute Rsvp rsvp, BindingResult result){


        if(result.hasErrors()){
            return "updatersvp";
        }

        Boolean updated = rsvpSvc.updateRsvp(rsvp); 

        return "redirect:/rsvps";
    }

    
}
