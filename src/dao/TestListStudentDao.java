package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.TestListStudent;
import bean.Student;

public class TestListStudentDao extends Dao {
	// 学生No.と学校コードで、科目ごとのテスト回数と得点合計を取得するSQL
	private final String basesql =
		"SELECT s.cd AS subject_cd, s.name AS subject_name, COUNT(*) AS num, SUM(t.point) AS point " +
			"FROM test t " +
			"  JOIN subject s ON t.subject_cd = s.cd AND t.school_cd = s.school_cd " +
			"WHERE t.student_no = ? AND t.school_cd = ? " +
			"GROUP BY s.cd, s.name " +
			"ORDER BY s.cd";

	/**
	 * ResultSet を Bean リストにマッピング
	 */
	public List<TestListStudent> postFilter(ResultSet rs) throws SQLException {
		List<TestListStudent> list = new ArrayList<>();
		while (rs.next()) {
			TestListStudent bean = new TestListStudent();
			bean.setSubjectCd(rs.getString("subject_cd"));
			bean.setSubjectName(rs.getString("subject_name"));
			bean.setNum(rs.getInt("num"));
			bean.setPoint(rs.getInt("point"));
			list.add(bean);
		}
		return list;
	}

	/**
	 * 指定学生の科目別テストリストを取得
	 */
	public List<TestListStudent> filter(Student student) throws Exception {
		try (
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement(basesql)
		) {
			stmt.setString(1, student.getNo());
			stmt.setString(2, student.getSchool().getCd());
			try (ResultSet rs = stmt.executeQuery()) {
				return postFilter(rs);
			}
		}
	}
}
