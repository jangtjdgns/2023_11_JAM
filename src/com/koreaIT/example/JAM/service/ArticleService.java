package com.koreaIT.example.JAM.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.koreaIT.example.JAM.dao.ArticleDao;
import com.koreaIT.example.JAM.dto.Article;

public class ArticleService {
	private ArticleDao articleDao;

	public ArticleService(Connection conn) {
		this.articleDao = new ArticleDao(conn);
	}

	public int doWrite(String title, String body, int memberId) {
		return articleDao.doWrite(title, body, memberId);
	}

	public List<Article> showList(String searchKeyword) {
		List<Map<String, Object>> articleListMap = articleDao.showList(searchKeyword);

		List<Article> articles = new ArrayList<>();

		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}

		return articles;
	}

	public int getNumInCmd(String cmd) {
		return Integer.parseInt(cmd.split(" ")[2]);
	}

	public int getArticleCnt(int id) {
		return articleDao.getArticleCnt(id);
	}

	public void doModify(int id, String title, String body) {
		articleDao.doModify(id, title, body);
	}

	public Map<String, Object> getArticleMap(int id) {
		return articleDao.getArticleMap(id);
	}

	public void doDelete(int id) {
		articleDao.doDelete(id);
	}

	public Article showDetail(int id) {
		Map<String, Object> articleMap = articleDao.getArticleMap(id);
		if (articleMap.isEmpty()) {
			return null;
		}

		return new Article(articleMap);
	}

	public boolean checkAuthority(int id, int memberId) {
		return articleDao.checkAuthority(id, memberId);
	}
}
