package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;

public class SubjectDao extends Dao {
	/**
	 * 科目コードを指定して科目データを取得
	 * @param cd
	 * @param school
	 * @return
	 * @throws Exception
	 */
	public Subject get(String cd, School school) throws Exception {
		String sql = "select * from subject where cd = ? and school_cd = ?";

		try (
			Connection con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql)
		) {
			stmt.setString(1, cd);
			stmt.setString(2, school.getCd());

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return null;
				}

				SchoolDao schoolDao = new SchoolDao();

				Subject classNum = new Subject();
				classNum.setCd(rs.getString("cd"));
				classNum.setName(rs.getString("name"));
				classNum.setSchool(schoolDao.get(rs.getString("school_cd")));

				return classNum;
			}
		}
	}

	/**
	 * 学校を指定して科目データを取得
	 * @param school
	 * @return
	 * @throws Exception
	 */
	public List<Subject> filter(School school) throws Exception {
		List<Subject> items = new ArrayList<>();
		String sql = "select * from subject where school_cd = ? order by cd";

		try (
			Connection con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql)
		) {
			stmt.setString(1, school.getCd());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Subject subject = new Subject();
					subject.setCd(rs.getString("cd"));
					subject.setName(rs.getString("name"));
					subject.setSchool(school);

					items.add(subject);
				}
			}
		}

		return items;
	}

	public boolean save(Subject subject) throws Exception {
		try (Connection con = this.getConnection()) {
			// 実在チェック
			if (this.get(subject.getCd(), subject.getSchool()) == null) {
				// 新規登録
				String sql = "insert into subject (cd, name, school_cd) values (?, ?, ?)";
				try (PreparedStatement stmt = con.prepareStatement(sql)) {
					stmt.setString(1, subject.getCd());
					stmt.setString(2, subject.getName());
					stmt.setString(3, subject.getSchool().getCd());

					return stmt.executeUpdate() > 0;
				}
			} else {
				// 更新
				String sql = "update subject set name = ? where cd = ? and school_cd = ?";
				try (PreparedStatement stmt = con.prepareStatement(sql)) {
					stmt.setString(1, subject.getName());
					stmt.setString(2, subject.getCd());
					stmt.setString(3, subject.getSchool().getCd());

					return stmt.executeUpdate() > 0;
				}
			}
		}
	}

	public boolean delete(Subject subject) throws Exception {
		String sql = "delete from subject where cd = ? and school_cd = ?";

		try (Connection con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql)
		) {
			stmt.setString(1, subject.getCd());
			stmt.setString(2, subject.getSchool().getCd());

			return stmt.executeUpdate() > 0;
		}
	}
}
