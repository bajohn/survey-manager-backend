package com.staypal.server;

/**
 * Created by bjohn454 on 1/22/2017.
 */

import com.staypal.db.tables.Users;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.jooq.DSLContext;
import org.jooq.Result;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Map;


import com.staypal.db.tables.Users;
import com.staypal.server.DatabaseConnector;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.jooq.DSLContext;
import org.jooq.Result;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Created by bjohn454 on 1/16/2017.
 */
public class GeneralHandler implements RouterNanoHTTPD.UriResponder
{

    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session)
    {
        return NanoHTTPD.newFixedLengthResponse("Warning- PUT not implemented");
    }
    public NanoHTTPD.Response put(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session)
    {
        return NanoHTTPD.newFixedLengthResponse("Warning- PUT not implemented");
    }
    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session)
    {
        return NanoHTTPD.newFixedLengthResponse("Warning- POST not implemented");
    }
    public NanoHTTPD.Response delete(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session)
    {
        return NanoHTTPD.newFixedLengthResponse("Warning- DELETE not implemented");
    }
    public NanoHTTPD.Response other(String method, RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session)
    {
        return NanoHTTPD.newFixedLengthResponse("Warning- unrecognized HTTP method: " + method);
    }


}
