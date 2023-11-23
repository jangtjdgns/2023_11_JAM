package com.koreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.koreaIT.example.JAM.controller.ArticleController;

public class App {
	public void run() {
        Scanner sc = new Scanner(System.in);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        System.out.println("== 프로그램 시작 ==");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
            conn = DriverManager.getConnection(url, "root", "123456");

            while (true) {
                System.out.printf("명령어) ");
                String cmd = sc.nextLine().trim();

                ArticleController articleController = new ArticleController(sc, cmd, conn, pstmt, rs);

                if (cmd.equals("exit")) {
                    break;
                }

                if (cmd.equals("article write")) {
                    articleController.doWrite();
                } else if (cmd.equals("article list")) {
                    articleController.showList();
                } else if (cmd.startsWith("article modify ")) {
                    articleController.doModify();
                } else {
                    System.out.println("존재하지 않는 명령어 입니다.");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 로딩 실패");
        } catch (SQLException e) {
            System.out.println("에러: " + e);
        } finally {
            closeResources(conn, pstmt, rs);
        }

        sc.close();
        System.out.println("== 프로그램 종료 ==");
    }

    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null && !pstmt.isClosed()) {
                pstmt.close();
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