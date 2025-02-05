import models.*;
import data.*;
import repositories.PlayerRepository;

import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PostgresDB PostgresDb = new PostgresDB("34.118.52.174", "rauan", "0000", "naval");


        PostgresDb.close();

    }
}
