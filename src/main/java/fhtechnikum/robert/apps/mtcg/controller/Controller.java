package fhtechnikum.robert.apps.mtcg.controller;

import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;

public interface Controller {

    boolean supports(String route);

    Response handle(Request request);
}
