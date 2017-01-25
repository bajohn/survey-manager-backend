package com.staypal.server;

import java.sql.Connection;
import org.apache.commons.dbcp2.BasicDataSource;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

/**
 * Created by bjohn454 on 1/16/2017.
 */
public class DatabaseConnector {
    public static DSLContext startConnect() throws Exception
    {
        BasicDataSource data_source = new BasicDataSource();
        data_source.setDriverClassName("com.mysql.jdbc.Driver");
        data_source.setUrl("jdbc:mysql://localhost:3306/javatwitter?useSSL=false");
        data_source.setUsername("root");
        data_source.setPassword("thisguy");

        data_source.setMaxTotal(160);
        data_source.setMaxIdle(10);
        data_source.setMinIdle(5);
        data_source.setInitialSize(5);
        data_source.setMinEvictableIdleTimeMillis(1800000);
        data_source.setTimeBetweenEvictionRunsMillis(1800000);
        data_source.setMaxWaitMillis(10000);
        data_source.setValidationQuery("SELECT 1");
        data_source.setTestOnBorrow(true);
        data_source.setTestOnReturn(true);
        data_source.setTestWhileIdle(true);

        Connection conn = data_source.getConnection();





        return DSL.using(conn);//, settings);
    }
}
