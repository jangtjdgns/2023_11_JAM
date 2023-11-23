package com.koreaIT.example.JAM.controller;

import java.sql.Connection;
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
	String cmd;
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;

	public ArticleController(Scanner sc, String cmd, Connection conn, PreparedStatement pstmt, ResultSet rs) {
		this.articles = new ArrayList<>();
		this.lastArticleId = 0;
		this.sc = sc;
		this.cmd = cmd;
		this.conn = conn;
		this.pstmt = pstmt;
		this.rs = rs;
	}

	// 작성
	public void doWrite() {
		System.out.println("== 게시물 작성 ==");
		System.out.printf("제목: ");
		String title = sc.nextLine().trim();
		System.out.printf("내용: ");
		String body = sc.nextLine().trim();

		insertQuery(title, body);
		System.out.println("게시물이 생성되었습니다.");
	}

	// 목록
	public void showList() {
		System.out.println("== 게시물 목록 ==");

		selectQuery();

		// 게시물이 존재하지 않을 때
		if (this.articles.size() == 0) {
			System.out.println("게시물이 없습니다.");
			return;
		}

		System.out.println("번호	|	제목");
		for (Article article : articles) {
			System.out.printf("%d	|	%s\n", article.id, article.title);
		}
	}

	// 수정
	public void doModify() {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		System.out.printf("== %d번 게시물 수정 ==\n", id);
		System.out.printf("수정할 제목: ");
		String title = sc.nextLine().trim();
		System.out.printf("수정할 내용: ");
		String body = sc.nextLine().trim();

		updateQuery(id, title, body);

		System.out.printf("%d번 게시물이 수정 되었습니다.\n", id);
	}

	// insert
	public void insertQuery(String title, String body) {
		try {
			String sql = "INSERT INTO article";
			sql += " SET regDate = NOW()";
			sql += ", updateDate = NOW()";
			sql += ", title = '" + title + "'";
			sql += ", `body` = '" + body + "';";

			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("에러: " + e);
		}
	}

	// select
	public void selectQuery() {
		this.articles = new ArrayList<>();
		try {
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

		} catch (SQLException e) {
			System.out.println("에러: " + e);
		}
	}

	// update
	public void updateQuery(int id, String title, String body) {
		try {
			String sql = "UPDATE article";
			sql += " SET updateDate = NOW()";
			sql += ", title = '" + title + "'";
			sql += ", `body` = '" + body + "'";
			sql += " WHERE id = " + id + ";";

			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("에러: " + e);
		}
	}
}
