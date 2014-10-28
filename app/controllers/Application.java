package controllers;

import play.mvc.*;

public class Application extends Controller {

    public static Result index() {
        return ok("Your new application is ready.");
    }

}
