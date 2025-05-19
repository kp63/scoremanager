package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;

public class StudentDao extends Dao {
	/**
	 * 学生IDを指定して教員データを取得
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Student get(String no) throws Exception {
		String sql = "select * from student where no = ?";

		try (
			Connection con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql)
		) {
			stmt.setString(1, no);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return null;
				}

				SchoolDao schoolDao = new SchoolDao();

				Student student = new Student();
				student.setNo(rs.getString("no"));
				student.setName(rs.getString("name"));
				student.setEntYear(rs.getInt("ent_year"));
				student.setClassNum(rs.getString("class_num"));
				student.setAttend(rs.getBoolean("is_attend"));
				student.setSchool(schoolDao.get(rs.getString("school_cd")));

				return student;
			}
		}
	}

	/**
	 * 学生の特徴から一致する学生をリストで取得
	 * @param school
	 * @param entYear
	 * @param classNum
	 * @param isAttend
	 * @return
	 * @throws Exception
	 */
	public List<Student> filter(School school, int entYear, String classNum, boolean isAttend) throws Exception {
		// SELECT文をセット1
		StringBuilder sql = new StringBuilder("select * from student");
		List<Object> params = new ArrayList<>();

		// 条件をWHEREで絞り込み
		// SQLインジェクション対策の為、SQL文とパラメータを分けて定義
		sql.append(" where school_cd = ?");
		params.add(school.getCd());

		if (entYear != 0) {
			sql.append(" and ent_year = ?");
			params.add(entYear);
		}
		if (classNum != null) {
			sql.append(" and class_num = ?");
			params.add(classNum);
		}
		if (isAttend) {
			sql.append(" and is_attend = ?");
			params.add(isAttend);
		}

		// ORDER BYでソートを定義
		sql.append(" order by no asc");

		// 実行してStudentリストを取得
		List<Student> students = executeQuery(sql.toString(), params);

		return students;
	}

	public List<Student> filter(School school, int entYear, boolean isAttend) throws Exception {
		// SELECT文をセット1
		StringBuilder sql = new StringBuilder("select * from student");
		List<Object> params = new ArrayList<>();

		// 条件をWHEREで絞り込み
		// SQLインジェクション対策の為、SQL文とパラメータを分けて定義
		sql.append(" where school_cd = ?");
		params.add(school.getCd());
		if (entYear != 0) {
			sql.append(" and ent_year = ?");
			params.add(entYear);
		}
		if (isAttend) {
			sql.append(" and is_attend = ?");
			params.add(isAttend);
		}

		// ORDER BYでソートを定義
		sql.append(" order by no asc");

		// 実行してStudentリストを取得
		List<Student> students = executeQuery(sql.toString(), params);

		return students;
	}

	public List<Student> filter(School school, boolean isAttend) throws Exception {
		// SELECT文をセット
		StringBuilder sql = new StringBuilder("select * from student");
		List<Object> params = new ArrayList<>();

		// 条件をWHEREで絞り込み
		// SQLインジェクション対策の為、SQL文とパラメータを分けて定義
		sql.append(" where school_cd = ?");
		params.add(school.getCd());
		if (isAttend) {
			sql.append(" and is_attend = ?");
			params.add(isAttend);
		}

		// ORDER BYでソートを定義
		sql.append(" order by no asc");

		// 実行してStudentリストを取得
		List<Student> students = executeQuery(sql.toString(), params);

		return students;
	}

	public List<Student> filter(School school, String name) throws Exception {
		// SELECT文をセット1
		StringBuilder sql = new StringBuilder("select * from student");
		List<Object> params = new ArrayList<>();

		// 条件をWHEREで絞り込み
		// SQLインジェクション対策の為、SQL文とパラメータを分けて定義
		sql.append(" where school_cd = ?");
		params.add(school.getCd());

		if (name != null){
			sql.append(" and name like ?");
			params.add("%" + name.trim() + "%");
		}

		// ORDER BYでソートを定義
		sql.append(" order by no asc");

		// 実行してStudentリストを取得
		List<Student> students = executeQuery(sql.toString(), params);

		return students;
	}

	public boolean save(Student student) throws Exception {
		try (Connection con = this.getConnection()) {
			// 引数の学生IDから該当する学生が居るか否かでINSERTかUPDATEを判断
			// 学生IDはユニーク制約があるので、同じ学生IDは存在しない
			Student oldStudent = null;
			if (student.getNo() != null) {
				oldStudent = this.get(student.getNo());
			}
			if (oldStudent == null) {
				// 新規登録
				try (PreparedStatement stmt = con.prepareStatement("insert into student(no, name, ent_year, class_num, is_attend, school_cd) values(?, ?, ?, ?, ?, ?)")) {
					stmt.setString(1, student.getNo());
					stmt.setString(2, student.getName());
					stmt.setInt(3, student.getEntYear());
					stmt.setString(4, student.getClassNum());
					stmt.setBoolean(5, student.isAttend());
					stmt.setString(6, student.getSchool().getCd());

					return stmt.executeUpdate() > 0;
				}
			} else {
				// 更新
				try (PreparedStatement stmt = con.prepareStatement("update student set name = ?, ent_year = ?, class_num = ?, is_attend = ?, school_cd = ? where no = ?")) {
					stmt.setString(1, student.getName());
					stmt.setInt(2, student.getEntYear());
					stmt.setString(3, student.getClassNum());
					stmt.setBoolean(4, student.isAttend());
					stmt.setString(5, student.getSchool().getCd());
					stmt.setString(6, student.getNo());

					return stmt.executeUpdate() > 0;
				}
			}
		}
	}

	public boolean delete(String no) throws Exception {
		try (Connection con = this.getConnection()) {
			// 学生IDを指定してDELETE
			try (PreparedStatement stmt = con.prepareStatement("delete from student where no = ?")) {
				stmt.setString(1, no);
				return stmt.executeUpdate() > 0;
			}
		}
	}


	/**
	 * SQL文字列とパラメータからクエリを構築・実行し、Beanのリストとして取得
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	protected List<Student> executeQuery(String sql, List<Object> params) throws SQLException, Exception {
		List<Student> items = new ArrayList<>();

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
				SchoolDao schoolDao = new SchoolDao();

				// 1行ずつBeanにデータを移してリストに格納
				while (rs.next()) {
					Student student = new Student();
					student.setNo(rs.getString("no"));
					student.setName(rs.getString("name"));
					student.setEntYear(rs.getInt("ent_year"));
					student.setClassNum(rs.getString("class_num"));
					student.setAttend(rs.getBoolean("is_attend"));
					student.setSchool(schoolDao.get(rs.getString("school_cd")));

					items.add(student);
				}
			}
		}

		return items;
	}
}
