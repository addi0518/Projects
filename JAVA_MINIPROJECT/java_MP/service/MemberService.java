package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import data.Member;
import util.AESAlgorithm;

public class MemberService {
  public static List<Member> member_List = new ArrayList<Member>();
  public static Scanner sc = new Scanner(System.in);
  public static Member loginMember = null;

  public MemberService() {}
  public static void memberJoin() throws Exception { //회원가입
    System.out.print("아이디: ");
    String tmp_id =  sc.nextLine();
    if (isDuplicatedId(tmp_id) == true) {
      System.out.println(tmp_id + "은(는) 이미 등록된 아이디 입니다.");
      return;
    }
    if (tmp_id.length() < 6) {
      System.out.println("아이디를 6자 이상 입력해주세요.");
      return;
    }
    System.out.print("비밀번호: ");
    String tmp_pwd =  sc.nextLine();
    if (tmp_pwd.length() < 6) {
    System.out.println("비밀번호를 6자 이상 입력해주세요.");
      return;
    }
    System.out.print("닉네임: ");
    String tmp_name =  sc.nextLine();
    if (tmp_name.length() == 0) {
      System.out.println("닉네임을 입력해주세요.");
      return;
    }
    System.out.print("주민번호 13자리('-'없이 입력): ");
    String tmp_regist_number =  sc.nextLine();
    if (tmp_regist_number.length() != 13) {
      System.out.println("잘못된 주민등록번호 입니다.");
      return;
    }
    if (isDuplicatedRegist(tmp_regist_number) == true) {
      System.out.println("이미 가입된 사람입니다.");
      return;
    }
    
    String pwd1 = AESAlgorithm.Encrypt(tmp_pwd);
    Member m = new Member(tmp_id, pwd1, tmp_name, tmp_regist_number, 1);
    member_List.add(m);
    
    BufferedWriter w = new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(new File("data_files/member.dat")), "UTF-8"));
      for(int i=0; i<member_List.size(); i++){
        w.write(member_List.get(i).makeDataString() + "\r\n");
      }
      w.close();

      System.out.println("회원 등록이 완료되었습니다");
  }

  public static Member logIn(String tmp_id, String tmp_pwd) throws Exception { //로그인
    loginMember = null;
    Boolean leavemem = false;
    String pwd = AESAlgorithm.Encrypt(tmp_pwd);
    for (Member m: member_List) {
      if (m.id.equals(tmp_id) && m.pwd.equals(pwd)) {
        if (m.member_state == 0) {
          System.out.println("이미 탈퇴한 회원입니다.");
          leavemem = true;
          m = null;
        } else {
          loginMember = m;
          System.out.println("로그인 되었습니다.");
        }
      }
    }
    if (loginMember == null && leavemem == false) {
      System.out.println("아이디, 비밀번호가 잘못되었거나 없는 회원입니다.");
    }
    return loginMember;
  }

  public static void memberLeave(Member m) throws Exception { // 회원탈퇴
    BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("data_files/member.dat")), "UTF-8"));
    m.member_state = 0; //원래의 메모리 내용에서 탈퇴 처리
    for(int i=0; i<member_List.size(); i++){
      w.write(member_List.get(i).makeDataString()+"\r\n");
    }
    w.close();
    System.out.println("탈퇴 되었습니다.");
  }

  public static void memberModi(Member m) throws Exception {
    System.out.print("닉네임 입력: ");
    String tmp_name = sc.nextLine();
    m.name = tmp_name;
    BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("data_files/member.dat")), "UTF-8"));
    for(int i=0; i<member_List.size(); i++){
      w.write(member_List.get(i).makeDataString()+"\r\n");
    }
    w.close();
    System.out.println("수정 되었습니다.");
  }

  public static void logOut(Member m) {
    m = null;
    System.out.println("로그아웃 되었습니다.");
  }

  public static void showMemberList() {
    if (member_List.size() == 0) {
      System.out.println("등록된 회원이 없습니다.");
    } else {
      for (Member m: member_List) {
        System.out.println(m);
      }
    }
  }

  public static Boolean isDuplicatedId(String id) {
    for (Member m: member_List) {
      if (m.id.equals(id)) return true; //중복검출
    }
    return false; //리스트를 모두 조회하고나서 여기로 나올 수 있다면
    // 중복 아이디가 없다는 것
  }

  public static Boolean isDuplicatedRegist(String regist_number) { //주민번호로 동일인물 검사
    for (Member m: member_List) {
      if (m.regist_number.equals(regist_number))
        return true; // 중복검출
    }
    return false; // 리스트를 모두 조회하고나서 여기로 나올 수 있다면
    // 중복 아이디가 없다는 것
  }
}
