package com.koreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.Scanner;

import com.koreaIT.example.JAM.service.MemberService;

public class MemberController {
	private Scanner sc;
	private MemberService memberService;
	
	public MemberController(Scanner sc, Connection conn) {
		this.sc = sc;
		this.memberService = new MemberService(conn);
	}

	public void doJoin() {
		System.out.println("== 회원가입 ==");
		
		String loginId = null;
		while (true) {
			System.out.printf("아이디: ");
			loginId = sc.nextLine().trim();

			if (loginId.length() == 0) {
				System.out.println("아이디는 필수입력정보입니다.");
				continue;
			}
			
			boolean isLoginIdDup = memberService.isLoginIdDup(loginId);
			
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
		
		memberService.doJoin(loginId, loginPw, name);
		
		System.out.printf("%s 회원님이 가입되었습니다.\n", name);
	}
}
