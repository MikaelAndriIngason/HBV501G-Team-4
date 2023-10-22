package is.hi.hbv501g.hbv501gteam4.Controllers;

import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Conversation;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.Message;
import is.hi.hbv501g.hbv501gteam4.Persistence.Entities.User;
import is.hi.hbv501g.hbv501gteam4.Services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    ConversationService conversationService;
    UserService userService;
    MessageService messageService;


    @Autowired
    public ChatController(ConversationService conversationService, UserService userService, MessageService messageService) {
        this.conversationService = conversationService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @RequestMapping("/chat")
    public String chatPage(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if (sessionUser != null) {
            model.addAttribute("LoggedInUser", sessionUser);
        } else {
            return "redirect:/";
        }
        List<Conversation> allConversations = conversationService.findBySellerIdOrBuyerId(sessionUser.getId());

        List<User> contacts = new ArrayList<>();
        for (Conversation conversation : allConversations) {
            if (conversation.getBuyerID() == sessionUser.getId()) {
                contacts.add(userService.findById(conversation.getSellerID()));
            } else {
                contacts.add(userService.findById(conversation.getBuyerID()));
            }
        }


        //Add some data to the Model
        model.addAttribute("conversations", allConversations);
        model.addAttribute("contacts", contacts);
        model.addAttribute("activeConversation", false);

        return "chat";
    }

    @GetMapping("/chat/{id}")
    public String selectConversation(HttpSession session, @PathVariable("id") long conversationId, Model model) {
        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if (sessionUser != null) {
            model.addAttribute("LoggedInUser", sessionUser);
        } else {
            return "redirect:/";
        }

        Conversation selectedConversation = conversationService.findByConversationID(conversationId);
        List<Conversation> allConversations = conversationService.findBySellerIdOrBuyerId(sessionUser.getId());

        List<Message> allMessages = messageService.findByConversationID(conversationId);

        List<String> names = new ArrayList<>();
        for (Message message : allMessages) {
            if(message.getSenderID() == sessionUser.getId())
                names.add("You");
            else
                names.add(userService.findById(message.getSenderID()).getName());
        }

        //model.addAttribute("conversationTitle", selectedConversation.getConversationTitle());
        model.addAttribute("currentConversation", selectedConversation);
        model.addAttribute("conversations", allConversations);

        model.addAttribute("activeConversation", !selectedConversation.isConversationEnded());

        model.addAttribute("messages", allMessages);
        model.addAttribute("names", names);

        return "chat";
    }

    @GetMapping("/endChat/{conversationId}")
    public String endChat(@PathVariable("conversationId") long conversationId) {
        Conversation conversation = conversationService.findByConversationID(conversationId);
        if (conversation != null) {
            conversation.setConversationEnded(true);
            conversationService.save(conversation);
        }
        return "redirect:/chat/" + conversationId;
    }

    @GetMapping("/chat/refresh/{conversationId}")
    public String refreshChat(@PathVariable("conversationId") long conversationId) {
        return "redirect:/chat/" + conversationId;
    }

    @PostMapping("/send-message/{conversationId}")
    public String sendMessage(HttpSession session, @PathVariable("conversationId") long conversationId, @RequestParam("message") String messageText) {
        Conversation conversation = conversationService.findByConversationID(conversationId);

        if (conversation != null) {
            User sessionUser = (User) session.getAttribute("LoggedInUser");
            if (sessionUser != null) {
                Message message = new Message(conversationId, sessionUser.getId(), messageText);

                messageService.save(message);

                return "redirect:/chat/" + conversationId;
            }
        }
        return "redirect:/";
    }

    @GetMapping("/create-conversation/{sellerId}/{title}")
    public String createConversation(HttpSession session, @PathVariable("sellerId") long sellerId, @PathVariable("title") String title) {
        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if (sessionUser != null) {
            Conversation conversation = new Conversation(sessionUser.getId(), sellerId, title);

            conversationService.save(conversation);

            return "redirect:/chat/" + conversation.getConversationID();

        } else {
            return "redirect:/";
        }
    }
}
