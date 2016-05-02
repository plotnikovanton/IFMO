import java.sql.SQLException;

/**
 * Created by anton on 25.12.15.
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        // It could be parsed with something powerful like common cli to handle wrong inputs and do some other cool stuff
        String user = args[0];
        String passwd = args[1];
        String indb = args[2];
        String outdb = args[3];

        CopyManager copyManager = new CopyManager("jdbc:postgresql://localhost:5432/", user, passwd);
        copyManager.copyDbData(indb, outdb);
    }
}
