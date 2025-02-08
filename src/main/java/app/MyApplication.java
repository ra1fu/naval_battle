package app;

import data.PostgresDB;
import models.Game;
import models.Player;
import repositories.*;
import services.GameService;
import services.PlayerService;
import services.ShipService;

import java.util.Scanner;

public class MyApplication {
    private final PlayerService playerService;
    private final GameService gameService;
    private final ShipService shipService;
    private final Scanner scanner;

    public MyApplication() {
        PostgresDB db = PostgresDB.getInstance("34.118.52.174", "rauan", "0000", "naval");

        PlayerRepository playerRepository = new PlayerRepository(db);
        GameRepository gameRepository = new GameRepository(db);
        ShipRepository shipRepository = new ShipRepository(db);
        MoveRepository moveRepository = new MoveRepository(db);

        this.playerService = new PlayerService(playerRepository);
        this.gameService = new GameService(gameRepository, moveRepository, shipRepository);
        this.shipService = new ShipService(shipRepository);
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Welcome to the Naval Battle Game!");

        Player player1 = getOrCreatePlayer();
        Player player2 = getOrCreatePlayer();

        Game game = gameService.startGame(player1.getPlayerId(), player2.getPlayerId());
        if (game == null) {
            System.out.println("Game creation failed.");
            return;
        }

        System.out.println("Game started! Game ID: " + game.getGameId());
        System.out.println("Players: " + player1.getName() + " vs " + player2.getName());

        placeShips(game.getGameId(), player1.getPlayerId());
        placeShips(game.getGameId(), player2.getPlayerId());

        System.out.println("All ships placed! The game is ready to start.");
        gameLoop(game);
    }

    private Player getOrCreatePlayer() {
        System.out.print("Enter player name: ");
        String name = scanner.nextLine();

        Player player = playerService.getPlayerByName(name);
        if (player == null) {
            player = new Player(0, name, 1000, 0, 0, 0);
            boolean created = playerService.createPlayer(player);
            if (created) {
                player = playerService.getPlayerByName(name);
                System.out.println("New player created: " + name);
            } else {
                System.out.println("Error creating player. Exiting.");
                System.exit(0);
            }
        } else {
            System.out.println("Player found: " + name);
        }
        return player;
    }

    private void placeShips(int gameId, int playerId) {
        System.out.println("Player " + playerId + ", place your ships on the board:");
        int[] shipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                System.out.println("Placing a ship of size " + size);
                shipService.displayBoard(gameId, playerId);

                System.out.print("Enter start X coordinate (0-9): ");
                int x = scanner.nextInt();
                System.out.print("Enter start Y coordinate (0-9): ");
                int y = scanner.nextInt();
                System.out.print("Enter orientation (H/V): ");
                String orientation = scanner.next().toUpperCase();

                placed = shipService.placeShip(gameId, playerId, x, y, size, orientation, false);
                if (!placed) {
                    System.out.println("Invalid ship placement. Try again.");
                }
            }
        }
    }

    private void gameLoop(Game game) {
        boolean gameOver = false;
        while (!gameOver) {
            shipService.displayBoard(game.getGameId(), game.getCurrentTurn());

            System.out.println("Player " + game.getCurrentTurn() + "'s turn.");
            System.out.print("Enter target X coordinate (0-9): ");
            int x = scanner.nextInt();
            System.out.print("Enter target Y coordinate (0-9): ");
            int y = scanner.nextInt();

            boolean moveSuccessful = gameService.makeMove(game.getGameId(), game.getCurrentTurn(), x, y);
            if (!moveSuccessful) {
                System.out.println("Invalid move, try again.");
            }

            gameOver = gameService.isGameOver(game.getGameId());
        }

        System.out.println("Game over! Winner: Player " + game.getWinnerId());
    }

    public static void main(String[] args) {
        new MyApplication().run();
    }
}
