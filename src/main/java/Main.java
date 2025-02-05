import data.interfaces.IDB;
import models.*;
import data.*;
import repositories.GameRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        IDB db = new PostgresDB("34.118.52.174", "rauan", "0000", "naval");

        db.close();

    }
}
