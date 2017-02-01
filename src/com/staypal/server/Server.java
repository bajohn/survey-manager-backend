package com.staypal.server;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.util.ServerRunner;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

import java.io.IOException;
import java.util.logging.*;

/**
 * Created by bjohn454 on 1/16/2017.
 */
public class Server extends NanoHTTPD{

    private Router router;

    public Server() throws IOException
    {
        super(8500);
        router = new Router();
        router.start(5000,true);
        start();
    }

    public synchronized void waitCall() throws InterruptedException, IOException
    {
        wait();
    }



    public static void main(String[] args)
    {
        try {
            Server serverLocal = new Server();
            serverLocal.waitCall();


        } catch (IOException|InterruptedException exc) {
            System.out.println("Couldn't start server:\n" + exc);
        }

    }

}
