package repositories.interfaces;

import models.Game;
import models.GameDetails;
import java.util.List;

public interface IGameRepository {
    boolean createGame(Game game);
    Game getGame(int id);
    List<Game> getAllGames();
    boolean updateGame(Game game);
    boolean deleteGame(int id);
    GameDetails getFullGameDetails(int gameId);;
}