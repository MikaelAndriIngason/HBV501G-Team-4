package is.hi.hbv501g.hbv501gteam4.Controllers;

import is.hi.hbv501g.hbv501gteam4.Services.ServiceUsers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    ServiceUsers su;

    public IndexController(ServiceUsers su) {
        this.su = su;
    }

    /*@RequestMapping("/")
    public String indexPage(Model model) {
        return "index";
    }*/

    @GetMapping("/")
    public String indexPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("LoggedInUser");

        if (loggedInUser != null) {
            model.addAttribute("LoggedInUser", loggedInUser);
        }

        return "index";
    }
}
