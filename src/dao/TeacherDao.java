package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Teacher;

public class TeacherDao extends Dao {
	/**
	 * 教員IDを指定して教員データを取得
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Teacher get(String id) throws Exception {
		String sql = "select * from teacher where id = ?";

		try (
			Connection con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql)
		) {
			stmt.setString(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return null;
				}

				SchoolDao schoolDao = new SchoolDao();

				Teacher teacher = new Teacher();
				teacher.setId(rs.getString("id"));
				teacher.setPassword(rs.getString("password"));
				teacher.setName(rs.getString("name"));
				teacher.setSchool(schoolDao.get(rs.getString("school_cd")));
				teacher.setRole("default");

				// ユーザーIDがadminの場合は管理者権限として扱う
				if (teacher.getId().equals("admin")) {
					teacher.setRole("admin");
				}

				return teacher;
			}
		}
	}

	/**
	 * 指定の学校に所属する教員をリストで取得
	 * @param school
	 * @return 教員のリスト
	 * @throws Exception
	 */
	public List<Teacher> filter(School school) throws Exception {
		// SELECT文をセット1
		String sql = "select * from teacher where school_cd = ? order by id";

		List<Object> params = new ArrayList<>();
		params.add(school.getCd());

		return this.executeQuery(sql, params);
	}

	/**
	 * 教員情報を保存する
	 * 教員IDが存在しない場合はINSERT、存在する場合はUPDATEを実行
	 * @param teacher 教員情報
	 * @return 教員情報の保存に成功した場合はtrue、失敗した場合はfalse
	 * @throws Exception
	 */
	public boolean save(Teacher teacher) throws Exception {
		Teacher existingTeacher = this.get(teacher.getId());

		// 教員IDが存在しない場合はINSERT、存在する場合はUPDATEを実行
		if (existingTeacher == null) {
			// INSERT処理
			try (Connection con = this.getConnection()) {
				try (PreparedStatement stmt = con.prepareStatement("insert into teacher(id, password, name, school_cd) values(?, ?, ?, ?)")) {
					stmt.setString(1, teacher.getId());
					stmt.setString(2, teacher.getPassword());
					stmt.setString(3, teacher.getName());
					stmt.setString(4, teacher.getSchool().getCd());

					return stmt.executeUpdate() > 0;
				}
			}
		} else {
			// UPDATE処理
			try (Connection con = this.getConnection()) {
				try (PreparedStatement stmt = con.prepareStatement("update teacher set password = ?, name = ?, school_cd = ? where id = ?")) {
					stmt.setString(1, teacher.getPassword());
					stmt.setString(2, teacher.getName());
					stmt.setString(3, teacher.getSchool().getCd());
					stmt.setString(4, teacher.getId());

					return stmt.executeUpdate() > 0;
				}
			}
		}
	}

	/**
	 * 教員IDを指定して教員を削除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean delete(String id) throws Exception {
		try (Connection con = this.getConnection()) {
			// 教員IDを指定してDELETE
			try (PreparedStatement stmt = con.prepareStatement("delete from teacher where id = ?")) {
				stmt.setString(1, id);
				return stmt.executeUpdate() > 0;
			}
		}
	}
	/**
	 * 教員IDとパスワードで認証を行う
	 * @param id
	 * @param password
	 * @return 認証成功時は教員情報、失敗時はnull
	 * @throws Exception
	 */
	public Teacher login(String id, String password) throws Exception {
		Teacher teacher = this.get(id);

		if (teacher == null || !teacher.getPassword().equals(password)) {
			return null;
		}

		return teacher;
	}

	/**
	 * SQL文字列とパラメータからクエリを構築・実行し、Beanのリストとして取得
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	protected List<Teacher> executeQuery(String sql, List<Object> params) throws SQLException, Exception {
		List<Teacher> items = new ArrayList<>();

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
					Teacher teacher = new Teacher();
					teacher.setId(rs.getString("id"));
					teacher.setPassword(rs.getString("password"));
					teacher.setName(rs.getString("name"));
					teacher.setSchool(schoolDao.get(rs.getString("school_cd")));
					teacher.setRole("default");

					// ユーザーIDがadminの場合は管理者権限として扱う
					if (teacher.getId().equals("admin")) {
						teacher.setRole("admin");
					}

					items.add(teacher);
				}
			}
		}

		return items;
	}

}
