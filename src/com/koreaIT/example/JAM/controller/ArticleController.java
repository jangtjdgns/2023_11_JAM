package com.koreaIT.example.JAM.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.koreaIT.example.JAM.dto.Article;

public class ArticleController {
	List<Article> articles;
	int lastArticleId;
	Scanner sc;
	
	public ArticleController(Scanner sc){
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

		Article article = new Article(++this.lastArticleId, title, body);

		this.articles.add(article);
		System.out.printf("%d번 게시물이 생성되었습니다.\n", this.lastArticleId);
	}
	
	public void showList() {
		System.out.println("== 게시물 목록 ==");
		
		// 게시물이 존재하지 않을 때
		if (this.articles.size() == 0) {
			System.out.println("게시물이 없습니다.");
			return;
		}

		System.out.println("번호	|	제목");
		for(int i = this.articles.size() - 1; i >= 0; i--) {
			Article article = articles.get(i);
			System.out.printf("%d	|	%s\n", article.id, article.title);
		}
	}
}
