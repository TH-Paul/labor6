package map.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DBRepository<T> implements ICrudRepository<T> {

    public DBRepository() {
    }

    protected Connection openConnection(){
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/universitat?autoReconnect=true&useSSL=false", "root", "Paul2001");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void closeConnection(Connection con){
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
