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
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.PrintWriter;
import java.io.StringWriter;
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

import static com.staypal.server.Helpers.isAdmin;
import static com.staypal.server.Helpers.isAuthed;
import static com.staypal.server.Helpers.parseParams;

/**
 * Created by bjohn454 on 3/15/2017.
 */
public class SurveyHandler extends RouterNanoHTTPD.GeneralHandler {



    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session) {
        try {

            if (isAdmin(session))
            {
                HashMap<String, String> parsed = ( HashMap<String, String> ) session.getParms();
                DSLContext db = DatabaseConnector.startConnect();

                Result<UsersRecord> res =  db.selectFrom(Users.USERS).fetch();
                db.close();
                JSONArray outputArr = new JSONArray();

                for(UsersRecord rec :res)
                {
                    HashMap<String, String> localMap = new HashMap<>();
                    localMap.put("email", rec.getEmail());
                    localMap.put("phone", rec.getPhone());

                    localMap.put("name", rec.getName());
                    localMap.put("need_rides", rec.getNeedRides().toString());
                    localMap.put("need_food", rec.getNeedFood().toString());
                    localMap.put("need_childcare", rec.getNeedChildcare().toString());
                    localMap.put("need_housing", rec.getNeedHousing().toString());
                    localMap.put("need_translation", rec.getNeedTranslation().toString());
                    localMap.put("need_court", rec.getNeedCourt().toString());

                    localMap.put("nw", rec.getNw().toString());
                    localMap.put("ne", rec.getNe().toString());
                    localMap.put("sw", rec.getSw().toString());
                    localMap.put("se", rec.getSe().toString());
                    localMap.put("va", rec.getVa().toString());
                    localMap.put("md", rec.getMd().toString());

                    outputArr.add(new JSONObject(localMap));
                }

                return NanoHTTPD.newFixedLengthResponse(outputArr.toString());
            }
            else
            {
                return NanoHTTPD.newFixedLengthResponse("Error: not authenticated");
            }
        }
        catch(Exception e)
        {
            return NanoHTTPD.newFixedLengthResponse("Error parsing POST params: " + e.toString());
        }


    }
    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session) {
        HashMap<String, String> resp = new HashMap<>();
        try
        {





            if(isAuthed(session)) //todo: isAuthed changes the state of session so that getParms works later; fix this
            {
                HashMap<String, String> parsed = ( HashMap<String, String> ) session.getParms();
                DSLContext db = DatabaseConnector.startConnect();
                db.update(Users.USERS).set(Users.USERS.NAME, parsed.get("name"))
                        .set(Users.USERS.PHONE, parsed.get("phone"))

                        .set(Users.USERS.NEED_RIDES, new Byte( parsed.get("rides")))
                        .set(Users.USERS.NEED_FOOD, new Byte(parsed.get("food")))
                        .set(Users.USERS.NEED_CHILDCARE, new Byte(parsed.get("care")))
                        .set(Users.USERS.NEED_HOUSING, new Byte(parsed.get("house")))
                        .set(Users.USERS.NEED_TRANSLATION, new Byte(parsed.get("transl")))
                        .set(Users.USERS.NEED_COURT, new Byte(parsed.get("court")))

                        .set(Users.USERS.NW, new Byte(parsed.get("nw") ))
                        .set(Users.USERS.NE, new Byte(parsed.get("ne") ))
                        .set(Users.USERS.SW, new Byte(parsed.get("sw") ))
                        .set(Users.USERS.SE, new Byte(parsed.get("se") ))
                        .set(Users.USERS.MD, new Byte(parsed.get("md") ))
                        .set(Users.USERS.VA, new Byte(parsed.get("va")) )

                        .set(Users.USERS.OTHER_COMMENTS, parsed.get("comments"))
                        .set(Users.USERS.HOW_HEAR, parsed.get("howhear") )

                        .where(Users.USERS.EMAIL.equal(parsed.get("email"))).execute();
                        resp.put("success", "true");
                        resp.put("message", "db updated");

                        db.close();         return NanoHTTPD.newFixedLengthResponse(new JSONObject(resp).toString());
            }
            else
            {

                resp.put("success", "false");
                resp.put("message", "User not authenticated");

                return NanoHTTPD.newFixedLengthResponse(new JSONObject(resp).toString());
            }

        }
        catch (Exception e)
        {
            resp.put("success", "false");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            resp.put("message", "Error parsing POST params: " + e.toString() + sw.toString());
            return NanoHTTPD.newFixedLengthResponse(new JSONObject(resp).toString());
        }

        }
}
