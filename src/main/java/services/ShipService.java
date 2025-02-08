package services;

import exceptions.InvalidDataException;
import models.Ship;
import repositories.interfaces.IShipRepository;
import validators.GameValidator;

import java.util.List;

public class ShipService {
    private final IShipRepository shipRepository;

    public ShipService(IShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public boolean placeShip(int gameId, int playerId, int x, int y, int size, String orientation, boolean sunk) {
        if (!GameValidator.isValidGameId(gameId) || playerId <= 0 || size <= 0) {
            throw new InvalidDataException("Invalid ship placement data.");
        }

        List<Ship> existingShips = shipRepository.getShipsByGameAndPlayer(gameId, playerId);

        if (!GameValidator.isPlacementValid(x, y, size, orientation, existingShips)) {
            return false;
        }

        Ship ship = new Ship(0, gameId, playerId, size, x, y, orientation, sunk);
        return shipRepository.createShip(ship);
    }

    public void displayBoard(int gameId, int playerId) {
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

                if (ship.getOrientation().equals("H")) {
                    x += i;
                } else {
                    y += i;
                }

                board[y][x] = ship.isSunk() ? 'X' : 'O';
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
}
