package com.staypal.server;

import com.staypal.server.handlers.PasswordHandler;
import com.staypal.server.pageloaders.IndexLoader;
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
        addRoute("/hello_world", PasswordHandler.class);
        addRoute("/", IndexLoader.class);
        //addRoute("/img", ImageLoader.class)
    }
}
