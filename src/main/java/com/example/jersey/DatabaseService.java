package com.example.jersey;

import java.sql.*;

import org.codehaus.jettison.json.JSONObject;
import java.util.*;

public class DatabaseService {

    public static Connection createDatabaseConnection() throws SQLException, ClassNotFoundException {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        Class.forName( driver );
        String url = "jdbc:derby:sampleDB;create=true";
        Connection c = DriverManager.getConnection(url);
        return c;
    }

    public static boolean tableExists ( Connection con, String table ) {
        int numRows = 0;
        try {
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables( null, "APP", table.toUpperCase(), null);
            while( rs.next() ) ++numRows;
        } catch ( SQLException e ) {
            String theError = e.getSQLState();
            System.out.println( "Can't query DB metadata: " + theError );
            System.exit( 1 );
        }
        return numRows > 0;
    }

    public static JSONObject createNote ( String input ) throws Exception {
        JSONObject result = new JSONObject();
        Integer idValue = 0;
        Connection connection = createDatabaseConnection();
        Statement statement = connection.createStatement();

        if ( !tableExists(connection, "savedNotes") ) {
            statement.execute("create table savedNotes(id int, note varchar(100))");
        }

        PreparedStatement pCount = connection.prepareStatement("select count(*) as rowCount from savedNotes");
        ResultSet countResult = pCount.executeQuery();
        while ( countResult.next() ) {
            idValue = countResult.getInt( "rowCount" ) + 1;
        }

        PreparedStatement pInsert = connection.prepareStatement("insert into savedNotes values (?, ?)");
        pInsert.setInt( 1, idValue );
        pInsert.setString( 2, input );
        pInsert.executeUpdate();

        result.put( "id", idValue );
        result.put( "note", input );

        connection.close();
        pInsert.close();
        pCount.close();
        return result;
    }

    public static JSONObject getNote ( Integer id ) throws Exception {
        JSONObject result = new JSONObject();
        String note = "There is no note";
        Connection connection = createDatabaseConnection();
        PreparedStatement dbStatement = connection.prepareStatement("SELECT * FROM savedNotes where id = (?)");
        dbStatement.setInt(1,id);
        ResultSet dbResult = dbStatement.executeQuery();
        if ( dbResult.next() ) {
            note = dbResult.getString( "note" );
        }

        dbStatement.close();
        connection.close();
        result.put( "id", id );
        result.put( "note", note );
        return result;
    }

    public static JSONObject getAllNotes ( String query ) throws Exception {
        Connection connection = createDatabaseConnection();
        Map<Integer,String> noteMap = new HashMap<Integer,String>();
        String noteSql;
        PreparedStatement dbStatement;

        if ( query == null || query == "" ) {
            noteSql = "SELECT * FROM savedNotes";
            dbStatement = connection.prepareStatement( noteSql );
        } else {
            noteSql = "SELECT * FROM savedNotes WHERE note LIKE (?)";
            dbStatement = connection.prepareStatement( noteSql );
            dbStatement.setString( 1, "%" + query + "%" );
        }

        ResultSet dbResult = dbStatement.executeQuery();
        ResultSetMetaData meta = dbResult.getMetaData();
        Integer numColumns = meta.getColumnCount();
        while ( dbResult.next() ) {
            for ( int i = 0; i <= numColumns; ++i ) {
                Integer id = dbResult.getInt( "id" );
                String note = dbResult.getString( "note" );
                noteMap.put ( id, note );
            }
        }
        return new JSONObject( noteMap );
    }

}
