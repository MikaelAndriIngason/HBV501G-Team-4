package is.hi.hbv501g.hbv501gteam4.Controllers;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    /**
     * Opens up the index page
     * @param model
     * @param session session id
     * @return redirects to the index page
     */
    @GetMapping("/")
    public String indexPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("LoggedInUser");

        if (loggedInUser != null) {
            model.addAttribute("LoggedInUser", loggedInUser);
        }

        return "index";
    }
}
