package is.hi.hbv501g.hbv501gteam4.Controllers;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Disc;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.User;
import is.hi.hbv501g.hbv501gteam4.Services.DiscService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;

@Controller
public class DiscController {

    DiscService discService;

    @Autowired
    public DiscController(DiscService discService) {
        this.discService = discService;
    }

    @RequestMapping("/home")
    public String indexPageLoggedIn(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if(sessionUser != null){
            model.addAttribute("LoggedInUser", sessionUser);
        }
        else {
            return "redirect:/";
        }
        List<Disc> allDiscs = discService.findAll();
        //Add some data to the Model
        model.addAttribute("discs", allDiscs);
        return "home";
    }

    @RequestMapping(value = "/addDisc", method = RequestMethod.GET)
    public String addDiscGET(Disc disc, Model model) {
        return "addDisc";
    }

    @RequestMapping(value = "/addDisc", method = RequestMethod.POST)
    public String addDiscPOST(Disc disc, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "addDisc";
        }
        discService.save(disc);
        return "redirect:/home";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String updateDiscGET(@PathVariable("id") long id, Model model) {
        Disc discToUpdate = discService.findBydiscID(id);
        model.addAttribute("disc", discToUpdate);
        return "updateDisc";
    }

    @RequestMapping(value = "/updateDisc", method = RequestMethod.POST)
    public String updateDiscPOST(Disc disc, BindingResult result, Model model) {
        if(result.hasErrors()){
            System.out.println(result.getAllErrors());
            return "redirect:/home";
        }
        discService.save(disc);
        return "redirect:/home";
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
    public String deleteDisc(@PathVariable("id") long id, Model model){
        Disc discToDelete = discService.findBydiscID(id);
        discService.delete(discToDelete);
        return "redirect:/home";
    }
}
