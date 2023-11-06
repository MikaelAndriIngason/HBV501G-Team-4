package is.hi.hbv501g.hbv501gteam4.Controllers;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.User;
import is.hi.hbv501g.hbv501gteam4.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class AccountController {
    private UserService userService;

    private User user;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Opens up the account page if a user is logged in
     * @param model
     * @param session session id
     * @return redirects to the home page if logged in, otherwise to the index page
     */
    @GetMapping("/account")
    public String indexPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("LoggedInUser");

        if (loggedInUser != null) {
            model.addAttribute("LoggedInUser", loggedInUser);
            user = loggedInUser;
        } else {
            return "redirect:/";
        }

        return "account";
    }

    /**
     * Changes the name of the user
     * @param newName the new name
     * @return redirects back to the account page
     */
    @PostMapping("/account/changename")
    public String changeName(@RequestParam("name") String newName) {

        String result = newName.replaceAll("\\s+", " ");

        if (!result.isEmpty()) {
            user.setName(newName);
            userService.save(user);
        }

        return "redirect:/account";
    }

    /**
     * Changes the email of the user
     * @param oldEmail the old/current email
     * @param newEmail the new email
     * @return redirects back to the account page
     */
    @PostMapping("/account/changeemail")
    public String changeEmail(@RequestParam("old-email") String oldEmail, @RequestParam("new-email") String newEmail) {

        if (oldEmail.equals(user.getEmail())) {
            String result = newEmail.replaceAll("\\s+", " ");

            if (!result.isEmpty()) {
                user.setEmail(newEmail);
                userService.save(user);
            }
        }

        return "redirect:/account";
    }

    /**
     * Changes the password of the user
     * @param oldPass the old/current password
     * @param newPass the new password
     * @param confirmPass confirmation of the new password
     * @return redirects back to the account page
     */
    @PostMapping("/account/changepass")
    public String changePassword(@RequestParam("old-pass") String oldPass, @RequestParam("new-pass") String newPass, @RequestParam("confirm-pass") String confirmPass) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (passwordEncoder.matches(oldPass, user.getPassword())) {

            if (newPass.equals(confirmPass)) {
                String result = newPass.replaceAll("\\s+", " ");

                if (!result.isEmpty()) {
                    String hashedNewPassword = passwordEncoder.encode(newPass);
                    user.setPassword(hashedNewPassword);
                    userService.save(user);
                }
            }
        }

        return "redirect:/account";
    }

    /**
     * Deletes the user
     * @param session session id
     * @param password the password of the user
     * @return if the password is correct then the user is redirected to the home page, otherwise the account page
     */
    @PostMapping("/account/delete")
    public String deleteAccount(HttpSession session, @RequestParam("pass") String password) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (passwordEncoder.matches(password, user.getPassword())) {
            userService.delete(user);
            session.invalidate();
            return "redirect:/";
        }

        return "redirect:/account";
    }
}