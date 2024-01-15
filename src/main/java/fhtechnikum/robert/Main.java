package fhtechnikum.robert;

import fhtechnikum.robert.server.Server;
import fhtechnikum.robert.system.ReposManager;
import fhtechnikum.robert.system.Router;

public class Main {
    public static void main(String[] args) {
        ReposManager repoManager = new ReposManager();
        Router router = new Router(repoManager.getRepositories());
        Server server = new Server(router);

        server.start();
    }
}