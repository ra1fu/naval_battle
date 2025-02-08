package services;

import exceptions.GameNotFoundException;
import exceptions.InvalidDataException;
import models.Game;
import models.Move;
import models.Ship;
import repositories.interfaces.IGameRepository;
import repositories.interfaces.IMoveRepository;
import repositories.interfaces.IShipRepository;
import validators.GameValidator;

import java.time.LocalDateTime;
import java.util.List;

public class GameService {
    private final IGameRepository gameRepository;
    private final IMoveRepository moveRepository;
    private final IShipRepository shipRepository;

    public GameService(IGameRepository gameRepository, IMoveRepository moveRepository, IShipRepository shipRepository) {
        this.gameRepository = gameRepository;
        this.moveRepository = moveRepository;
        this.shipRepository = shipRepository;
    }

    public Game startGame(int player1Id, int player2Id) {
        if (player1Id <= 0 || player2Id <= 0) {
            throw new InvalidDataException("Invalid player IDs provided.");
        }

        Game game = new Game(0, player1Id, player2Id, player1Id, "IN_PROGRESS", null);

        if (!GameValidator.isValidGame(game)) {
            throw new InvalidDataException("Invalid game data.");
        }

        boolean success = gameRepository.createGame(game);
        if (!success) {
            throw new RuntimeException("Failed to create game.");
        }

        return game;
    }

    public void displayGameBoard(int gameId, int playerId) {
        char[][] board = new char[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = '~';
            }
        }

        List<Ship> ships = shipRepository.getShipsByGameAndPlayer(gameId, playerId);
        for (Ship ship : ships) {
            for (int i = 0; i < ship.getSize(); i++) {
                int x = ship.getStartX();
                int y = ship.getStartY();

                if (ship.getOrientation().equals("HORIZONTAL")) {
                    x += i;
                } else {
                    y += i;
                }

                board[y][x] = ship.isSunk() ? 'X' : 'O';
            }
        }

        List<Move> moves = moveRepository.getMovesByGameId(gameId);
        for (Move move : moves) {
            int x = move.getX();
            int y = move.getY();
            if (move.getResult().equals("HIT")) {
                board[y][x] = 'X';
            } else {
                board[y][x] = 'M';
            }
        }

        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 10; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean placeShip(int gameId, int playerId, int x, int y, int size, String orientation) {
        if (!GameValidator.isValidGameId(gameId) || playerId <= 0 || size <= 0) {
            System.out.println("Error: Incorrect ship parameters.");
            return false;
        }

        List<Ship> existingShips = shipRepository.getShipsByGameAndPlayer(gameId, playerId);

        if (!GameValidator.isPlacementValid(x, y, size, orientation, existingShips)) {
            System.out.println("Error: The ship cannot be placed in the specified location.");
            return false;
        }

        Ship ship = new Ship(0, gameId, playerId, size, x, y, orientation, false);

        boolean success = shipRepository.createShip(ship);

        if (!success) {
            System.out.println("Error: The ship could not be created in the database.");
            return false;
        }

        System.out.println("The ship has been successfully created:" + ship);
        displayGameBoard(gameId, playerId);
        return true;
    }

    public boolean makeMove(int gameId, int playerId, int x, int y) {
        Game game = gameRepository.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException("Игра с ID " + gameId + " не найдена.");
        }

        if (!game.isPlayerTurn(playerId)) {
            throw new InvalidDataException("It's not the player's turn right now " + playerId + ".");
        }

        boolean hit = gameRepository.checkHit(gameId, x, y);
        if (hit) {
            gameRepository.updateShipStatus(gameId, x, y);
        }

        Move move = new Move(0, gameId, playerId, x, y, hit ? "HIT" : "MISS", LocalDateTime.now());

        if (!GameValidator.validateMove(move)) {
            throw new InvalidDataException("Invalid move data.");
        }

        boolean moveCreated = moveRepository.createMove(move);
        if (!moveCreated) {
            throw new RuntimeException("Error when creating a move.");
        }

        if (gameRepository.isGameOver(gameId)) {
            game.setStatus("FINISHED");
            game.setWinnerId(playerId);
            gameRepository.updateGame(game);
            return true;
        }

        game.switchTurn();
        gameRepository.updateGame(game);
        return true;
    }

    public boolean isGameOver(int gameId) {
        if (!GameValidator.isValidGameId(gameId)) {
            return false;
        }
        return gameRepository.isGameOver(gameId);
    }
}
