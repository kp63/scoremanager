<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム" />

	<c:param name="scripts">
		<script type="text/javascript">
			// DOMの読み込みが完了したら実行
			$(function() {
				// パスワード表示切り替えチェックボックスのイベントハンドラを設定
				$('#password-display').change(function() {
					// チェックボックスの状態を取得
					if ($(this).prop('checked')) {
						// チェックされている場合:
						// パスワード入力フィールドのtype属性を'text'に変更し
						// パスワードを平文で表示
						$('#password-input').attr('type', 'text');
					} else {
						// チェックが外れている場合:
						// パスワード入力フィールドのtype属性を'password'に変更し
						// パスワードを●●●で非表示
						$('#password-input').attr('type', 'password');
					}
				});
			});
		</script>
	</c:param>


<c:param name="content">
	<%-- ログインフォームセクション
	w-75: 幅75%
	text-center: 中央揃え
	m-auto: 自動マージン
	border: 枠線表示
	pb-3: 下パディング
	--%>
	<section class="w-75 text-center m-auto border pb-3">
		<%-- ログインフォーム: LoginExecute.actionにPOSTで送信 --%>
		<form action = "LoginExecute.action" method="post">
			<div id="wrap_box">
			<%-- ログインヘッダー --%>
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2">ログイン</h2>

			<%-- エラーメッセージ表示領域 --%>
			<c:if test="${errors.size()>0}">
				<div>
					<ul>
						<c:forEach var="error" items="${errors}">
							<li>${error}</li>
						</c:forEach>
					</ul>
				</div>
			</c:if>

			<div>
				<%-- ID入力フィールド --%>
				<div class="form-floating mx-5">
					<input class="form-control px-5 fs-5" autocomplete="off"
					id="id-input" maxlength="20" name="id" placeholder="半角
					でご入力下さい
					"
					style="ime-mode: disabled" type="text" value="${id}"
					required
					/>
					<label>ＩＤ</label>
				</div>

				<%-- パスワード入力フィールド --%>
				<div class="form-floating mx-5 mt-3">
					<input class="form-control px-5 fs-5" autocomplete="off"
					id="password-input" maxlength="20" name="password"
					placeholder="20文字以内の半角英数字でご入力下さい"
					style="ime-mode:
					disabled"
					type="password" required />
					<label>パスワード</label>
				</div>

				<%-- パスワード表示切り替えチェックボックス --%>
				<div class="form-check mt-3">
					<label class="form-check-label" for="password-display">
					<input class="form-check-input" id="password-display"
					name="chk_d_ps" type="checkbox"
					/>
					パスワードを表示
					</label>
					</div>
				</div>

				<%-- ログインボタン --%>
				<div class="mt-4">
					<input
						class="w-25 btn btn-lg btn-primary"
						type="submit"
						name="login"
						value="ログイン"
					/>
				</div>
			</div>
		</form>
	</section>
</c:param>

</c:import>
