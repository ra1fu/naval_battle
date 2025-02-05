package models;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int size;
    private char[][] grid;
    private List<Ship> ships;

    public Board(int size) {
        this.size = size;
        this.grid = new char[size][size];
        this.ships = new ArrayList<>();
        Grid();
    }

    private void Grid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = '.';
            }
        }
    }

    public boolean placeShip(Ship ship) {
        if (!canPlaceShip(ship)) {
            return false;
        }

        for (int i = 0; i < ship.getSize(); i++) {
            int x = ship.getStartX() + (ship.getOrientation().equals("Horizontal") ? i : 0);
            int y = ship.getStartY() + (ship.getOrientation().equals("Horizontal") ? 0 : i);
            grid[x][y] = 'S';
        }
        ships.add(ship);
        return true;
    }

    public boolean canPlaceShip(Ship ship) {
        for (int i = 0; i < ship.getSize(); i++) {
            int x = ship.getStartX() + (ship.getOrientation().equals("Horizontal") ? i : 0);
            int y = ship.getStartY() + (ship.getOrientation().equals("Horizontal") ? 0 : i);

            if (x < 0 || x >= size || y < 0 || y >= size || grid[x][y] != '.') {
                return false;
            }
        }
        return true;
    }

    public String receiveShot(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return "Miss";
        }
        if (grid[x][y] == 'S') {
            grid[x][y] = 'X';

            for (Ship ship : ships) {
                for (int i = 0; i < ship.getSize(); i++) {
                    int shipX = ship.getStartX() + (ship.getOrientation().equals("Horizontal") ? i : 0);
                    int shipY = ship.getStartY() + (ship.getOrientation().equals("Horizontal") ? 0 : i);

                    if (shipX == x && shipY == y) {
                        ship.hit();
                        return ship.isSunk() ? "Sunk" : "Hit";
                    }
                }
            }
        }
        grid[x][y] = 'O';
        return "Miss";
    }

    public void displayOwnBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void displayOpponentBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == 'S') {
                    System.out.print(". ");
                } else {
                    System.out.print(grid[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public char[][] getGrid() {
        return grid;
    }

    public void setGrid(char[][] grid) {
        this.grid = grid;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }
}