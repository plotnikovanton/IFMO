import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static java.lang.String.format;

/**
 * Created by anton on 25.12.15.
 */
public class CopyManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CopyManager.class);
    private final String addr;
    private final String user;
    private final String passwd;

    public CopyManager(String addr, String user, String passwd) {
        this.addr = addr;
        this.user = user;
        this.passwd = passwd;
    }

    public Connection makeConnection(String dbname) throws SQLException {
        return DriverManager.getConnection(addr + dbname, user, passwd);
    }

    /**
     * Each table description has the following columns:
     * <p>
     * TABLE_CAT String => table catalog (may be null)
     * TABLE_SCHEM String => table schema (may be null)
     * TABLE_NAME String => table name
     * TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
     * "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
     * REMARKS String => explanatory comment on the table
     * TYPE_CAT String => the types catalog (may be null)
     * TYPE_SCHEM String => the types schema (may be null)
     * TYPE_NAME String => type name (may be null)
     * SELF_REFERENCING_COL_NAME String => name of the designated "identifier" column of a typed table (may be null)
     * REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM",
     * "USER", "DERIVED". (may be null)
     */
    public ResultSet getTables(Connection conn) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", new String[]{"TABLE"});

        return rs;
    }

    public void forEach(ResultSet rs, Consumer<ResultSet> consumer) throws SQLException {
//        rs.beforeFirst();
        while (rs.next()) {
            consumer.accept(rs);
        }
    }

    public void copyDbData(String dbin, String dbout) throws SQLException {
        // Do some connections and collect statements
        Connection inConn = makeConnection(dbin);
        Statement inSt = inConn.createStatement();
        DatabaseMetaData dbmeta = inConn.getMetaData();

        Connection outConn = makeConnection(dbout);
        Statement outSt = outConn.createStatement();

        ResultSet tables = getTables(inConn);
        forEach(tables, rs -> {
            try {
                // Okay, we'v got our table name
                String tableName = rs.getString("TABLE_NAME");

                //PreparedStatement st = DriverManager.getConnection(addr, user, passwd).prepareStatement(format("CREATE TABLE %s.%s (LIKE %s.%s)", dbout, tableName, dbin, tableName));
                //st.execute();
                //st.close();

                // Lets fetch all data from it
                ResultSet data = inSt.executeQuery(format("SELECT * FROM \"%s\"", tableName));

                // Lets get table schema from metadata and generate creation operator
                ResultSetMetaData datamd = data.getMetaData();
                int columnCount = datamd.getColumnCount();

                StringBuilder sb = new StringBuilder();
                sb.append("CREATE TABLE ").append(tableName).append(" (");

                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) sb.append(", ");
                    sb.append(datamd.getColumnName(i)).append(" ").append(datamd.getColumnTypeName(i));

                    if (datamd.isNullable(i) == ResultSetMetaData.columnNoNulls) sb.append(" NOT NULL");
                    // Tons of parameters could be parsed here except of references and keys

                    //if (precision != 0) {
                    //    sb.append("(").append(precision).append(")");
                    // }

                    switch (datamd.getColumnTypeName(i)) {
                        case "character":
                        case "time":
                        case "":
                            sb.append("(").append(datamd.getPrecision(i)).append(")");
                    }
                }
                sb.append(");");

                // Looks good, lets create this in our outer database
                LOGGER.info("Execute: '{}'", sb.toString());

                outSt.execute(sb.toString());


                // Setting up primary keys
                Map<String, StringBuilder> primaryKeys = new HashMap<>();
                ResultSet primaryKeysRs = dbmeta.getPrimaryKeys(null, null, tableName);
                forEach(primaryKeysRs, pkRs -> {
                    try {
                        String columName = pkRs.getString("COLUMN_NAME");
                        String pkName = pkRs.getString("PK_NAME");

                        if (!primaryKeys.containsKey(pkName)) {
                            primaryKeys.put(pkName, new StringBuilder(columName));
                        } else {
                            primaryKeys.get(pkName).append(", ").append(columName);
                        }

                    } catch (SQLException e) {
                        // Should never be reached
                        e.printStackTrace();
                    }
                });

                primaryKeys.entrySet().forEach(entry -> {
                    String pkName = entry.getKey();
                    String pk = entry.getValue().toString();
                    String query = format("ALTER TABLE %s ADD PRIMARY KEY (%s)", tableName, pk);
                    try {
                        LOGGER.info("Execute: '{}'", query);
                        outSt.executeUpdate(query);
                    } catch (SQLException e) {
                        LOGGER.error("Setting '{}' as primary key in '{}' failed", pk, tableName);
                        e.printStackTrace();
                    }
                });

                // Generate insertion statement
                StringBuilder prepareQuery = new StringBuilder("INSERT INTO ");
                prepareQuery.append(tableName).append(" VALUES (");
                for (int i = 0; i < columnCount; i++) {
                    if (i > 0) prepareQuery.append(", ");
                    prepareQuery.append("?");
                }
                prepareQuery.append(");");

                LOGGER.info("Prepare statement with: {}", prepareQuery);
                PreparedStatement pst = outConn.prepareStatement(prepareQuery.toString());

                // And put all our's data in it
                forEach(data, dataRs -> {
                    try {
                        for (int i = 1; i <= columnCount; i++) {
                            pst.setObject(i, dataRs.getObject(i));
                        }

                        LOGGER.info("Execute: '{}'", pst.toString());
                        pst.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    /*
                    try {
                        StringBuilder insert = new StringBuilder();
                        insert.append("INSERT INTO \"").append(tableName).append("\" VALUES (");
                        for (int i = 1; i <= columnCount; i++) {
                            if (i > 1) insert.append(", ");
                            insert.append(dataRs.getString(i));
                        }
                        insert.append(");");
                        // Finally put insert it in table
                        // Timestamps would be inserted incorrect
                        LOGGER.info("Execute: '{}'", insert.toString());
                        outSt.executeUpdate(insert.toString());
                    } catch (SQLException e) {
                        LOGGER.error("Failed to insert into '{}'", tableName);
                    }
                    */
                });
            } catch (SQLException e) {
                LOGGER.error("Failed to create table");
                e.printStackTrace();
            }
        });

        // Lets deal up with foreign keys
        tables.beforeFirst();
        forEach(tables, rs -> {
            try {
                String tableName = rs.getString("TABLE_NAME");
                ResultSet foreignKeysRs = dbmeta.getImportedKeys(null, null, tableName);
                Map<String, String> pkTables = new HashMap<>();
                Map<String, StringBuilder> pkKeys = new HashMap<>();
                Map<String, StringBuilder> fkKeys = new HashMap<>();
                forEach(foreignKeysRs, keyRs -> {
                    try {

                        String pkTable = keyRs.getString("PKTABLE_NAME");
                        String pkColumn = keyRs.getString("PKCOLUMN_NAME");
                        String fkColumn = keyRs.getString("FKCOLUMN_NAME");
                        String fkName = keyRs.getString("FK_NAME");

                        if (!pkTables.containsKey(fkName)) {
                            pkTables.put(fkName, pkTable);
                            pkKeys.put(fkName, new StringBuilder(pkColumn));
                            fkKeys.put(fkName, new StringBuilder(fkColumn));
                        } else {
                            pkKeys.get(fkName).append(", ").append(pkColumn);
                            fkKeys.get(fkName).append(", ").append(fkColumn);
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Should newer be reached
                    }
                });
                pkTables.entrySet().forEach(entry -> {
                    String keyName = entry.getKey();
                    String extTableName = entry.getValue();

                    String alterFK = format("ALTER TABLE %s ADD FOREIGN KEY (%s) REFERENCES %s(%s);",
                            tableName,
                            //keyName,
                            fkKeys.get(keyName),
                            extTableName,
                            pkKeys.get(keyName)
                    );

                    LOGGER.info("Execute: '{}'", alterFK);

                    try {
                        outSt.executeUpdate(alterFK);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                });


            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }
}
