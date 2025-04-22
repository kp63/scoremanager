package dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Dao {
    private static DataSource dataSource;

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            try {
                InitialContext ic = new InitialContext();
                dataSource = (DataSource) ic.lookup("java:/comp/env/jdbc/exam");
            } catch (NamingException e) {
                throw new Error("データソースの取得に失敗しました", e);
            }
        }

        return dataSource.getConnection();
    }

	public String getTableName() {
		return null;
	}

}
