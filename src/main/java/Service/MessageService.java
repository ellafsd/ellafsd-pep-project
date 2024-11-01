package Service;
import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
    private final MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
            return null;
        }
        
        if (message.getPosted_by() <= 0) {
            return null;
        }
        
        return messageDAO.saveMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId) {
        Message message = messageDAO.getMessageById(messageId);
        if (message != null) {
            messageDAO.deleteMessageById(messageId);
        }
        return message;
    }

    public Message updateMessage(int messageId, String newText) {
        if (newText == null || newText.trim().isEmpty() || newText.length() > 255) {
            return null; // Return null to indicate a failed update due to invalid input
        }
        
        return messageDAO.updateMessage(messageId, newText);
        }

    public List<Message> getMessagesByUserId(int accountId) {
        return messageDAO.getMessagesByUserId(accountId);
    }
}
