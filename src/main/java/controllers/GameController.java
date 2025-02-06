package controllers;

import models.*;
import repositories.interfaces.*;

import java.util.List;
import java.util.Scanner;

public class GameController {
    private final IGameRepository gameRepository;
    private final IPlayerRepository playerRepository;
    private final IShipRepository shipRepository;
    private final IMoveRepository moveRepository;
    private final Scanner scanner;

    public GameController(IGameRepository gameRepository, IPlayerRepository playerRepository,
                          IShipRepository shipRepository, IMoveRepository moveRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.shipRepository = shipRepository;
        this.moveRepository = moveRepository;
        this.scanner = new Scanner(System.in);
    }

    /** Creates a new game between two players */
    public void createNewGame() {
        System.out.print("Enter the ID of the first player: ");
        int player1Id = scanner.nextInt();
        Player player1 = playerRepository.getPlayer(player1Id);

        if (player1 == null) {
            System.out.println("Player with ID " + player1Id + " not found.");
            return;
        }

        System.out.print("Enter the ID of the second player: ");
        int player2Id = scanner.nextInt();
        Player player2 = playerRepository.getPlayer(player2Id);

        if (player2 == null) {
            System.out.println("Player with ID " + player2Id + " not found.");
            return;
        }

        Game newGame = new Game(0, player1Id, player2Id, "IN_PROGRESS", 0);
        boolean gameCreated = gameRepository.createGame(newGame);
        if (gameCreated) {
            System.out.println("Game successfully created between " + player1.getName() + " and " + player2.getName());
        } else {
            System.out.println("Error while creating the game.");
        }
    }

    /** Places ships for a player */
    public void placeShips(int gameId, int playerId) {
        System.out.println("Placing ships for player " + playerId);
        System.out.print("Enter the number of ships: ");
        int shipCount = scanner.nextInt();

        for (int i = 0; i < shipCount; i++) {
            System.out.print("Enter X coordinate for the ship: ");
            int x = scanner.nextInt();
            System.out.print("Enter Y coordinate for the ship: ");
            int y = scanner.nextInt();
            System.out.print("Enter ship type (DESTROYER, CRUISER, BATTLESHIP): ");
            String type = scanner.next().toUpperCase();

            Ship newShip = new Ship(0, gameId, playerId, x, y, type, false);
            boolean shipPlaced = shipRepository.placeShip(newShip);
            if (shipPlaced) {
                System.out.println("Ship " + type + " successfully placed at (" + x + ", " + y + ")");
            } else {
                System.out.println("Error while placing the ship.");
            }
        }
    }

    /** Player makes a move (shoots at coordinates) */
    public void makeMove(int gameId, int playerId) {
        System.out.println("Player " + playerId + " is making a move.");
        System.out.print("Enter X coordinate: ");
        int x = scanner.nextInt();
        System.out.print("Enter Y coordinate: ");
        int y = scanner.nextInt();

        Move newMove = new Move(0, gameId, playerId, x, y, "PENDING");
        boolean moveMade = moveRepository.recordMove(newMove);
        if (moveMade) {
            System.out.println("Move recorded at (" + x + ", " + y + ")");
            updateGameStatus(gameId);
        } else {
            System.out.println("Invalid move. Try again.");
        }
    }

    /** Checks if a game has a winner */
    private void updateGameStatus(int gameId) {
        List<Ship> remainingShipsPlayer1 = shipRepository.getShipsByPlayer(gameId, 1);
        List<Ship> remainingShipsPlayer2 = shipRepository.getShipsByPlayer(gameId, 2);

        if (remainingShipsPlayer1.isEmpty()) {
            endGame(gameId, 2);
        } else if (remainingShipsPlayer2.isEmpty()) {
            endGame(gameId, 1);
        }
    }

    /** Ends the game and updates player ratings */
    public void endGame(int gameId, int winnerId) {
        boolean gameUpdated = gameRepository.updateGameStatus(gameId, "COMPLETED", winnerId);
        if (gameUpdated) {
            System.out.println("Game " + gameId + " has ended. Player " + winnerId + " is the winner!");
            playerRepository.updatePlayerRating(winnerId, 100); // Example: Winner gets +100 rating
        } else {
            System.out.println("Error while updating game status.");
        }
    }
}
