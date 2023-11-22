package com.koreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCInsertTest {
	public static void main(String[] args) {
		Connection conn = null;			// conn이 pstmt보다 먼저 생성돼야함
		PreparedStatement pstmt = null; // 쿼리에 명령어를 전달하는 일을 하는 변수

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "123456"); // 접속 시도

			// 성공 시
			// 쿼리 작성
			String sql = "INSERT INTO article"; // 띄어쓰기 주의 해야함
			sql += " Set regDate = NOW()";
			sql += ", updateDate = NOW()";
			sql += ", title = '제목1'";
			sql += ", `body` = '내용1';";

			pstmt = conn.prepareStatement(sql); // 작성한 쿼리 넣기
			pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
			// 먼저 실행한 변수의 역순으로 종료해야함
			// conn -> pstmt 순서로 실행했다면 -> pstmt -> conn 순서로 종료해야함
			try {
				if (pstmt != null && !pstmt.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
