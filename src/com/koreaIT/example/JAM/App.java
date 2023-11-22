package com.koreaIT.example.JAM;

import java.util.Scanner;

import com.koreaIT.example.JAM.controller.ArticleController;

public class App {
	void run() {
		Scanner sc = new Scanner(System.in);
		ArticleController articleController = new ArticleController(sc);
		
		System.out.println("== 프로그램 시작 ==");
		
		while (true) {
			System.out.printf("명령어) ");
			String cmd = sc.nextLine().trim();

			if (cmd.equals("exit")) {
				break;
			}

			// 작성
			if (cmd.equals("article write")) {
				articleController.doWrite();
			} else if (cmd.equals("article list")) {
				articleController.showList();
			} else {
				System.out.println("존재하지 않는 명령어 입니다.");
			}
		}

		sc.close();
		
		System.out.println("== 프로그램 종료 ==");
	}
}