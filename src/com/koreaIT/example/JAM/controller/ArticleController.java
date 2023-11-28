package com.koreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.koreaIT.example.JAM.dto.Article;
import com.koreaIT.example.JAM.service.ArticleService;

public class ArticleController {
	private Scanner sc;
	private ArticleService articleService;
	
	public ArticleController(Scanner sc, Connection conn) {
		this.sc = sc;
		this.articleService = new ArticleService(conn);
	}

	public void doWrite() {
		System.out.println("== 게시물 작성 ==");

		System.out.printf("제목 : ");
		String title = sc.nextLine().trim();
		System.out.printf("내용 : ");
		String body = sc.nextLine().trim();

		int id = articleService.doWrite(title, body);

		System.out.printf("%d번 게시물이 생성되었습니다\n", id);
	}

	public void showList() {
		System.out.println("== 게시물 목록 ==");
		
		List<Article> articles = new ArrayList<>();
		
		List<Map<String, Object>> articleListMap = articleService.showList();

		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}

		if (articles.size() == 0) {
			System.out.println("존재하는 게시물이 없습니다");
			return;
		}

		System.out.println("번호	|	제목	|	날짜");
		for (Article article : articles) {
			System.out.printf("%d	|	%s	|	%s\n", article.id, article.title, article.regDate);
		}
	}

	public void doModify(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		int articleCnt = articleService.getArticleCnt(id);
		
		if (articleCnt == 0) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
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
		int id = Integer.parseInt(cmd.split(" ")[2]);
		
		Map<String, Object> articleMap = articleService.getArticleMap(id);
		
		if (articleMap.isEmpty()) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}

		Article article = new Article(articleMap);

		System.out.printf("== %d번 게시물 상세보기 ==\n", id);
		System.out.println("번  호: " + article.id);
		System.out.println("작성일: " + article.regDate);
		System.out.println("수정일: " + article.updateDate);
		System.out.println("제  목: " + article.title);
		System.out.println("내  용: " + article.body);
	}

	public void doDelete(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		int articleCnt = articleService.getArticleCnt(id);

		if (articleCnt == 0) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}

		System.out.println("== 게시물 삭제 ==");

		articleService.doDelete(id);

		System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
	}
}
