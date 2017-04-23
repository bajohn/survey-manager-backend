package com.staypal.server;

import java.sql.Connection;
import org.apache.commons.dbcp2.BasicDataSource;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

/**
 * Created by bjohn454 on 1/16/2017.
 */
public class  DatabaseConnector {

    public static BasicDataSource stored_data_source = new BasicDataSource();;


    static {
        stored_data_source.setDriverClassName("com.mysql.jdbc.Driver");
        stored_data_source.setUrl("jdbc:mysql://localhost:3306/staypaldb?useSSL=false");
        stored_data_source.setUsername("jooquser");
        stored_data_source.setPassword("jooquserpassword");

        stored_data_source.setMaxTotal(5000); //todo: make sure these don't get crazy high
        stored_data_source.setMaxIdle(10);
        stored_data_source.setMinIdle(5);
        stored_data_source.setInitialSize(5);
        stored_data_source.setMinEvictableIdleTimeMillis(10000);
        stored_data_source.setTimeBetweenEvictionRunsMillis(10000);
        stored_data_source.setMaxWaitMillis(10000);
        stored_data_source.setValidationQuery("SELECT 1");
        stored_data_source.setTestOnBorrow(true);
        stored_data_source.setTestOnReturn(true);
        stored_data_source.setTestWhileIdle(true);


    }
    public static DSLContext startConnect() throws Exception
    {


        Connection conn = stored_data_source.getConnection();

        return DSL.using(conn);//, settings);
    }
}
