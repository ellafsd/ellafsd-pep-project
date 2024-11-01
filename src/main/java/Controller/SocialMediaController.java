package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

     private final AccountService accountService;
     private final MessageService messageService;
 
     public SocialMediaController() {
         this.accountService = new AccountService();
         this.messageService = new MessageService();
     }


    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserId);

        return app;
    }

    private void registerUser(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account result = accountService.registerAccount(account);
        if (result != null) {
            ctx.json(result).status(200);
        } else {
            ctx.status(400);
        }
    }

    private void loginUser(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account result = accountService.loginAccount(account);
        if (result != null) {
            ctx.json(result).status(200);
        } else {
            ctx.status(401);
        }
    }

    private void createMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Message result = messageService.createMessage(message);
        if (result != null) {
            ctx.json(result).status(200);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessages(Context ctx) {
        ctx.json(messageService.getAllMessages()).status(200);
    }

    private void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message result = messageService.getMessageById(messageId);
        if (result != null) {
            ctx.json(result).status(200);
        } else {
            ctx.status(200);  // Empty response for non-existent message
        }
    }

    private void deleteMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message result = messageService.deleteMessageById(messageId);
        if (result != null) {
            ctx.json(result).status(200);
        } else {
            ctx.status(200);  // Empty response for non-existent message
        }
    }

    private void updateMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        
        // Parse JSON to extract only the "message_text" field
        String newText = ctx.bodyAsClass(Message.class).getMessage_text();

        Message updatedMessage = messageService.updateMessage(messageId, newText);

        if (updatedMessage != null) {
            ctx.json(updatedMessage).status(200);  // Return updated message
        } else {
            ctx.status(400);  // Return 400 if update was not successful (e.g., empty message_text)
        }
    }
    

    private void getMessagesByUserId(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getMessagesByUserId(accountId)).status(200);
    }



    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}