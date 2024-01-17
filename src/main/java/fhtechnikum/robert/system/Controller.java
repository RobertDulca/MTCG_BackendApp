package fhtechnikum.robert.system;

import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;


public interface Controller {
    Response response = new Response();
    EntitySerializer serializer = new EntitySerializer();
    Response process(Request request);
}
