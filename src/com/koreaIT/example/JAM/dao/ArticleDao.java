package com.koreaIT.example.JAM.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.koreaIT.example.JAM.util.DBUtil;
import com.koreaIT.example.JAM.util.SecSql;

public class ArticleDao {
	private Connection conn;
	
	public ArticleDao(Connection conn) {
		this.conn = conn;
	}

	public int doWrite(String title, String body, int memberId) {
		SecSql sql = SecSql.from("INSERT INTO article");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);
		sql.append(", memberId = ?", memberId);

		return DBUtil.insert(conn, sql);
	}

	public List<Map<String, Object>> showList() {
		SecSql sql = SecSql.from("SELECT * FROM article");
		sql.append("ORDER BY id DESC");
		
		return DBUtil.selectRows(conn, sql);
	}
	

	public int getArticleCnt(int id) {
		SecSql sql = SecSql.from("SELECT count(*) FROM article");
		sql.append("WHERE id = ?", id);
		return DBUtil.selectRowIntValue(conn, sql);
	}

	public void doModify(int id, String title, String body) {
		SecSql sql = SecSql.from("UPDATE article");
		sql.append("SET updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);
		sql.append("WHERE id = ?", id);

		DBUtil.update(conn, sql);
	}

	public Map<String, Object> getArticleMap(int id) {
		SecSql sql = SecSql.from("SELECT * FROM article");
		sql.append("WHERE id = ?", id);
		return DBUtil.selectRow(conn, sql);
	}

	public void doDelete(int id) {
		SecSql sql = SecSql.from("DELETE FROM article");
		sql.append("WHERE id = ?", id);
		DBUtil.delete(conn, sql);
	}

	public String getNameByArticleId(int articleId) {
		SecSql sql = SecSql.from("SELECT m.name AS `writerName` FROM `member` AS m");
		sql.append("INNER JOIN article AS a");
		sql.append("ON m.id = a.memberId");
		sql.append("WHERE a.id = ?", articleId);
		
		return DBUtil.selectRowStringValue(conn, sql);
	}

	public boolean checkAuthority(int id, int memberId) {
		SecSql sql = SecSql.from("SELECT");
		sql.append("memberId = ? FROM article", memberId);
		sql.append("WHERE id = ?", id);

		return DBUtil.selectRowBooleanValue(conn, sql);
	}

	
}
