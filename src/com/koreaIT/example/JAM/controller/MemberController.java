package com.koreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.Scanner;

import com.koreaIT.example.JAM.dto.Member;
import com.koreaIT.example.JAM.service.MemberService;
import com.koreaIT.example.JAM.session.Session;
import com.koreaIT.example.JAM.util.Util;

public class MemberController {
	private Scanner sc;
	private MemberService memberService;

	public MemberController(Scanner sc, Connection conn) {
		this.sc = sc;
		this.memberService = new MemberService(conn);
	}

	public void doJoin() {
		if (Session.isLogined()) {
			System.out.println("로그아웃 후 이용가능합니다.");
			return;
		}
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

			if (isLoginIdDup) {
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

			if (!loginPw.equals(loginPwChk)) {
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

	public void doLogin() {
		if (Session.isLogined()) {
			System.out.println("로그아웃 후 이용가능합니다.");
			return;
		}
		System.out.println("== 로그인 ==");

		String loginId = null;
		String loginPw = null;
		Member member = null;

		while (true) {
			System.out.printf("로그인 아이디: ");
			loginId = sc.nextLine().trim();
			System.out.printf("로그인 비밀번호: ");
			loginPw = sc.nextLine().trim();

			if (loginId.length() == 0) {
				System.out.println("아이디를 입력해주세요.");
				continue;
			}

			if (loginPw.length() == 0) {
				System.out.println("비밀번호를 입력해주세요.");
				continue;
			}

			member = memberService.getMemberByLoginId(loginId);

			if (member == null) {
				System.out.println(loginId + "은(는) 존재하지 않는 아이디입니다.");
				continue;
			}

			if (!member.loginPw.equals(loginPw)) {
				System.out.println("비밀번호가 일치하지 않습니다.");
				continue;
			}

			Session.login(member);
			break;
		}

		System.out.printf("%s님이 로그인 되었습니다.\n", member.name);
	}

	public void doLogout() {
		if (!Session.isLogined()) {
			System.out.println("로그인 후 이용가능합니다.");
			return;
		}

		Session.logout();
		System.out.println("로그아웃 되었습니다.");
	}

	public void showProfile() {
		if (!Session.isLogined()) {
			System.out.println("로그인 후 이용가능합니다.");
			return;
		}

		System.out.println("== 마이페이지 ==");

		Member member = Session.getMember();

		System.out.println("가입일: " + Util.datetimeFormat(member.regDate));
		System.out.println("수정일: " + Util.datetimeFormat(member.updateDate));
		System.out.println("로그인 아이디: " + member.loginId);
		System.out.println("이름: " + member.name);
	}
}
