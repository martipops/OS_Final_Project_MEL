package shared;
import java.io.Serializable;



public class Message implements Serializable {
    private static final long serialVersionUID = 4242995988408459990L;
    private String text;
    private CanvasInfo canvas;
    private MessageType type;
    private PlayerList players;
    private PlayerProfile playerInfo;

    public String getText() {
        return text;
    }

    public CanvasInfo getCanvas() {
        return canvas;
    }

    public MessageType getType() {
        return type;
    }

    public PlayerList getPlayers() {
        return players;
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

    public Message(PlayerList players) {
        this.type = MessageType.PLAYERLIST;
        this.text = "Placeholder";
        this.players = players;
    }

    public Message(PlayerProfile playerInfo) {
        this.type = MessageType.PLAYERPROFILE;
        this.text = "Placeholder";
        this.playerInfo = playerInfo;
    }

    @Override
    public String toString() {
        switch (this.type) {
            case CANVAS:
                return canvas.toString();
            case TEXT:
                return text;
            case PLAYERLIST:
                return players.toString();
            case PLAYERPROFILE:
                return playerInfo.toString();
        }        
        return text;
    }
}