package fhtechnikum.robert.system;

import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;


public interface Controller {
    Response response = new Response();
    //TODO: Model Serializer needs to be done to create the body of the response
    ModelSerializer serializer = new ModelSerializer();
    Response process(Request request);
}
