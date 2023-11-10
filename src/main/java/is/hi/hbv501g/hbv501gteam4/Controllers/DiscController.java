package is.hi.hbv501g.hbv501gteam4.Controllers;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Disc;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Favorite;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Image;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.User;
import is.hi.hbv501g.hbv501gteam4.Services.DiscService;
import is.hi.hbv501g.hbv501gteam4.Services.FavoriteService;
import is.hi.hbv501g.hbv501gteam4.Services.ImageService;
import is.hi.hbv501g.hbv501gteam4.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.*;

@Controller
public class DiscController {

    DiscService discService;
    ImageService imageService;
    UserService userService;
    FavoriteService favoriteService;

    private User user;


    public DiscController(DiscService discService, ImageService imageService, UserService userService, FavoriteService favoriteService) {
        this.discService = discService;
        this.imageService = imageService;
        this.userService = userService;
        this.favoriteService = favoriteService;
    }

    /**
     * opens the home page if a user is logged in
     * @param session session id
     * @param model
     * @return redirects to home if logged in else to the index page
     */
    @RequestMapping("/home")
    public String indexPageLoggedIn(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if (sessionUser != null) {
            model.addAttribute("LoggedInUser", sessionUser);
            user = sessionUser;
        } else {
            return "redirect:/";
        }
        List<Disc> allDiscs = discService.findAll();

        //Add some data to the Model
        model.addAttribute("discs", allDiscs);
        model.addAttribute("showingFavorites", false);
        return "home";
    }

    /**
     * displays his favorites on the home page
     * @param session session id
     * @param model
     * @return redirects to home if logged in else to the index page
     */
    @RequestMapping("/home/favorites")
    public String homePageFavorites(HttpSession session, Model model) {
        List<Favorite> favorites = favoriteService.findFavoritesByUser(user);
        List<Disc> favoriteDiscs = new ArrayList<>();

        for (Favorite favorite : favorites) {
            Disc disc = favorite.getDisc();
            favoriteDiscs.add(disc);
        }
        if (user != null) {
            model.addAttribute("LoggedInUser", user);
        }

        //Add some data to the Model
        model.addAttribute("discs", favoriteDiscs);
        model.addAttribute("showingFavorites", true);
        return "home";
    }

    /**
     * opens the page so a user can add a new disc
     * @param disc a potential disc
     * @param model
     * @return the page to add a disc
     */
    @RequestMapping(value = "/addDisc", method = RequestMethod.GET)
    public String addDiscGET(Disc disc, Model model) {
        if (user != null) {
            model.addAttribute("LoggedInUser", user);
        }

        return "addDisc";
    }

    /**
     * adds a disc to the db
     * @param disc the disc being added
     * @param result
     * @param model
     * @param images images that were uploaded with the new disc
     * @return home is adding a disc was successful
     */
    @RequestMapping(value = "/addDisc", method = RequestMethod.POST)
    public String addDiscPOST(Disc disc, BindingResult result, Model model, @RequestParam("image") MultipartFile[] images){
        if (result.hasErrors()) {
            return "addDisc";
        }

        disc.setUser(user);

        Disc discSaved = discService.save(disc);
        addImage(discSaved, images);
        return "redirect:/home";
    }

