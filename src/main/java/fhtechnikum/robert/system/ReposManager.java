package fhtechnikum.robert.system;

import fhtechnikum.robert.application.packages.PackagesRepository;
import fhtechnikum.robert.application.user.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class ReposManager {

    private final Map<String, Repository> repositories;

    //TODO: Create the repositories and add them here
    public ReposManager() {
        repositories = new HashMap<>();

        UserRepository userRepo = new UserRepository();
        PackagesRepository packageRepo = new PackagesRepository();


        repositories.put("user", userRepo);
        repositories.put("package", packageRepo);

    }

    public Map<String, Repository> getRepositories() {
        return repositories;
    }

}
