package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.TestListSubject;
import bean.Subject;
import bean.School;

public class TestListSubjectDao extends Dao {
	/**
	 * 各学生のテスト（回次→点数）を取得するSQL（年度＋クラス＋科目＋学校で絞り込む）
	 */
	private final String basesql =
			"SELECT st.ent_year      AS ent_year,"
					+ "       st.no            AS student_no,"
					+ "       st.name          AS student_name,"
					+ "       st.class_num     AS class_num,"
					+ "       t.no             AS test_no,"
					+ "       t.point          AS point "
					+ "  FROM test t "
					+ "  JOIN student st "
					+ "    ON t.student_no = st.no "
					+ "   AND t.school_cd   = st.school_cd "
					+ " WHERE st.ent_year   = ? "
					+ "   AND st.class_num  = ? "
					+ "   AND t.subject_cd  = ? "
					+ "   AND t.school_cd   = ? "
					+ " ORDER BY st.no, t.no";

	/**
	 * ResultSet を TestListSubject Bean のリストに変換。
	 * 同一 student_no は一つの Bean にまとめ、points に格納する。
	 */
	public List<TestListSubject> postFilter(ResultSet rs) throws SQLException {
		List<TestListSubject> out = new ArrayList<>();
		Map<String, TestListSubject> map = new HashMap<>();

		while (rs.next()) {
			String stuNo   = rs.getString("student_no");
			int    entYear = rs.getInt("ent_year");
			String stuName = rs.getString("student_name");
			int    clsNum  = rs.getInt("class_num");
			int    testNo  = rs.getInt("test_no");
			int    pt      = rs.getInt("point");

			TestListSubject bean = map.get(stuNo);
			if (bean == null) {
				bean = new TestListSubject();
				bean.setStudentNo(stuNo);
				bean.setEntYear(entYear);
				bean.setStudentName(stuName);
				bean.setClassNum(clsNum);
				bean.setPoints(new HashMap<>());
				map.put(stuNo, bean);
			}
			bean.getPoints().put(testNo, pt);
		}

		out.addAll(map.values());
		return out;
	}

	/**
	 * 学校に紐づく入学年度の一覧を取得（重複除去、昇順）
	 */
	public List<Integer> findDistinctEntYears(School school) throws Exception {
		String sql = "SELECT DISTINCT ent_year FROM student WHERE school_cd = ? ORDER BY ent_year";
		try (Connection con = getConnection();
			 PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, school.getCd());
			try (ResultSet rs = stmt.executeQuery()) {
				List<Integer> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getInt("ent_year"));
				}
				return list;
			}
		}
	}

	/**
	 * 指定した条件で TestListSubject の一覧を取得
	 */
	public List<TestListSubject> filter(int entYear,
										String classNum,
										Subject subject,
										School school) throws Exception {
		try (Connection con = getConnection();
			 PreparedStatement stmt = con.prepareStatement(basesql)) {
			stmt.setInt(1, entYear);
			stmt.setString(2, classNum);
			stmt.setString(3, subject.getCd());
			stmt.setString(4, school.getCd());
			try (ResultSet rs = stmt.executeQuery()) {
				return postFilter(rs);
			}
		}
	}
}
