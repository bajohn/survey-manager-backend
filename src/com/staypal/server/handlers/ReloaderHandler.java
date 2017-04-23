package com.staypal.server.handlers;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.util.Map;

/**
 * Created by bjohn454 on 3/18/2017.
 */
public class ReloaderHandler extends RouterNanoHTTPD.GeneralHandler {
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session) {
        try {
            String[] cmd = new String[]{"/bin/sh", "/var/www/backend/mysql.restart.bash"};
            Process pr = Runtime.getRuntime().exec(cmd);
            return NanoHTTPD.newFixedLengthResponse("Server refreshed: ");
        } catch (Exception e) {
            return NanoHTTPD.newFixedLengthResponse("Error: " + e.toString());
        }
    }}
