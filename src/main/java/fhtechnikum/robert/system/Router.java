package fhtechnikum.robert.system;


import fhtechnikum.robert.application.user.UserController;
import fhtechnikum.robert.application.user.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Controller> handler = new HashMap<>();
    private final Map<String, Repository> repositories;

    //TODO: Create the controllers and them here
    public Router(Map<String, Repository> repositories) {
        this.repositories = repositories;

        UserController userController = new UserController((UserRepository) repositories.get("user"));


        handler.put("/users", userController);

    }

    public Controller route(String path) {
        return handler.get(path);
    }
}
