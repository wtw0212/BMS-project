import java.sql.*;
import oracle.jdbc.driver.*;

public class Database
{
    public static void main(String args[]) throws SQLException
    {

        String username = "\"23118761d\"";
        String pwd = "btracoql";

        // Connection
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn =
                (OracleConnection)DriverManager.getConnection(
                        "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms",username,pwd);

        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("SELECT EMPNO, ENAME, JOB FROM EMP");
        while (rset.next())
        {
            System.out.println(rset.getInt(1)
                    + " " + rset.getString(2)
                    + " " + rset.getString(3));
        }
        System.out.println();
        conn.close();
    }
}
