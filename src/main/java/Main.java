import data.PostgresDB;
import exceptions.InvalidDataException;
import exceptions.PlayerNotFoundException;
import models.Game;
import models.Player;
import repositories.GameRepository;
import repositories.MoveRepository;
import repositories.PlayerRepository;
import repositories.ShipRepository;
import services.GameService;
import services.PlayerService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PostgresDB db = PostgresDB.getInstance("34.118.52.174", "rauan", "0000", "naval");


        db.close();

    }
}
