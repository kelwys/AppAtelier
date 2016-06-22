package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }
    public Result account() {
        return ok(views.html.site.account.render("Your new application is ready."));
        
    }
//    public Result register() {
//        return ok(views.html.site.account.render("Your new application is ready."));
//        
//    }

}
