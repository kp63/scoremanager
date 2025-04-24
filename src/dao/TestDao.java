package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {
	/**
	 * 得点IDを指定してテストデータを取得
	 * @param id
	 * @return
	 * @throws Exception
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
				SchoolDao schoolDao = new SchoolDao();

				Student studentData = studentDao.get(rs.getString("student_no"));
				Subject subjectData = subjectDao.get(rs.getString("subject_cd"), schoolDao.get(rs.getString("school_cd")));
				School schoolData = schoolDao.get(rs.getString("school_cd"));

				Test test = new Test();
				test.setStudent(studentData);
				test.setSubject(subjectData);
				test.setSchool(schoolData);
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
	 * @param school
	 * @param entYear
	 * @param classNum
	 * @param isAttend
	 * @return
	 * @throws Exception
	 */
	public List<Test> filter(int entYear, String classNum, Subject subject, int no, School school) throws Exception {
		// SELECT文をセット1
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select student.*, COALESCE(test.no, ?) as test_no, test.point, COALESCE(test.subject_cd, ?) as subject_cd from student"
			+ " left join test on student.no = test.student_no");
		params.add(no);
		params.add(subject.getCd());

		sql.append(" and test.no = ?");
		params.add(no);

		sql.append(" and test.subject_cd = ?");
		params.add(subject.getCd());

		// 条件をWHEREで絞り込み
		// SQLインジェクション対策の為、SQL文とパラメータを分けて定義
		sql.append(" where student.school_cd = ?");
		params.add(school.getCd());

		if (entYear != 0) {
			sql.append(" and student.ent_year = ?");
			params.add(entYear);
		}
		if (classNum != null) {
			sql.append(" and student.class_num = ?");
			params.add(classNum);
		}

		// ORDER BYでソートを定義
		sql.append(" order by student.no asc");

		// 実行してTestリストを取得
		List<Test> tests = executeQuery(sql.toString(), params);

		return tests;
	}

	public boolean save(List<Test> tests) throws Exception {
		boolean result = true;
			try (Connection con = this.getConnection()) {
				for (Test test : tests) {
				result &= this.save(test, con);
			}
		}
		return result;
	}

	private boolean save(Test test, Connection con) throws Exception {
		// 引数から該当するテストが有るか否かでINSERTかUPDATEを判断
		Test oldTest = null;
		if (test.getStudent() != null) {
			oldTest = this.get(test.getStudent(), test.getSubject(), test.getSchool(), test.getNo());
		}
		if (oldTest == null) {
			// 新規登録
			try (PreparedStatement stmt = con.prepareStatement("insert into test(student_no, subject_cd, school_cd, no, point, class_num) values(?, ?, ?, ?, ?, ?)")) {
				stmt.setString(1, test.getStudent().getNo());
				stmt.setString(2, test.getSubject().getCd());
				stmt.setString(3, test.getSchool().getCd());
				stmt.setInt(4, test.getNo());
				stmt.setInt(5, test.getPoint());
				stmt.setString(6, test.getClassNum());

				return stmt.executeUpdate() > 0;
			}
		} else {
			// 更新
			try (PreparedStatement stmt = con.prepareStatement("update test set point = ? where student_no = ? and subject_cd = ? and school_cd = ? and no = ?")) {
				stmt.setInt(1, test.getPoint());
				stmt.setString(2, test.getStudent().getNo());
				stmt.setString(3, test.getSubject().getCd());
				stmt.setString(4, test.getSchool().getCd());
				stmt.setInt(5, test.getNo());

				return stmt.executeUpdate() > 0;
			}
		}
	}

	// public boolean delete(String no) throws Exception {
	// 	try (Connection con = this.getConnection()) {
	// 		// 学生IDを指定してDELETE
	// 		try (PreparedStatement stmt = con.prepareStatement("delete from student where no = ?")) {
	// 			stmt.setString(1, no);
	// 			return stmt.executeUpdate() > 0;
	// 		}
	// 	}
	// }


	/**
	 * SQL文字列とパラメータからクエリを構築・実行し、Beanのリストとして取得
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	protected List<Test> executeQuery(String sql, List<Object> params) throws SQLException, Exception {
		List<Test> items = new ArrayList<>();

		System.out.println("SQL: " + sql);
		System.out.println("PARAMS: " + params);

		try (
			Connection con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql)
		) {
			// 上記で定義したSQLパラメータを全てセット
			for (int i = 0; i < params.size(); i++) {
	            stmt.setObject(i + 1, params.get(i));
			}

			// SQL実行
			try (ResultSet rs = stmt.executeQuery()) {
				StudentDao studentDao = new StudentDao();
				SchoolDao schoolDao = new SchoolDao();
				SubjectDao subjectDao = new SubjectDao();

				// 1行ずつBeanにデータを移してリストに格納
				while (rs.next()) {
					Test test = new Test();
					test.setStudent(studentDao.get(rs.getString("student.no")));
					test.setSchool(schoolDao.get(rs.getString("student.school_cd")));
					test.setSubject(subjectDao.get(rs.getString("subject_cd"), test.getSchool()));
					test.setClassNum(rs.getString("student.class_num"));

					test.setNo(rs.getInt("test_no"));
					// test.setPoint(rs.getInt("test.point"));
					if (rs.getObject("test.point") != null) {
						test.setPoint(rs.getInt("test.point"));
					} else {
						test.setPoint(-1);
					}

					items.add(test);
				}
			}
		}

		return items;
	}
}
