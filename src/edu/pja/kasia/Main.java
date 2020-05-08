package edu.pja.kasia;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static int port = 5000;

    public static void main(String[] args) {
        new MainMenu();

        final ExecutorService newThreadPool = Executors.newFixedThreadPool(5);
        newThreadPool.execute(new Server(port));

        Database.getInstance().connect();
        Database.getInstance().createInitTables();
    }
}
