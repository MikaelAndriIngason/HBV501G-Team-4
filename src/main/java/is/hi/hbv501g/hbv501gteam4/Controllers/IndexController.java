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

    @GetMapping("/")
    public String showIndexPage() {
        System.out.println(this.su.getRepository());
        return "index";
    }
}
