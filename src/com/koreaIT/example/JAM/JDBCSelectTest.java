package com.koreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koreaIT.example.JAM.dto.Article;

public class JDBCSelectTest {
	public static void main(String[] args) {
		Connection conn = null; // conn이 pstmt보다 먼저 생성돼야함
		PreparedStatement pstmt = null; // 쿼리에 명령어를 전달하는 일을 하는 변수
		ResultSet rs = null; // sql 결과값 받는 변수

		List<Article> articles = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "123456"); // 접속 시도

			// 성공 시
			String sql = "SELECT * FROM article";
			sql += " ORDER BY id DESC";

			pstmt = conn.prepareStatement(sql); // 작성한 쿼리 넣기
			rs = pstmt.executeQuery(); // 객체로 값을 가져옴(여러개일때), 쉽게 생각하면 압축되어 나옴. 반복문으로 꺼내야함

			// rs.next(), boolean타입, 하나씩 가져올때 다음 가져올것이 없으면 종료
			while (rs.next()) {
				int id = rs.getInt("id");
				String regDate = rs.getString("regDate");
				String updateDate = rs.getString("updateDate");
				String title = rs.getString("title");
				String body = rs.getString("body");
				
				Article article = new Article(id, regDate, updateDate, title, body);
				articles.add(article);
			}
			
			for(Article article : articles) {
				System.out.println(article.id);
				System.out.println(article.regDate);
				System.out.println(article.updateDate);
				System.out.println(article.title);
				System.out.println(article.body);
			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
