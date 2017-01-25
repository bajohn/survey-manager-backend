package com.staypal.server.pageloaders;

import com.staypal.server.GeneralHandler;
import com.staypal.server.Helpers;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.util.Map;

/**
 * Created by bjohn454 on 1/22/2017.
 */
public class IndexLoader extends GeneralHandler {
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session)
    {
        return Helpers.loadPage("index.html");
    }
}
