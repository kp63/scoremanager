package bean;

import java.io.Serializable;
import java.util.Map;

/**
 * 特定学生のテストデータを管理するBeanクラス
 */
public class TestListSubject implements Serializable {
	private int entYear;
	private String studentNo;
	private String studentName;
	private int classNum;
	private Map<Integer, Integer> points;

	public int getEntYear() {
		return entYear;
	}
	public void setEntYear(int entYear) {
		this.entYear = entYear;
	}
	public String getStudentNo() {
		return studentNo;
	}
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public int getClassNum() {
		return classNum;
	}
	public void setClassNum(int classNum) {
		this.classNum = classNum;
	}
	public Map<Integer, Integer> getPoints() {
		return points;
	}
	public void setPoints(Map<Integer, Integer> points) {
		this.points = points;
	}


}
