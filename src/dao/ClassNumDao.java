package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.ClassNum;
import bean.School;

public class ClassNumDao extends Dao {
	/**
	 * 学校とクラス番号を指定してレコードを取得
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ClassNum get(String class_num, School school) throws Exception {
		String sql = "select * from class_num where class_num = ? and school_cd = ?";

		try (
			Connection con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql)
		) {
			stmt.setString(1, class_num);
			stmt.setString(2, school.getCd());

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return null;
				}

				SchoolDao schoolDao = new SchoolDao();

				ClassNum classNum = new ClassNum();
				classNum.setClass_num(rs.getString("class_num"));
				classNum.setSchool(schoolDao.get(rs.getString("school_cd")));

				return classNum;
			}
		}
	}

	/**
	 * 学校を指定してクラス番号の一覧を取得
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<String> filter(School school) throws Exception {
		List<String> items = new ArrayList<>();
		String sql = "select * from class_num where school_cd = ?";

		try (
			Connection con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql)
		) {
			stmt.setString(1, school.getCd());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					items.add(rs.getString("class_num"));
				}
			}
		}

		return items;
	}

}
