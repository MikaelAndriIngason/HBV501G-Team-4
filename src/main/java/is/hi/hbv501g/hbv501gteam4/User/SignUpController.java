package is.hi.hbv501g.hbv501gteam4.User;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


//Smá týnd hérna var að pæla að kanski væri betra að hafa bara auðveldan klasa með bara SignUp formi og svo hafa
//annan controller klasa sem er með allt boolean sem tékkar á hvort email sé í lagi og username og svoleiðis??

@RequestMapping("/SignUp")
public class SignUpController {

    private final EmailValidation emailValidation;
    private UserRepo userRepo;


    @PostMapping
    public String handleSubmit(@RequestParam("Name") String name,
                               @RequestParam("Username") String username,
                               @RequestParam("Email") String email,
                               @RequestParam("Password") String password,
                               @RequestParam("Confirm password") String confirmpassword) {

        User user = new User();

    }

   @RequestMapping (value = "/Signup", method = RequestMethod.POST)
    public ModelAndView registerUser(RegistrationRequest request, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        boolean isEmailValid = emailValidation.test(request.getEmail());
        boolean alreadyUser = UserRepo.findByEmail(request.getEmail()).isPresent();
        if(!isEmailValid) {
            modelAndView.addObject("successMessage", "Email isn't valid");
        } else if (alreadyUser) {
            modelAndView.addObject("succsessMessage", "Account using this email already exists");

        } else if(!password.equals(confirmPassword)

        }
   }

}
