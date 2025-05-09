package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Superuser;

public class SuperuserDao extends Dao {
	/**
	 * IDを指定してスーパーユーザー情報を取得
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Superuser get(String id) throws Exception {
		String sql = "select * from superuser where id = ?";

		try (
			Connection con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql)
		) {
			stmt.setString(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return null;
				}

				Superuser superuser = new Superuser();
				superuser.setId(rs.getString("id"));
				superuser.setPassword(rs.getString("password"));

				return superuser;
			}
		}
	}

	/**
	 * 全てのスーパーユーザー情報を取得する
	 * @param school
	 * @return ユーザーのリスト
	 * @throws Exception
	 */
	public List<Superuser> getAll() throws Exception {
		// SELECT文をセット
		String sql = "select * from superuser order by id";

		return this.executeQuery(sql, new ArrayList<>());
	}

	/**
	 * ユーザー情報を保存する
	 * ユーザーIDが存在しない場合はINSERT、存在する場合はUPDATEを実行
	 * @param user ユーザー情報
	 * @return ユーザー情報の保存に成功した場合はtrue、失敗した場合はfalse
	 * @throws Exception
	 */
	public boolean save(Superuser user) throws Exception {
		Superuser existingUser = this.get(user.getId());

		// ユーザーIDが存在しない場合はINSERT、存在する場合はUPDATEを実行
		if (existingUser == null) {
			// INSERT処理
			try (Connection con = this.getConnection()) {
				try (PreparedStatement stmt = con.prepareStatement("insert into superuser(id, password) values(?, ?)")) {
					stmt.setString(1, user.getId());
					stmt.setString(2, user.getPassword());

					return stmt.executeUpdate() > 0;
				}
			}
		} else {
			// UPDATE処理
			try (Connection con = this.getConnection()) {
				try (PreparedStatement stmt = con.prepareStatement("update teacher set password = ? where id = ?")) {
					stmt.setString(1, user.getPassword());

					return stmt.executeUpdate() > 0;
				}
			}
		}
	}

	/**
	 * ユーザーIDを指定してスーパーユーザーを削除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean delete(String id) throws Exception {
		try (Connection con = this.getConnection()) {
			// ユーザーIDを指定してDELETE
			try (PreparedStatement stmt = con.prepareStatement("delete from superuser where id = ?")) {
				stmt.setString(1, id);
				return stmt.executeUpdate() > 0;
			}
		}
	}
	/**
	 * ユーザーIDとパスワードで認証を行う
	 * @param id
	 * @param password
	 * @return 認証成功時はスーパーユーザー情報、失敗時はnull
	 * @throws Exception
	 */
	public Superuser login(String id, String password) throws Exception {
		Superuser superuser = this.get(id);

		if (superuser == null || !superuser.getPassword().equals(password)) {
			return null;
		}

		return superuser;
	}

	/**
	 * SQL文字列とパラメータからクエリを構築・実行し、Beanのリストとして取得
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	protected List<Superuser> executeQuery(String sql, List<Object> params) throws SQLException, Exception {
		List<Superuser> items = new ArrayList<>();

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
				// 1行ずつBeanにデータを移してリストに格納
				while (rs.next()) {
					Superuser superuser = new Superuser();
					superuser.setId(rs.getString("id"));
					superuser.setPassword(rs.getString("password"));

					items.add(superuser);
				}
			}
		}

		return items;
	}

}
