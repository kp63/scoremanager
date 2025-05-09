package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;


public class SchoolDao extends Dao {
	/**
	 * 学校コードを指定して学校データを取得
	 * @param cd
	 * @throws Exception
	 */
	public School get(String cd) throws Exception {
		String sql = "select * from school where cd = ?";

		try (
			Connection con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql)
		) {
			stmt.setString(1, cd);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return null;
				}

				School school = new School();
				school.setCd(rs.getString("cd"));
				school.setName(rs.getString("name"));
				return school;
			}
		}
	}

	/*
	 * 学校一覧取得
	 */

	public List<School> filter() throws Exception {
		List<School> items = new ArrayList<>();
		String sql = "select * from school";

		try (
			Connection con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql)
		) {

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					School school = new School();
					school.setCd(rs.getString("cd"));
					school.setName(rs.getString("name"));

					items.add(school);
				}
			}
		}

		return items;
	}

}
