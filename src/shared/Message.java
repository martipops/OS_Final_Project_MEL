package shared;
import java.io.Serializable;



public class Message implements Serializable {
    private static final long serialVersionUID = 4242995988408459990L;
    private String text;
    private CanvasInfo canvas;
    private MessageType type;

    public String getText() {
        return text;
    }

    public CanvasInfo getCanvas() {
        return canvas;
    }

    public MessageType getType() {
        return type;
    }

    public Message(String text) {
        this.type = MessageType.TEXT;
        this.text = text;
    }

    public Message(CanvasInfo canvas) {
        this.type = MessageType.CANVAS;
        this.text = "Placeholder";
        this.canvas = canvas;
    }

    public Message(MessageType t) {
        this.type = t;
        this.text = "Placeholder";
    }

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