package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
}
