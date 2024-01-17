package fhtechnikum.robert.system;

import fhtechnikum.robert.application.user.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class ReposManager {

    private final Map<String, Repository> repositories;

    //TODO: Create the repositories and add them here
    public ReposManager() {
        repositories = new HashMap<>();

        UserRepository userRepo = new UserRepository();


        repositories.put("user", userRepo);

    }

    public Map<String, Repository> getRepositories() {
        return repositories;
    }

}
