package com.staypal.server.pageloaders;

import com.staypal.server.GeneralHandler;
import com.staypal.server.Helpers;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.util.Map;

import static com.staypal.server.Helpers.isAuthed;

/**
 * Created by bjohn454 on 4/1/2017.
 */

public class FooterLoader extends GeneralHandler {
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session)
    {

        try
        {
            return Helpers.loadPage("./pages/footer.html");
        } catch (Exception e  )
        {
            return(NanoHTTPD.newFixedLengthResponse(e.getMessage()));
        }

    }
}
