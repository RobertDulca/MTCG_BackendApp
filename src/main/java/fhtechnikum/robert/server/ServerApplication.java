package fhtechnikum.robert.server;

import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;

public interface ServerApplication {

    Response handle(Request request);
}
