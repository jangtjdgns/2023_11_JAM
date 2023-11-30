package com.koreaIT.example.JAM.session;

import com.koreaIT.example.JAM.dto.Member;

public class Session {
	private static int loginedMemberId;
	private static Member loginedMember;
	
	static {
		loginedMemberId = -1;
		loginedMember = null;
	}

	public static void login(Member member) {
		loginedMemberId = member.id;
		loginedMember = member;
	}
	
	public static void logout() {
		loginedMemberId = -1;
		loginedMember = null;
	}
	
	public static boolean isLogined() {
		return loginedMemberId != -1;
	}

	public static Member getMember() {
		return loginedMember;
	}

	public static int getMemberId() {
		return loginedMemberId;
	}
}
