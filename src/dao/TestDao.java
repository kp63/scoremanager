package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {
	/**
	 * 得点IDを指定してテストデータを取得
	 */
	public Test get(Student student, Subject subject, School school, int no) throws Exception {
		String sql = "select * from test where student_no = ? and subject_cd = ? and school_cd = ? and no = ?";
		try (
			Connection con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql)
		) {
			stmt.setString(1, student.getNo());
			stmt.setString(2, subject.getCd());
			stmt.setString(3, school.getCd());
			stmt.setInt(4, no);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return null;
				}
				StudentDao studentDao = new StudentDao();
				SubjectDao subjectDao = new SubjectDao();
				SchoolDao schoolDao   = new SchoolDao();

				Student studentData   = studentDao.get(rs.getString("student_no"));
				School schoolData     = schoolDao.get(rs.getString("school_cd"));
				Subject subjectData   = subjectDao.get(rs.getString("subject_cd"), schoolData);

				Test test = new Test();
				test.setStudent(studentData);
				test.setSchool(schoolData);
				test.setSubject(subjectData);
				test.setNo(rs.getInt("no"));
				if (rs.getObject("point") != null) {
					test.setPoint(rs.getInt("point"));
				} else {
					test.setPoint(-1);
				}
				test.setClassNum(rs.getString("class_num"));
				return test;
			}
		}
	}

	/**
	 * 学生の特徴から一致するテストをリストで取得
	 */
	public List<Test> filter(int entYear, String classNum, Subject subject, int no, School school)
		throws Exception {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder(
			"select student.*, "
				+ "COALESCE(test.no, ?) as test_no, "
				+ "test.point, "
				+ "COALESCE(test.subject_cd, ?) as subject_cd "
				+ "from student "
				+ "left join test on student.no = test.student_no"
		);
		params.add(no);
		params.add(subject.getCd());
		sql.append(" and test.no = ?");        params.add(no);
		sql.append(" and test.subject_cd = ?"); params.add(subject.getCd());
		sql.append(" where student.school_cd = ?"); params.add(school.getCd());
		if (entYear != 0) {
			sql.append(" and student.ent_year = ?"); params.add(entYear);
		}
		if (classNum != null && !classNum.isEmpty()) {
			sql.append(" and student.class_num = ?"); params.add(classNum);
		}
		sql.append(" order by student.no asc");

		return executeQuery(sql.toString(), params);
	}

	/**
	 * Testオブジェクトを保存（INSERT or UPDATE）
	 */
	public boolean update(Test test) throws Exception {
		try (Connection con = getConnection()) {
			return save(test, con);
		}
	}

	/**
	 * Testオブジェクトのリストを一括保存
	 */
	public boolean save(List<Test> tests) throws Exception {
		boolean result = true;
		try (Connection con = this.getConnection()) {
			for (Test t : tests) {
				result &= save(t, con);
			}
		}
		return result;
	}

	/**
	 * (内部) TestオブジェクトをINSERT or UPDATE
	 */
	private boolean save(Test test, Connection con) throws Exception {
		// 既存レコードチェック
		Test old = get(
			test.getStudent(),
			test.getSubject(),
			test.getSchool(),
			test.getNo()
		);
		if (old == null) {
			// INSERT
			String insertSql =
				"insert into test(student_no, subject_cd, school_cd, no, point, class_num) "
					+ "values(?, ?, ?, ?, ?, ?)";
			try (PreparedStatement stmt = con.prepareStatement(insertSql)) {
				stmt.setString(1, test.getStudent().getNo());
				stmt.setString(2, test.getSubject().getCd());
				stmt.setString(3, test.getSchool().getCd());
				stmt.setInt(4, test.getNo());
				if (test.getPoint() < 0) {
					stmt.setNull(5, Types.INTEGER);
				} else {
					stmt.setInt(5, test.getPoint());
				}
				stmt.setString(6, test.getClassNum());
				return stmt.executeUpdate() > 0;
			}
		} else {
			// UPDATE
			String updateSql =
				"update test set point = ? "
					+ "where student_no = ? and subject_cd = ? and school_cd = ? and no = ?";
			try (PreparedStatement stmt = con.prepareStatement(updateSql)) {
				if (test.getPoint() < 0) {
					stmt.setNull(1, Types.INTEGER);
				} else {
					stmt.setInt(1, test.getPoint());
				}
				stmt.setString(2, test.getStudent().getNo());
				stmt.setString(3, test.getSubject().getCd());
				stmt.setString(4, test.getSchool().getCd());
				stmt.setInt(5, test.getNo());
				return stmt.executeUpdate() > 0;
			}
		}
	}

	/**
	 * SQL文字列とパラメータからクエリを構築・実行し、Beanのリストとして取得
	 */
	protected List<Test> executeQuery(String sql, List<Object> params)
		throws SQLException, Exception {
		List<Test> items = new ArrayList<>();
		try (
			Connection con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql)
		) {
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
			}
			try (ResultSet rs = stmt.executeQuery()) {
				StudentDao studentDao = new StudentDao();
				SchoolDao schoolDao   = new SchoolDao();
				SubjectDao subjectDao = new SubjectDao();

				while (rs.next()) {
					Test t = new Test();
					t.setStudent(studentDao.get(rs.getString("student.no")));
					t.setSchool(schoolDao.get(rs.getString("student.school_cd")));
					t.setSubject(subjectDao.get(
						rs.getString("subject_cd"), t.getSchool()
					));
					t.setClassNum(rs.getString("student.class_num"));
					t.setNo(rs.getInt("test_no"));
					if (rs.getObject("point") != null) {
						t.setPoint(rs.getInt("point"));
					} else {
						t.setPoint(-1);
					}
					items.add(t);
				}
			}
		}
		return items;
	}
}
