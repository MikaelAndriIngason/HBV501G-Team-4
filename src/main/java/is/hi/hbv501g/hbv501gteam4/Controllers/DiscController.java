package is.hi.hbv501g.hbv501gteam4.Controllers;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Disc;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Image;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.User;
import is.hi.hbv501g.hbv501gteam4.Services.DiscService;
import is.hi.hbv501g.hbv501gteam4.Services.ImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.springframework.http.*;

@Controller
public class DiscController {

    DiscService discService;
    ImageService imageService;

    public DiscController(DiscService discService, ImageService imageService) {
        this.discService = discService;
        this.imageService = imageService;
    }

    @RequestMapping("/home")
    public String indexPageLoggedIn(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if (sessionUser != null) {
            model.addAttribute("LoggedInUser", sessionUser);
        } else {
            return "redirect:/";
        }
        List<Disc> allDiscs = discService.findAll();

        //Add some data to the Model
        model.addAttribute("discs", allDiscs);
        return "home";
    }

    @RequestMapping(value = "/addDisc", method = RequestMethod.GET)
    public String addDiscGET(Disc disc, Model model) {
        return "addDisc";
    }


    @RequestMapping(value = "/addDisc", method = RequestMethod.POST)
    public String addDiscPOST(Disc disc, BindingResult result, Model model, @RequestParam("image") MultipartFile[] images){
        if (result.hasErrors()) {
            return "addDisc";
        }

        Disc discSaved = discService.save(disc);
        addImage(discSaved, images);
        return "redirect:/home";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String updateDiscGET(@PathVariable("id") long id, Model model) {
        Disc discToUpdate = discService.findBydiscID(id);
        model.addAttribute("disc", discToUpdate);
        return "updateDisc";
    }

    @RequestMapping(value = "/updateDisc", method = RequestMethod.POST)
    public String updateDiscPOST(Disc disc, BindingResult result, Model model, @RequestParam("image") MultipartFile[] images) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "redirect:/home";
        }
        //List<Image> imagesPrev = disc.getImages();  // Get the list of existing images

        //List<Image> imageList = addImageUpdate(disc, images, imagesPrev);
        //disc.setImages(imageList);
        Disc discSaved = discService.save(disc);
        addImage(discSaved, images);
        return "redirect:/home";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteDisc(@PathVariable("id") long id, Model model) {
        Disc discToDelete = discService.findBydiscID(id);
        discService.delete(discToDelete);
        return "redirect:/home";
    }

    @RequestMapping(value = "/deleteImage/{id}/{imageId}", method = RequestMethod.GET)
    public String deleteImage(@PathVariable("id") long id,@PathVariable("imageId") long imageID,Model model) {
        Image imageToDelete = imageService.findByimageID(imageID);
        deleteImage(imageToDelete);
        imageService.delete(imageToDelete);
        return String.format("redirect:/update/%s", id);
    }

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
}