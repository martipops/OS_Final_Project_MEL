package shared;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerList implements Serializable {
    public int turnId;
    public List<PlayerProfile> profiles;

    public PlayerList() {
        turnId = 0;
        profiles = new ArrayList<PlayerProfile>();
    }

    public PlayerProfile getWhosTurn() {
        return profiles.stream()
                        .filter(x -> x.getId() == turnId)
                        .findFirst()
                        .get();
    }

    public void removePlayer(int playerId) {
        profiles.removeIf(x -> (x.getId() == playerId));
    }

    public void playerConnected(int playerId) {
        profiles.add(new PlayerProfile(playerId));
    }
    
}
