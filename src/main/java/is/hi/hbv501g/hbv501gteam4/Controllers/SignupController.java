package is.hi.hbv501g.hbv501gteam4.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/signup")
public class SignupController {

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

        // Skoða hvort lykilorð passa
        if (!password.equals(confirmPassword)) {
            return "redirect:/signup?error=password-mismatch";
        }

        // Encrypt-a lykilorðið

        // Senda nýtt entry í DB

        // Sendir notandan á sign-in formið ef allt gengur upp
        return "redirect:/signin";
    }
}