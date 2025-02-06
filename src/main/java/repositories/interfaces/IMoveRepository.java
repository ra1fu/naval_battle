package repositories.interfaces;

import models.Move;
import java.util.List;

public interface IMoveRepository {
    boolean createMove(Move move);
    Move getMove(int id);
    List<Move> getMovesByGameId(int gameId);
    boolean updateMove(Move move);
    boolean deleteMove(int id);
}