package com.koreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.koreaIT.example.JAM.dto.Article;
import com.koreaIT.example.JAM.util.DBUtil;
import com.koreaIT.example.JAM.util.SecSql;

public class App {
	public void run() {
		Scanner sc = new Scanner(System.in);

		System.out.println("== 프로그램 시작 ==");

		Connection conn = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
			conn = DriverManager.getConnection(url, "root", "");

			while (true) {
				System.out.printf("명령어) ");
				String cmd = sc.nextLine().trim();

				if (cmd.equals("exit")) {
					break;
				}

				// 작성
				if (cmd.equals("article write")) {

					System.out.println("== 게시물 작성 ==");

					System.out.printf("제목 : ");
					String title = sc.nextLine().trim();
					System.out.printf("내용 : ");
					String body = sc.nextLine().trim();

					SecSql sql = SecSql.from("INSERT INTO article");
					sql.append("SET regDate = NOW()");
					sql.append(", updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?", body);

					int id = DBUtil.insert(conn, sql);

					System.out.printf("%d번 게시물이 생성되었습니다\n", id);
				}

				// 목록
				else if (cmd.equals("article list")) {

					System.out.println("== 게시물 목록 ==");

					List<Article> articles = new ArrayList<>();

					SecSql sql = SecSql.from("SELECT * FROM article");
					sql.append("ORDER BY id DESC");

					List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

					for (Map<String, Object> articleMap : articleListMap) {
						articles.add(new Article(articleMap));
					}

					if (articles.size() == 0) {
						System.out.println("존재하는 게시물이 없습니다");
						continue;
					}

					System.out.println("번호	|	제목	|	날짜");
					for (Article article : articles) {
						System.out.printf("%d	|	%s	|	%s\n", article.id, article.title, article.regDate);
					}
				}

				// 수정
				else if (cmd.startsWith("article modify ")) {
					int id = Integer.parseInt(cmd.split(" ")[2]);

					SecSql sql = SecSql.from("SELECT count(*) FROM article");
					sql.append("WHERE id = ?", id);

					int articleCnt = DBUtil.selectRowIntValue(conn, sql);

					if (articleCnt == 0) {
						System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
						continue;
					}

					System.out.println("== 게시물 수정 ==");
					System.out.printf("수정할 제목 : ");
					String title = sc.nextLine().trim();
					System.out.printf("수정할 내용 : ");
					String body = sc.nextLine().trim();

					sql = SecSql.from("UPDATE article");
					sql.append("SET updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?", body);
					sql.append("WHERE id = ?", id);

					DBUtil.update(conn, sql);

					System.out.printf("%d번 게시물이 수정되었습니다\n", id);

				}

				// 조회
				else if (cmd.startsWith("article detail ")) {
					int id = Integer.parseInt(cmd.split(" ")[2]);

					SecSql sql = SecSql.from("SELECT * FROM article");
					sql.append("WHERE id = ?", id);

					Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

					if (articleMap.isEmpty()) {
						System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
						continue;
					}

					Article article = new Article(articleMap);

					System.out.printf("== %d번 게시물 상세보기 ==\n", id);
					System.out.println("번  호: " + article.id);
					System.out.println("작성일: " + article.regDate);
					System.out.println("수정일: " + article.updateDate);
					System.out.println("제  목: " + article.title);
					System.out.println("내  용: " + article.body);
				}

				// 삭제
				else if (cmd.startsWith("article delete ")) {
					int id = Integer.parseInt(cmd.split(" ")[2]);

					SecSql sql = SecSql.from("SELECT count(*) FROM article");
					sql.append("WHERE id = ?", id);

					int articleCnt = DBUtil.selectRowIntValue(conn, sql);

					if (articleCnt == 0) {
						System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
						continue;
					}

					System.out.println("== 게시물 삭제 ==");

					sql = SecSql.from("DELETE FROM article");
					sql.append("WHERE id = ?", id);
					DBUtil.delete(conn, sql);

					System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
				}

				// 회원가입
				else if (cmd.equals("member join")) {
					
					System.out.println("== 회원가입 ==");
					
					String loginId = null;
					while (true) {
						System.out.printf("아이디: ");
						loginId = sc.nextLine().trim();

						if (loginId.length() == 0) {
							System.out.println("아이디는 필수입력정보입니다.");
							continue;
						}
						
						SecSql sql = SecSql.from("SELECT COUNT(*) > 0 FROM `member`");
						sql.append("WHERE loginId = ?", loginId);
						
						boolean isLoginIdDup = DBUtil.selectRowBooleanValue(conn, sql);
						
						if(isLoginIdDup) {
							System.out.printf("%s은(는) 이미 사용중인 아이디입니다.\n", loginId);
							continue;
						}

						System.out.printf("%s은(는) 사용가능한 아이디입니다.\n", loginId);
						break;
					}

					String loginPw = null;
					while (true) {
						System.out.printf("비밀번호: ");
						loginPw = sc.nextLine().trim();

						if (loginPw.length() == 0) {
							System.out.println("비밀번호는 필수입력정보입니다.");
							continue;
						}
						
						System.out.printf("비밀번호확인: ");
						String loginPwChk = sc.nextLine().trim();
						
						if(!loginPw.equals(loginPwChk)) {
							System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
							continue;
						}

						break;
					}

					String name = null;
					while (true) {
						System.out.printf("이름: ");
						name = sc.nextLine().trim();
						
						if (name.length() == 0) {
							System.out.println("이름은 필수입력정보입니다.");
							continue;
						}
						break;
					}
					
					SecSql sql = SecSql.from("INSERT INTO `member`");
					sql.append("SET regDate = NOW()");
					sql.append(", updateDate = NOW()");
					sql.append(", loginId = ?", loginId);
					sql.append(", loginPw = ?", loginPw);
					sql.append(", `name` = ?", name);
					                        
					DBUtil.insert(conn, sql);
					
					System.out.printf("%s 회원님이 가입되었습니다.", name);
				}

				else {
					System.out.println("존재하지 않는 명령어 입니다");
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
			closeResources(conn);
		}

		sc.close();

		System.out.println("== 프로그램 종료 ==");
	}

	private void closeResources(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
