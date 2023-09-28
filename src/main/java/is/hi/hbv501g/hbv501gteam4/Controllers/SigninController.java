package is.hi.hbv501g.hbv501gteam4.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/signin")
public class SigninController {

    @GetMapping
    public String showLoginForm() {
        return "signin";
    }

    /**
     * Höndlar sign-in innskráningu
     * @param email netfang notandans
     * @param password lykilorð notandans
     * @return
     */
    @PostMapping
    public String handleSubmit(@RequestParam("email") String email, @RequestParam("password") String password) {

        // Þarf að athuga hvort notandi með netfangið er til í DB

        // Athuga hvort notandi sláði inn rétt password

        // Búa til session fyrir notandan

        // Sendir notandan á forsíðuna ef allt gengur upp
        return "redirect:/";
    }
}