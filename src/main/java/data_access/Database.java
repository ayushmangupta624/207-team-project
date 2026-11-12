/**
 * This file will set up connections to the database and run the schema. schema.sql will contain CREATE TABLE statements.
 *
 *
  */

package data_access;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {

    private final String url;

    public Database(String url){
        this.url = url;
    }

    /*
    Essentially want to create and store url to the database file.
    On my (Ayushman's) machine this is /home/<username>/.quercuspro/quercuspro.db
     */
    public static Database atDefaultLocation() throws IOException{
        //Get the file system path to the user's home directory
        Path dir = Paths.get(System.getProperty("user.home"), ".quercuspro");
        //Essentially mkdir -p, can also throw IOException if e.g. no write perms
        Files.createDirectories(dir);
        //Call Database constructor and store db file url. sqlite will create file lazily during first connection.
        return new Database("jdbc:sqlite:" + dir.resolve("quercuspro.db"));
    }

    /*
    Return a Connection, i.e. a jdbc object representing an open session with the db.
     */
    public Connection connect() throws SQLException{
        // open a connection (also creates db file if it doesn't exist)
        Connection conn = DriverManager.getConnection(url);
        /*
        Statement is an object used to send SQL to the db.
        PRAGMA is SQLite’s command for changing database settings.
        SQLite ignores foreign key constraints by default.
        without this line REFERENCES and ON DELETE CASCADE would be ignored.
         */
        try (Statement s = conn.createStatement()){
            s.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    public void initSchema() throws IOException, SQLException {
        String sql;
        try (InputStream in = getClass().getResourceAsStream("/schema.sql")){
            sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
        try (Connection conn = connect(); Statement s = conn.createStatement()) {
            for (String stmt : sql.split(";")) {
                if (!stmt.isBlank()) s.execute(stmt);
            }
        }

    }

}
