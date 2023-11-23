package com.koreaIT.example.JAM.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.koreaIT.example.JAM.dto.Article;

public class ArticleController {
	List<Article> articles;
	int lastArticleId;
	Scanner sc;

	public ArticleController(Scanner sc) {
		this.articles = new ArrayList<>();
		this.lastArticleId = 0;
		this.sc = sc;
	}

	public void doWrite() {
		System.out.println("== 게시물 작성 ==");
		System.out.printf("제목: ");
		String title = sc.nextLine().trim();
		System.out.printf("내용: ");
		String body = sc.nextLine().trim();

		insertQuery(title, body);
		System.out.println("게시물이 생성되었습니다.");
	}

	public void showList() {
		System.out.println("== 게시물 목록 ==");

		selectQuery();
		
		// 게시물이 존재하지 않을 때
		if (this.articles.size() == 0) {
			System.out.println("게시물이 없습니다.");
			return;
		}

		System.out.println("번호	|	제목");
		for(Article article : articles) {
			System.out.printf("%d	|	%s\n", article.id, article.title);
		}
	}
	
	// insert
	public void insertQuery(String title, String body) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");		// cj를 넣으면 문구 안나오게 가능함
			String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "123456");

			String sql = "INSERT INTO article";
			sql += " Set regDate = NOW()";
			sql += ", updateDate = NOW()";
			sql += ", title = '" + title + "'";
			sql += ", `body` = '" + body + "1';";

			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
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
	
	
	// select
	public void selectQuery() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		this.articles = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "123456");

			// 성공 시
			String sql = "SELECT * FROM article";
			sql += " ORDER BY id DESC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String regDate = rs.getString("regDate");
				String updateDate = rs.getString("updateDate");
				String title = rs.getString("title");
				String body = rs.getString("body");

				Article article = new Article(id, regDate, updateDate, title, body);
				this.articles.add(article);
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
