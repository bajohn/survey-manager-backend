package com.staypal.server.handlers;
import com.staypal.db.Staypaldb;
import com.staypal.db.tables.AuthTokens;
import com.staypal.db.tables.Users;
import com.staypal.db.tables.records.UsersRecord;
import com.staypal.server.DatabaseConnector;
import com.staypal.server.Helpers;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.tools.json.JSONObject;
import org.jooq.tools.json.JSONParser;
import org.jooq.tools.json.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import static com.staypal.server.Helpers.parseParams;

/**
 * Created by bjohn454 on 1/16/2017.
 */
public class PasswordHandler extends RouterNanoHTTPD.GeneralHandler
{

    public JSONObject authenticate(String emailIn, String passwordIn) throws Exception
    {
        HashMap<String, String> resp = new HashMap<>();
        DSLContext db = DatabaseConnector.startConnect();
        Result res = db.selectFrom(Staypaldb.STAYPALDB.USERS).where(Staypaldb.STAYPALDB.USERS.EMAIL.equal(emailIn)).fetch();

        if(res.isEmpty())
        {
            resp.put("success", "false");
            resp.put("message", "User not found" + emailIn);
        }
        else  if(res.size()>1)
        {
            resp.put("success", "false");
            resp.put("message", "Multiple accounts found for " + emailIn);
        }
        else
        {
            UsersRecord rec = (UsersRecord) res.get(0);
            if(Arrays.equals(Base64.getDecoder().decode(rec.getPassword()),
                    getEncryptedPassword(passwordIn, Base64.getDecoder().decode(rec.getSalt()))))
            {
                SecureRandom randomNumber = new SecureRandom();

                byte[] accessToken = new byte[64]; //will be base64 encoded later
                byte[] refreshToken = new byte[64];

                randomNumber.nextBytes(accessToken); //generate access token
                randomNumber.nextBytes(refreshToken); //generate refresh token

                String accessTokenEncoded = Base64.getEncoder().encodeToString(accessToken);
                String refreshTokenEncoded = Base64.getEncoder().encodeToString(refreshToken);

                db.insertInto(AuthTokens.AUTH_TOKENS).
                        set(AuthTokens.AUTH_TOKENS.EMAIL, emailIn).
                        set(AuthTokens.AUTH_TOKENS.ACCESS_TOKEN, accessTokenEncoded).
                        set(AuthTokens.AUTH_TOKENS.REFRESH_TOKEN, refreshTokenEncoded).
                        set(AuthTokens.AUTH_TOKENS.TIME_ISSUED, Timestamp.valueOf(LocalDateTime.now())).execute();

                resp.put("success", "true");
                resp.put("password", "correct");
                resp.put("a_tkn", accessTokenEncoded );
                resp.put("r_tkn", refreshTokenEncoded );

            }
            else
            {
                resp.put("success", "true");
                resp.put("password", "incorrect");
            }

        }


        return new JSONObject(resp);

    }

    public byte[] getEncryptedPassword(String passwordIn, byte[] saltIn) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String algorithm = "PBKDF2WithHmacSHA1";

        int derivedKeyLength = 160;

        int iterations = 20000;

        KeySpec spec = new PBEKeySpec(passwordIn.toCharArray(), saltIn, iterations, derivedKeyLength);

        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

        return f.generateSecret(spec).getEncoded();
    }

    public JSONObject createUser(String email, String passwordIn) throws Exception
    {
        SecureRandom randomNumber = new SecureRandom();  //default secure random number; was .getInstance("SHA1PRNG");
        DSLContext db = DatabaseConnector.startConnect();
        Result res = db.selectFrom(Users.USERS).where(Users.USERS.EMAIL.equal(email)).fetch();

        if(res.isEmpty())
        {
            byte[] salt = new byte[64];
            randomNumber.nextBytes(salt); //generate salt

            //Record new Salt into database
            db.insertInto(Users.USERS).
                    set(Users.USERS.EMAIL, email).
                    set(Users.USERS.SALT, Base64.getEncoder().encodeToString(salt)).
                    set(Users.USERS.PASSWORD,  Base64.getEncoder().encodeToString(getEncryptedPassword(passwordIn, salt))).
                    execute();

            HashMap<String, String> resp = new HashMap<>();
            resp.put("success", "true");
            resp.put("message", "User created: " + email + ", " +  Base64.getEncoder().encodeToString(getEncryptedPassword(passwordIn, salt)));
            return new JSONObject(resp);
        }
        else
        {
            //throw new Exception("Multiple users found"); //This should never happen
            HashMap<String, String> resp = new HashMap<>();
            resp.put("success", "false");
            resp.put("message", "User already exists");
            return new JSONObject(resp);
        }

    }


//    public void savePassword(String email, String passwordIn) throws NoSuchAlgorithmException, Exception
//    {
//        SecureRandom randomNumber = SecureRandom.getInstance("SHA1PRNG"); //secure random number based on system attributes / sha1
//        DSLContext db = DatabaseConnector.startConnect();
//        Result res = db.selectFrom(Users.USERS).where(Users.USERS.EMAIL.equal(email)).fetch();
//
//        if(res.isEmpty())
//        {
//            throw new Exception("User not found");
//        }
//        else if(res.size()>1)
//        {
//            throw new Exception("Multiple users found"); //This should never happen
//        }
//        else
//        {
//            byte[] salt = new byte[64];
//            randomNumber.nextBytes(salt); //generate salt
//
//            //Record new Salt into database
//            db.insertInto(Users.USERS).
//                    set(Users.USERS.EMAIL, userName).
//                    set(Users.USERS.SALT, Base64.getEncoder().encodeToString(salt)).
//                    set(Users.USERS.PASSWORD,  Base64.getEncoder().encodeToString(getEncryptedPassword(passwordIn, salt))).
//                    execute();
//
//
//
//        }
//
//    }

    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session)
    {

        return NanoHTTPD.newFixedLengthResponse("GET");

    }

    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session)
    {

        try
        {

            HashMap<String, String> parsedParams = parseParams(session);
            String action = parsedParams.get("action");
            if(action.equals("create"))
            {
                return NanoHTTPD.newFixedLengthResponse(createUser(parsedParams.get("email"), parsedParams.get("password") ).toString());
            }
            else if(action.equals("auth"))
            {
                return NanoHTTPD.newFixedLengthResponse(authenticate(parsedParams.get("email"), parsedParams.get("password") ).toString());
            }
            else
            {
                return  NanoHTTPD.newFixedLengthResponse("Error: unrecognized action in password_handler: " + action);
            }
        }

        catch (Exception e) {
            return NanoHTTPD.newFixedLengthResponse("Error parsing POST params: " + e.toString());
        }


//        return NanoHTTPD.newFixedLengthResponse(("headers: " + session.getHeaders() + "\nparams: " + params.toString() + "\nmore params: " + session.getParms().toString() + "\nuri resource: " + uriResource.toString()) );
        //return Helpers.loadPage("index.html");

    }

}
