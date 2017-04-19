package codetool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SqlTool {
	private static ResultSetMetaData metaData = null;
	private static Connection connection = null;

	public static ResultSetMetaData getTableMeta(String tableName,String dbName) {
		if (metaData == null) {
			ResultSet resultSet = null;
			try {
				Connection connection = getConnection(dbName);
				PreparedStatement st = connection
						.prepareStatement("select * from " + tableName
								+ " where 1=1");
				resultSet = st.executeQuery();
				metaData = resultSet.getMetaData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return metaData;
	}
    public static Connection getConnection(String dbName){
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager
                        .getConnection(
                                "jdbc:mysql://127.0.0.1:3306/"+dbName+"?useUnicode=true&amp;characterEncoding=UTF-8",
                                "root", "zhangll");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    public static Map<String,String> getColumnAndComment(String tableName,String dbName){
        String query = "SELECT COLUMN_NAME,COLUMN_COMMENT from information_schema.COLUMNS where TABLE_SCHEMA='"+dbName+"' and TABLE_NAME=?";
        Map<String,String> map = new HashMap<>();
        try {
            PreparedStatement ps = getConnection(tableName).prepareStatement(query);
            ps.setString(1,tableName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){

                map.put(rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
	
}
