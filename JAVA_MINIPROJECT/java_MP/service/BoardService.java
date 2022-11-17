package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import data.Financeboard;
import data.Member;
import util.AESAlgorithm;

public class BoardService {
  public static List<Financeboard> financeboard_List = new ArrayList<Financeboard>();
  public static List<Financeboard > mfb_List = new ArrayList < Financeboard > (); //빈 객체(개인 게시글 담는 용), 빈 메모장
  public static Scanner sc = new Scanner(System.in);

  public static Boolean showFinaceboardList() throws IOException { //전체 게시글 보기
    BufferedWriter w = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(new File("data_files/board.dat")), "UTF-8"));
    if (financeboard_List.size() == 0) {
      System.out.println("게시글이 없습니다.");
      return false;
    } else {
      for (int i=0; i<financeboard_List.size(); i++) {
        financeboard_List.get(i).post_no = i;
        System.out.println(financeboard_List.get(i)); //출력
        w.write(financeboard_List.get(i).makeDataString() + "\r\n");
      }
      w.close();
      return true;
    }
  }
  
  public static void addFinaceboardList(Member m) throws IOException { //게시글 등록
    System.out.print("제목을 입력하세요: ");
    String title = sc.nextLine();
    System.out.print("내용을 입력하세요: ");
    String content = sc.nextLine();
    Date post_reg_dt = new Date();
    Financeboard fb = new Financeboard(null, title, m.id, m.name, content, post_reg_dt, post_reg_dt, 1);
    financeboard_List.add(fb);
    BufferedWriter w = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(new File("data_files/board.dat")), "UTF-8"));
    for (int i = 0; i < financeboard_List.size(); i++) {
      financeboard_List.get(i).post_no = i;
      w.write(financeboard_List.get(i).makeDataString() + "\r\n");
    }
    w.close();
    System.out.println("글이 등록되었습니다.");
  }

  public static void readFinanceboard(int i) { //게시글 내용 보기
    String tmp_title = financeboard_List.get(i).post_title;
    String tmp_name = financeboard_List.get(i).post_name;
    String tmp_content = financeboard_List.get(i).post_content;
    System.out.println("----" + i + "번 게시글----");
    System.out.println("제목: " + tmp_title);
    System.out.println("작성자: " + tmp_name);
    System.out.println("내용: " + tmp_content);
  }

  public static Boolean showMemberFinanceboard(Member m) { // 사용자의 게시글 보여주기
    Boolean exist = false;
    if(financeboard_List.size() != 0){
      for (Financeboard f: financeboard_List) {
        if (f.post_state != 0 && f.post_id.equals(m.id)) {
          System.out.println(f);
          exist = true; //사용자의 게시글이 있음
        }
      }
      if(!exist){ //게시글이 없음
        System.out.println("등록한 게시글이 없습니다.");
        return false;
      }
      return true;
    }else{
      System.out.println("등록한 게시글이 없습니다.");
      return false;
    }
  }

  public static void deleteMemberFinanceboard(Member m) throws IOException { //사용자 게시글 삭제
    List < Integer > exsit_no = new ArrayList < Integer > ();
    
    for (Financeboard f: financeboard_List) {
      if (f.post_state != 0 && f.post_id.equals(m.id)) {
        System.out.println(f);
        exsit_no.add(f.post_no); //글번호 리스트에 저장
      }
    }
    System.out.print("삭제할 게시글 번호: ");
    Integer no = sc.nextInt();
    if (exsit_no.contains(no)) { // exist_no에 그 숫자가 있다면 
      financeboard_List.get(no).post_state = 0; //삭제 처리
      BufferedWriter w = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream(new File("data_files/board.dat")), "UTF-8"));
      for (int i = 0; i < financeboard_List.size(); i++) {
        w.write(financeboard_List.get(i).makeDataString() + "\r\n");
      }
      w.close();
      System.out.println("삭제 완료");

    } else {
      System.out.println("존재하지 않는 번호입니다.");
    }
    
  }

  public static void modifyMemberFinanceboard(Member m) throws IOException { //게시판 수정
    List < Integer > exsit_no = new ArrayList < Integer > ();
    for (Financeboard f: financeboard_List) {
      if (f.post_state != 0 && f.post_id.equals(m.id)) {
        System.out.println(f);
        exsit_no.add(f.post_no); //글번호 리스트에 저장
      }
    }
    System.out.print("수정할 게시글 번호: ");
    Integer no = sc.nextInt(); //스캔
    sc.nextLine();
    if (exsit_no.contains(no)) { //exist_no에 그 숫자가 있다면 수정
      System.out.print("수정 제목: ");
      String title = sc.nextLine();
      System.out.print("수정 내용: ");
      String content = sc.nextLine();
      Integer modi = 2;

      Date post_reg_dt = financeboard_List.get(no).post_reg_dt;
      Date post_mod_dt = new Date();
      Financeboard fb = new Financeboard(no, title, m.id, m.name, content, post_reg_dt, post_mod_dt, modi);
      financeboard_List.set(no ,fb);
      
      BufferedWriter w = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream(new File("data_files/board.dat")), "UTF-8"));
      for (int i = 0; i < financeboard_List.size(); i++) {
        w.write(financeboard_List.get(i).makeDataString() + "\r\n");
      }
      w.close();

      System.out.println("수정 완료");
    } else {
      System.out.println("존재하지 않는 번호입니다.");
    }
  }
}