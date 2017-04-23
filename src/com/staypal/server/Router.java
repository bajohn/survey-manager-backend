package com.staypal.server;

import com.staypal.server.handlers.PasswordHandler;
import com.staypal.server.handlers.ReloaderHandler;
import com.staypal.server.handlers.SurveyHandler;
import com.staypal.server.pageloaders.*;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.io.IOException;

/**
 * Created by bjohn454 on 1/16/2017.
 */
public class Router extends RouterNanoHTTPD{
    static int PORT = 8501;
    public Router() throws IOException
    {
        super(PORT);
        addMappings();
    }

    @Override
    public void addMappings()
    {
        super.addMappings();
        //html pages that require some backend processing
        addRoute("/", IndexLoader.class);
        addRoute("/login", LoginLoader.class);
        addRoute("/join", JoinLoader.class);
        addRoute("/password_handler", PasswordHandler.class);
        addRoute("/survey", SurveyHandler.class);
        addRoute("/header", HeaderLoader.class);
        addRoute("/homepage", HomeLoader.class);
        addRoute("/footer", FooterLoader.class);
        addRoute("/find", FindLoader.class);
        addRoute("/profile", ProfileLoader.class);
        addRoute("/restart", ReloaderHandler.class);

        //html pages loaded raw. This is unsafe / should only be used for temporary pages.
        addRoute("/pages/coming-soon.html", ResourceLoader.class);
        //addRoute("/pages/login-header.html", ResourceLoader.class);

        //javascript libraries
        addRoute("/js/bootstrap.min.js", ResourceLoader.class);
        addRoute("/js/jquery.animate-colors-min.js", ResourceLoader.class);
        addRoute("/js/jquery-3.1.1.min.js", ResourceLoader.class);
        addRoute("/js/tether.js", ResourceLoader.class);

        //custom js
        addRoute("/js/mover.js", ResourceLoader.class);
        addRoute("/js/join.js", ResourceLoader.class);
        addRoute("/js/login.js", ResourceLoader.class);
        addRoute("/js/profile.js", ResourceLoader.class);
        addRoute("/js/admin.js", ResourceLoader.class);



    }
}
