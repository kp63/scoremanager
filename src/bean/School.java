package bean;

import java.io.Serializable;

/**
 * 学校情報を管理するBeanクラス
 */
public class School implements Serializable {
	private String cd;
	private String name;

    /**
     * 学校コードを取得
     * @return 学校コード
     */
	public String getCd() {
		return this.cd;
	}
    /**
     * 学校コードを取得
     * @return 学校コード
     */
	public void setCd(String cd) {
		this.cd = cd;
	}

	/**
	 * 学校名を取得
	 * @return 学校名
	 */
    public String getName() {
        return this.name;
    }
    /**
     * 学校名を設定
     * @param name 設定する学校名
     */
    public void setName(String name) {
        this.name = name;
    }
}
