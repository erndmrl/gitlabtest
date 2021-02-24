package testprojectcore.util;

import testprojectcore.dataprovider.EnvironmentDataProvider;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.sql.*;


/**
 * @author Eren Demirel
 */
public class DatabaseUtil {

    final Logger logger = Logger.getLogger(this.getClass());

    private Connection connection;

    private String url;

    private String username;

    private String password;


    public DatabaseUtil(String username, String password) throws Exception {
        this.url= EnvironmentDataProvider.DATABASE.getPropertyValue("url");
        this.username = EnvironmentDataProvider.DATABASE.getPropertyValue("username");
        this.password =EnvironmentDataProvider.DATABASE.getPropertyValue("password");
    }

    public String getUrl() {
        return this.url;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                Class.forName("oracle.jdbc.OracleDriver");
                this.connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
                return this.connection;
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e);
        }
        return this.connection;
    }

    public Statement getStatement() {
        Statement stmt = null;
        try {
            stmt = this.getConnection().createStatement();
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
        return stmt;
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                Assert.fail(e.getMessage());
            }
        }
    }

    public ResultSet executeSqlQueryAndGetResultSet(String query) {
        ResultSet rs = null;
        try {
            rs = getStatement().executeQuery(query);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
        return rs;
    }

    public String executeSqlQueryAndGetString(String query) throws SQLException {
        String resultValue = "";
        ResultSet rs;
        rs = getStatement().executeQuery(query);
        try {
            while(rs.next()){
                resultValue = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException err) {
            System.out.println("No Records obtained for this specific query");
            err.printStackTrace();
        }
        return resultValue;
    }
}
