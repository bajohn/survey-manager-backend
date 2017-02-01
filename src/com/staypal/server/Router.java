package com.staypal.server;

import com.staypal.server.handlers.PasswordHandler;
import com.staypal.server.pageloaders.IndexBodyLoader;
import com.staypal.server.pageloaders.IndexLoader;
import com.staypal.server.pageloaders.LoginLoader;
import com.staypal.server.pageloaders.ResourceLoader;
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
        addRoute("/", IndexLoader.class);
        addRoute("/login", LoginLoader.class);
        addRoute("/index-body", IndexBodyLoader.class);
        addRoute("/hello_world", PasswordHandler.class);

        addRoute("/js/bootstrap.min.js", ResourceLoader.class);
        addRoute("/js/jquery.animate-colors-min.js", ResourceLoader.class);
        addRoute("/js/jquery-3.1.1.min.js", ResourceLoader.class);
        addRoute("/js/tether.js", ResourceLoader.class);

        addRoute("/js/mover.js", ResourceLoader.class);
        addRoute("/js/index.js", ResourceLoader.class);



    }
}
