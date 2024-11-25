package shared;
import java.io.Serializable;


/**
 * The Message class represents an object that can be sent between the server and the client.
 * Three types of messages are supported: text, canvas, and `not your turn`. See @MessageType
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 4242995988408459990L;
    private String text;
    private CanvasInfo canvas;
    private MessageType type;

    /**
     * Get the text of the message.
     * 
     * @return the text of the message
     */
    public String getText() {
        return text;
    }

    /**
     * Get the canvas information of the message.
     * 
     * @return the canvas information of the message
     */
    public CanvasInfo getCanvas() {
        return canvas;
    }

    /**
     * Get the type of the message.
     * 
     * @return the type of the message
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Create a message with the given text.
     * 
     * @param text the text of the message
     */
    public Message(String text) {
        this.type = MessageType.TEXT;
        this.text = text;
    }

    /**
     * Create a message with the given canvas information.
     * 
     * @param canvas the canvas information
     */
    public Message(CanvasInfo canvas) {
        this.type = MessageType.CANVAS;
        this.text = "Placeholder";
        this.canvas = canvas;
    }

    /**
     * Create a message with the given type.
     * 
     * @param type the type of the message
     */
    public Message(MessageType type) {
        this.type = type;
        this.text = "Placeholder";
    }

    /**
     * Get the string representation of the message.
     * 
     * @return the string representation of the message
     */
    @Override
    public String toString() {
        switch (this.type) {
            case CANVAS:
                return canvas.toString();
            case TEXT:
                return text;
            default:
                break;
        }        
        return text;
    }
}