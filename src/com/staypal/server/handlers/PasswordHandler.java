package com.staypal.server.handlers;
import com.staypal.db.tables.Users;
import com.staypal.server.DatabaseConnector;
import com.staypal.server.Helpers;
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
public class PasswordHandler extends RouterNanoHTTPD.GeneralHandler
{

    public boolean authenticate(String userName, String passwordIn) throws Exception
    {
        //Staypaldb.STAYPALDB.USERS.SALT
        return false;

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

    public void savePassword(String userName, String passwordIn) throws NoSuchAlgorithmException, Exception
    {
        SecureRandom randomNumber = SecureRandom.getInstance("SHA1PRNG"); //secure random number based on system attributes / sha1
        DSLContext db = DatabaseConnector.startConnect();
        Result res = db.selectFrom(Users.USERS).where(Users.USERS.USERNAME.equal(userName)).fetch();

        if(res.isEmpty())
        {
            throw new Exception("User not found");
        }
        else if(res.size()>1)
        {
            throw new Exception("Multiple users found"); //This should never happen
        }
        else
        {
            byte[] salt = new byte[64];
            randomNumber.nextBytes(salt); //generate salt

            //Record new Salt into database
            db.insertInto(Users.USERS).
                    set(Users.USERS.SALT, Base64.getEncoder().encodeToString(salt)).
                    set(Users.USERS.PASSWORD,  Base64.getEncoder().encodeToString(getEncryptedPassword(passwordIn, salt))).
                    execute();



        }

    }

    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session)
    {
        //return NanoHTTPD.newFixedLengthResponse("hola");
        return Helpers.loadPage("index.html");

    }

}
