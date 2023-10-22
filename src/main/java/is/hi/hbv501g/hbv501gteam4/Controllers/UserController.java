package is.hi.hbv501g.hbv501gteam4.Controllers;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.User;
import is.hi.hbv501g.hbv501gteam4.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
//import io.jsonwebtoken.Jwts;

@Controller
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGET(User user) {
        return "login";
    }


    /**
     * HandleSubmit
     * Höndlar log-in innskráningu
     * @param user user entity
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginPOST(User user, BindingResult result, Model model, HttpSession session) {

        if(result.hasErrors()){
            return "login";
        }
        User exists = userService.login(user);
        if(exists != null){
            session.setAttribute("LoggedInUser", exists);
            model.addAttribute("LoggedInUser", exists);
            return "redirect:/home";
        }
        return "redirect:/";
    }


    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupGET(User user) {
        return "signup";
    }

    /**
     * handleSubmit
     * Höndlar sign-up skráningu
     * @param user user entity
     * @param confirmPassword staðfesting lykilorðs notandans
     * @return
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupPOST(User user, BindingResult result, Model model, HttpSession session, @RequestParam("confirm-password") String confirmPassword) {

        if(result.hasErrors()){
            return "redirect:/signup";
        }

        if (!user.getPassword().equals(confirmPassword)) {
            return "redirect:/signup?error=password-mismatch";
        }

        User exists = userService.findByEmail(user.getEmail());
        if(exists == null){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            userService.save(user);
            User loginUser = userService.login(user);
            if(loginUser != null){
                session.setAttribute("LoggedInUser", loginUser);
                model.addAttribute("LoggedInUser", loginUser);
                return "redirect:/home";
            }
        }
        return "redirect:/";
    }

    /**
     * Skráir notandan út
     * @param session User session
     * @return Sendir notandan aftur á index
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
