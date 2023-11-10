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

    /**
     * Opens up the account page if a user is logged in
     * @param session the user session
     * @param model
     * @return redirects to the chat page if logged in, otherwise to the index page
     */
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

    /**
     * Opens up a conversation between two users
     * @param session the user session
     * @param conversationId the id of the conversation
     * @param model
     * @return displays the conversation
     */
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

        boolean deletedUser = false;

        List<String> names = new ArrayList<>();
        for (Message message : allMessages) {
            if(message.getSenderID() == sessionUser.getId()) {
                names.add("You");
            } else {
                User user = userService.findById(message.getSenderID());
                if (user != null) {
                    names.add(user.getName());
                } else {
                    names.add("Deleted user");
                    deletedUser = true;
                }
            }
        }

        if (deletedUser) {
            selectedConversation.setConversationEnded(true);
            conversationService.save(selectedConversation);
        }

        model.addAttribute("currentConversation", selectedConversation);
        model.addAttribute("conversations", allConversations);

        model.addAttribute("activeConversation", !selectedConversation.isConversationEnded());

        model.addAttribute("messages", allMessages);
        model.addAttribute("names", names);

        return "chat";
    }

    /**
     * Ends a conversation between two users
     * @param conversationId the id of the conversation
     * @return redirects back to the chat page
     */
    @GetMapping("/endChat/{conversationId}")
    public String endChat(@PathVariable("conversationId") long conversationId) {
        Conversation conversation = conversationService.findByConversationID(conversationId);
        if (conversation != null) {
            conversation.setConversationEnded(true);
            conversationService.save(conversation);
        }
        return "redirect:/chat/" + conversationId;
    }

    /**
     * Refreshes a conversation to pull new messages (if any)
     * @param conversationId the id of the conversation
     * @return redirects to the same page (refresh)
     */
    @GetMapping("/chat/refresh/{conversationId}")
    public String refreshChat(@PathVariable("conversationId") long conversationId) {
        return "redirect:/chat/" + conversationId;
    }

    /**
     * Sends a message in a conversation
     * @param session the user session
     * @param conversationId the id of the conversation
     * @param messageText the message
     * @return redirects to the same page (refresh)
     */
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

    /**
     * Creates a conversation between two users
     * @param session the user session
     * @param sellerId the id of the seller
     * @param title the title of the conversation
     * @return redirects to the new conversation
     */
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

    /**
     * Starts a conversation with customer service
     * @param session the user session
     * @return redirects to chats and opens the conversation with Customer Service
     */
    @GetMapping("/create-conversation/customer-service")
    public String createConversationCustomerService(HttpSession session) {
        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if (sessionUser != null) {
            Conversation conversation = new Conversation(sessionUser.getId(), 10, "Customer Service");

            conversationService.save(conversation);

            return "redirect:/chat/" + conversation.getConversationID();

        } else {
            return "redirect:/";
        }
    }
}
