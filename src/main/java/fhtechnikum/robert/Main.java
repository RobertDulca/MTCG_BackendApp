package fhtechnikum.robert;

import fhtechnikum.robert.apps.mtcg.MtcgApp;
import fhtechnikum.robert.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(new MtcgApp());
        server.start();
    }
}