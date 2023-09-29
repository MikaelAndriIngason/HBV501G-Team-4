package is.hi.hbv501g.hbv501gteam4.User;

import org.springframework.stereotype.Service;


import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class EmailValidation implements Predicate<String>{

    private static final String regex = "^(.+)@(.+)$";

    private final Pattern pattern = Pattern.compile(regex);


    @Override
    public boolean test(String s) {
        if(s == null){
            return false;
        }
        return pattern.matcher(s).matches();
    }

}
