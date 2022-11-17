package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import data.FinanceComment;
import data.Financeboard;
import data.Member;

public class CommentService {
  public static List<FinanceComment> financecomment_List = new ArrayList<FinanceComment>();
  public static Scanner sc = new Scanner(System.in);
  static Boolean exist = true;
  
  //게시글 전체의 댓글을 찾음
  public static void showFinanceComment(int post_no){ //게시판 번호를 받아옴
    if(financecomment_List.size() == 0){
      System.out.println("-----댓글-----");
      System.out.println("댓글이 없습니다.");
    }
    else {
      System.out.println("-----댓글-----");
      for (int i = 0; i < financecomment_List.size(); i++) { // 댓글들의 수만큼 돌린다
        if (post_no == financecomment_List.get(i).fboard_no) { // 입력한 게시글 번호와 같은 게시 글 번호를 찾는다
          Integer tmp_comment_no = financecomment_List.get(i).comment_no;
          String tmp_comment_content = financecomment_List.get(i).comment_content;
          String tmp_comment_name = financecomment_List.get(i).comment_name;
          Date tmp_reg_dt = financecomment_List.get(i).comment_reg_dt;
          SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

          System.out.println("번호: " + tmp_comment_no + " / 작성자: " + tmp_comment_name);
          System.out.println("내용: " + tmp_comment_content);
          System.out.println("작성일: " + f.format(tmp_reg_dt)); // 입력한 게시글 번호의 댓글을 읽어온다
          exist = false;
        }
      }
      if(exist){
        System.out.println("댓글이 없습니다.");
      }
    }
  }

  //해당 게시글의 댓글을 불러옴. i는 게시글 번호
  // public static void readFinanceCommentBoard(int i){ 
  //   for(FinanceComment fc : financecomment_List){
  //     if(fc.fboard_no == i){
  //       Integer tmp_comment_no = financecomment_List.get(i).comment_no;
  //       String tmp_comment_content = financecomment_List.get(i).comment_content;
  //       String tmp_comment_name = financecomment_List.get(i).comment_name;
  //       Date tmp_reg_dt = financecomment_List.get(i).comment_reg_dt;
  //       SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
    
  //       System.out.println("번호: "+tmp_comment_no+" / 작성자: "+tmp_comment_name);
  //       System.out.println("내용: "+tmp_comment_content);
  //       System.out.println("작성일: "+f.format(tmp_reg_dt));
  //     }
  //   }
  // }

  public static void addFinanceCommentBoard(int post_no, Member m) throws IOException { //댓글추가
    Date post_reg_dt = new Date();
    System.out.print("댓글 내용: ");
    String content = sc.nextLine();
    Integer no = financecomment_List.size();
    FinanceComment fbc = new FinanceComment(post_no, no, m.name, m.id, content, post_reg_dt, post_reg_dt, 1);
    financecomment_List.add(fbc);
    
    BufferedWriter w = new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(new File("data_files/comment.dat")), "UTF-8"));
      for(int i=0; i<financecomment_List.size(); i++){
        w.write(financecomment_List.get(i).makeDataString() + "\r\n");
      }
      w.close();
  }

  public static Boolean showMemberFinanceComment(Member m){ //작성자 모든 댓글 보기
    Boolean exist = false;
    if(financecomment_List.size() != 0){
      for(FinanceComment f : financecomment_List){
        if(f.comment_state != 0 && f.comment_id.equals(m.id)){
          System.out.println(f);
          exist = true;
        }
      }
      if(!exist){
        System.out.println("등록한 댓글이 없습니다.");
        return false;
      }
      return true;
    }else{
      System.out.println("댓글이 없습니다.");
      return false;
    }
  }

  public static void deleteMemberFinanceComment(Member m) throws IOException{ //댓글 삭제
    List<Integer> exist_no = new ArrayList<Integer>();
    for(FinanceComment f : financecomment_List){
      if(f.comment_state != 0 && f.comment_id.equals(m.id)){
        System.out.println(f);
        exist_no.add(f.fboard_no); //글번호 리스트에 저장
      }
    }
    System.out.println("삭제할 게시글 번호");
    Integer no = sc.nextInt();
    if(exist_no.contains(no)){ //exist_no에 그 숫자가 있다면 삭제
      financecomment_List.get(no).comment_state = 0;
      BufferedWriter w = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(new File("data_files/comment.dat")), "UTF-8"));
        for(int i=0; i<financecomment_List.size(); i++){
          w.write(financecomment_List.get(i).makeDataString() + "\r\n");
        }
        w.close();
      System.out.println("삭제 완료");
    }else{
      System.out.println("존재하지 않는 번호입니다.");
    }
  }

  public static void modifyMemberComment(Member m) throws IOException{ //댓글 수정
    List<Integer> exist_no = new ArrayList<Integer>();
    for (int i=0; i<financecomment_List.size(); i++) {
      if (financecomment_List.get(i).comment_state != 0 && financecomment_List.get(i).comment_id.equals(m.id)) {
        System.out.println(financecomment_List.get(i)); //조건에 맞는 댓글 객체 리스트에 저장
        exist_no.add(financecomment_List.get(i).comment_no);
      }
    }
    System.out.print("수정할 댓글 번호: ");
    Integer no = sc.nextInt();
    if (exist_no.contains(no)) { // 수정할 댓글 번호를 가지고 있다면
      for (FinanceComment f : financecomment_List) {
        if (f.comment_state != 0 && f.comment_id.equals(m.id) && f.comment_no == no) {
          System.out.print("수정할 내용: ");
          sc.nextLine();
          String content = sc.nextLine(); // 내용 입력
          f.comment_state = 2;
          f.comment_mod_dt = new Date();
          f.comment_content = content;
          f.comment_name = m.name;
        }
      }
      BufferedWriter w = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream(new File("data_files/comment.dat")), "UTF-8"));
      for (int i = 0; i < financecomment_List.size(); i++) {
        w.write(financecomment_List.get(i).makeDataString() + "\r\n");
      }
      w.close();
      System.out.println("수정 완료");
    } else {
      System.out.println("존재하지 않는 번호입니다.");
    }
  }
}
