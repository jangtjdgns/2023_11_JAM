package com.koreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import com.koreaIT.example.JAM.dto.Article;
import com.koreaIT.example.JAM.service.ArticleService;
import com.koreaIT.example.JAM.session.Session;
import com.koreaIT.example.JAM.util.Util;

public class ArticleController {
	private Scanner sc;
	private ArticleService articleService;

	public ArticleController(Scanner sc, Connection conn) {
		this.sc = sc;
		this.articleService = new ArticleService(conn);
	}

	public void doWrite() {
		if (!Session.isLogined()) {
			System.out.println("로그인 후 이용가능합니다.");
			return;
		}

		System.out.println("== 게시물 작성 ==");

		System.out.printf("제목 : ");
		String title = sc.nextLine().trim();
		System.out.printf("내용 : ");
		String body = sc.nextLine().trim();

		int id = articleService.doWrite(title, body, Session.getMemberId());

		System.out.printf("%d번 게시물이 생성되었습니다\n", id);
	}

	public void showList() {
		System.out.println("== 게시물 목록 ==");

		List<Article> articles = articleService.showList();

		if (articles.size() == 0) {
			System.out.println("존재하는 게시물이 없습니다");
			return;
		}

		System.out.println("번호	|	제목	|	작성자	|		작성일");
		for (Article article : articles) {
			String name = articleService.getNameByArticleId(article.id);
			System.out.printf("%d	|	%s	|	%s	|	%s\n", article.id, article.title, name,
					Util.datetimeFormat(article.regDate));
		}
	}

	public void doModify(String cmd) {
		if (!Session.isLogined()) {
			System.out.println("로그인 후 이용가능합니다.");
			return;
		}

		int id = articleService.getNumInCmd(cmd);

		int articleCnt = articleService.getArticleCnt(id);

		if (articleCnt == 0) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}

		boolean checkAuthority = articleService.checkAuthority(id, Session.getMemberId());

		if (!checkAuthority) {
			System.out.printf("%d번 게시물에 대한 권한이 없습니다.\n", id);
			return;
		}

		System.out.println("== 게시물 수정 ==");
		System.out.printf("수정할 제목 : ");
		String title = sc.nextLine().trim();
		System.out.printf("수정할 내용 : ");
		String body = sc.nextLine().trim();

		articleService.doModify(id, title, body);

		System.out.printf("%d번 게시물이 수정되었습니다\n", id);
	}

	public void showDetail(String cmd) {
		int id = articleService.getNumInCmd(cmd);

		Article article = articleService.showDetail(id);

		if (article == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}

		System.out.printf("== %d번 게시물 상세보기 ==\n", id);
		System.out.println("번  호: " + article.id);
		System.out.println("작성일: " + Util.datetimeFormat(article.regDate));
		System.out.println("수정일: " + Util.datetimeFormat(article.updateDate));
		System.out.println("작성자: " + articleService.getNameByArticleId(article.id));
		System.out.println("제  목: " + article.title);
		System.out.println("내  용: " + article.body);
	}

	public void doDelete(String cmd) {
		if (!Session.isLogined()) {
			System.out.println("로그인 후 이용가능합니다.");
			return;
		}

		int id = articleService.getNumInCmd(cmd);

		int articleCnt = articleService.getArticleCnt(id);

		if (articleCnt == 0) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}

		boolean checkAuthority = articleService.checkAuthority(id, Session.getMemberId());

		if (!checkAuthority) {
			System.out.printf("%d번 게시물에 대한 권한이 없습니다.\n", id);
			return;
		}

		System.out.println("== 게시물 삭제 ==");

		articleService.doDelete(id);

		System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
	}
}
