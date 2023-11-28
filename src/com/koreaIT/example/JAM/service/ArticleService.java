package com.koreaIT.example.JAM.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.koreaIT.example.JAM.dao.ArticleDao;

public class ArticleService {
	private ArticleDao articleDao;
	
	public ArticleService(Connection conn){
		this.articleDao = new ArticleDao(conn);
	}

	public int doWrite(String title, String body) {
		return articleDao.doWrite(title, body);
	}

	public List<Map<String, Object>> showList() {
		return articleDao.showList();
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
}
