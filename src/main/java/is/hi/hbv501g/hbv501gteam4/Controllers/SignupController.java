package is.hi.hbv501g.hbv501gteam4.Controllers;

import is.hi.hbv501g.hbv501gteam4.Entities.User;
import is.hi.hbv501g.hbv501gteam4.Services.ServiceUsers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Controller
@RequestMapping("/signup")
public class SignupController {

    ServiceUsers su;

    public SignupController(ServiceUsers su) {
        this.su = su;
    }

    @GetMapping
    public String showSignupForm() {
        return "signup";
    }

    /**
     * Höndlar sign-up skráningu
     * @param name nafn notandans
     * @param email netfang notandans
     * @param password lykilorð notandans
     * @param confirmPassword staðfesting lykilorðs notandans
     * @return
     */
    @PostMapping
    public String handleSubmit(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("confirm-password") String confirmPassword) {

        // Þarf að athuga hvort notandi með netfangið er til í DB
        if (this.su.checkIfUserExists(email)) {
            return "redirect:/signup?error=email-exists";
        }
        // Skoða hvort lykilorð passa
        if (!password.equals(confirmPassword)) {
            return "redirect:/signup?error=password-mismatch";
        }

        // Encrypt-a lykilorðið
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        // Senda nýtt entry í DB
        User u = new User(name, email, hashedPassword);
        this.su.createUser(u);

        // Sendir notandan á sign-in formið ef allt gengur upp
        return "redirect:/signin";
    }
}