    /**
     * page to edit the disc
     * @param id id of the disc that the user wants to edit
     * @param model
     * @return the page to edit a disc
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String updateDiscGET(@PathVariable("id") long id, Model model) {
        Disc discToUpdate = discService.findBydiscID(id);
        model.addAttribute("disc", discToUpdate);
        if (user != null) {
            model.addAttribute("LoggedInUser", user);
        }
        return "updateDisc";
    }

    /**
     * function to update the disc in the db
     * @param disc the disc with updated information
     * @param result
     * @param model
     * @param images images that were added to the disc
     * @return redirection to the home page.
     */
    @RequestMapping(value = "/updateDisc", method = RequestMethod.POST)
    public String updateDiscPOST(Disc disc, BindingResult result, Model model, @RequestParam("image") MultipartFile[] images) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "redirect:/home";
        }

        Disc discSaved = discService.save(disc);
        addImage(discSaved, images);
        return "redirect:/home";
    }

    /**
     * to delete a disc
     * @param id id of the disc that should be deleted
     * @param model
     * @return redirection to the home page
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteDisc(@PathVariable("id") long id, Model model) {
        Disc discToDelete = discService.findBydiscID(id);
        discService.delete(discToDelete);
        return "redirect:/home";
    }

    /**
     * function to delete an image that is connected to a disc
     * @param id id of the disc that the image is connected to
     * @param imageID id of the image
     * @param model
     * @return the page to update a disc (same page it is called from)
     */
    @RequestMapping(value = "/deleteImage/{id}/{imageId}", method = RequestMethod.GET)
    public String deleteImage(@PathVariable("id") long id,@PathVariable("imageId") long imageID,Model model) {
        Image imageToDelete = imageService.findByimageID(imageID);
        deleteImage(imageToDelete);
        imageService.delete(imageToDelete);
        return String.format("redirect:/update/%s", id);
    }

    /**
     * Function to add an image to the bucket in supabase
     * @param disc the disc that the image is connected to
     * @param images the image that needs to be uploaded
     */
    private void addImage(Disc disc, MultipartFile[] images){
        for (MultipartFile image : images) {
            // Process each image here
            if (!image.isEmpty()) {
                try {
                    byte[] fileBytes = image.getBytes();
                    String mimeType = image.getContentType();

                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFjcnFqenlvY3R5dmh1a3pib2RuIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTY5NTA0Njk1MCwiZXhwIjoyMDEwNjIyOTUwfQ.p1wvVmxqJTOhDEBCUxrDurXrg7m5MTDBiXHVNT55Ws8");

                    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                    body.add("file", new HttpEntity<>(fileBytes));
                    body.add("metadata", "{\"disID\":\"" + disc.getDiscID() + "\", \"mimeType\":\"" + mimeType + "\"}");

                    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

                    Image imageEntity = new Image(null, disc);

                    Image imageSaved = imageService.save(imageEntity);

                    String supabaseUrl = String.format("https://qcrqjzyoctyvhukzbodn.supabase.co/storage/v1/object/Images/Discs/%s", imageSaved.getImageID());
                    ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                            supabaseUrl,
                            HttpMethod.POST,
                            requestEntity,
                            String.class
                    );
                    supabaseUrl = String.format("https://qcrqjzyoctyvhukzbodn.supabase.co/storage/v1/object/public/Images/Discs/%s", imageSaved.getImageID());
                    imageSaved.setImage(supabaseUrl);
                    imageService.save(imageSaved);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Function to delete an image
     * @param image image that should be deleted
     */
    private void deleteImage(Image image){
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFjcnFqenlvY3R5dmh1a3pib2RuIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTY5NTA0Njk1MCwiZXhwIjoyMDEwNjIyOTUwfQ.p1wvVmxqJTOhDEBCUxrDurXrg7m5MTDBiXHVNT55Ws8");
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
            String supabaseUrl = String.format("https://qcrqjzyoctyvhukzbodn.supabase.co/storage/v1/object/Images/Discs/%s", image.getImageID());

            System.out.println(image.getImage());
            ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                    supabaseUrl,
                    HttpMethod.DELETE,
                    requestEntity,
                    String.class
            );

        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                // Handle 404 (not found) error
                System.out.println("Resource not found: " + ex.getResponseBodyAsString());
            } else {
                // Handle other HTTP errors
                System.out.println("HTTP Error: " + ex.getResponseBodyAsString());
            }}
            catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves and displays the details of the disc with specified ID
     *
     * @param id the identifier of the Frisbee to retrieve and display details for.
     * @param model object, used to pass data to the view
     * @return the site or template for discDetails
     */
    @RequestMapping (value = "/disc/{id}", method = RequestMethod.GET)
    public String discDetails(HttpSession session, @PathVariable("id") long id, Model model) {
        //Retrieves the disc with the specified ID form the discService
        Disc disc = discService.findBydiscID(id);

        Favorite favorite = favoriteService.findFavoriteByUserAndDisc(user, disc);
        List<Favorite> favorites = favoriteService.findFavoritesByUser(user);
        model.addAttribute("favorite", favorites.contains(favorite));
        if(favorite != null){
            model.addAttribute("favoriteId", favorite.getId());
        }


        //Retrieve a list of the images related to that specific disc
        List<Image> images = disc.getImages();
        model.addAttribute("disc", disc);
        model.addAttribute("images", images);
        if (user != null) {
            model.addAttribute("user_id", user.getId());
            model.addAttribute("LoggedInUser", user);
        }
        return "discDetails";
    }

    /**
     * Adds a disc to favorites
     * @param session
     * @param discId the id of the disc that should be added to favorites
     * @return the detail page (same page that the function is called from)
     */
    @RequestMapping(value= "/favorites/{discId}", method = RequestMethod.GET)
    public String addToFavorites(HttpSession session, @PathVariable("discId") long discId) {
        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if (sessionUser != null) {
            Disc newFavoriteDisc = discService.findBydiscID(discId);

            Favorite favorite = new Favorite(sessionUser, newFavoriteDisc);
            favoriteService.save(favorite);

            return "redirect:/disc/" + discId;
        } else {
            return "redirect:/";
        }
    }

    /**
     * Removes a disc from favorites
     * @param session
     * @param discId id of the disc that should be removed
     * @return
     */
    @RequestMapping(value= "/favorites/remove/{discId}/{favoriteId}", method = RequestMethod.GET)
    public String removeFromFavorites(HttpSession session, @PathVariable("discId") long discId, @PathVariable("favoriteId") long favoriteId) {
        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if (sessionUser != null) {
            Favorite favorite = favoriteService.findById(favoriteId);
            favoriteService.delete(favorite);
            return "redirect:/disc/" + discId;

        } else {
            return "redirect:/";
        }
    }

    /**
     * Filtering and search for the Frisbees.
     * had problem with the name-search when writing in lower or upper, so I got help from stackoverflow and chatgpt.
     * In the html it is a form that can be opened or closed on the site.
     * filters and search while using name, colour, condition and from to price.
     * If only one is chosen it won't those that were not chosen will not interfeer.
     * Originally you had to choose all or nothing but I was able to fix that.
     *
     * @param fromPrice from what price you want to filter
     * @param toPrice max price you want
     * @param colour selection of colours
     * @param condition used new
     * @param name name
     * @param model
     * @return filtered site
     */
    @GetMapping("/filter")
    public String filterDiscs(@RequestParam(value = "fromPrice", required = false) Integer fromPrice,
                              @RequestParam(value = "toPrice", required = false) Integer toPrice,
                              @RequestParam(value = "colour", required = false) String colour,
                              @RequestParam(value = "condition", required = false) String condition,
                              @RequestParam(value = "name", required = false) String name,
                              Model model) {

        List<Disc> filteredDiscs = discService.findAll();

        if (name != null && !name.isEmpty()) {
            List<Disc> nameDiscs = discService.findByNameContainingIgnoreCase(name);
            if (!nameDiscs.isEmpty()) {
                filteredDiscs.retainAll(nameDiscs);
            }
        }


        if (fromPrice != null &&  toPrice != null) {
            filteredDiscs = discService.findByPrice(fromPrice, toPrice);
        } else {
            if (fromPrice != null) {
               filteredDiscs = discService.findByPrice(fromPrice, Integer.MAX_VALUE);
            }
            if (toPrice != null) {
                filteredDiscs = discService.findByPrice(0, toPrice);
            }
        }

        if(colour != null && !colour.isEmpty()) {
            List<Disc> colourFilteredDiscs = discService.findByColour(colour);
            if(!colourFilteredDiscs.isEmpty()){
                filteredDiscs = discService.findByColour(colour);
            }
        }

        if(condition != null && !condition.isEmpty()) {
            List<Disc> conditionFilteredDiscs = discService.findByCondition(condition);
            if (!conditionFilteredDiscs.isEmpty()) {
                filteredDiscs.retainAll(conditionFilteredDiscs);
            }
        }

        if (user != null) {
            model.addAttribute("LoggedInUser", user);
        }

        model.addAttribute("discs", filteredDiscs);
        model.addAttribute("return", true);

        return "home";
    }
}