package data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FinanceComment {
  public Integer fboard_no; //게시글번호
  public Integer comment_no; //댓글 번호
  public String comment_name; //댓글 작성자
  public String comment_content; //댓글 내용
  public String comment_id; //댓글 작성자 아이디
  public Date comment_reg_dt; //작성일
  public Date comment_mod_dt; //수정일
  public Integer comment_state; //댓글 상태 0.삭제 1.일반 2.수정

  
  public FinanceComment(){}
  public FinanceComment(Integer fboard_no, Integer comment_no, String comment_name, String comment_id, String comment_content, Date comment_reg_dt, Date comment_mod_dt, Integer comment_state){
    this.fboard_no = fboard_no;
    this.comment_no = comment_no;
    this.comment_name = comment_name;
    this.comment_id = comment_id;
    this.comment_content = comment_content;
    this.comment_reg_dt = comment_reg_dt;
    this.comment_mod_dt = comment_mod_dt;
    this.comment_state = comment_state;
  }

  @Override
  public String toString() {
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Boolean b = comment_reg_dt.equals(comment_mod_dt);
    if(b){
      return "번호: "+comment_no+" / 내용: "+comment_content+" / 작성자: "+comment_name+" / 작성일: "+f.format(comment_reg_dt);
    }else{
      return "번호: "+comment_no+" / 내용: "+comment_content+" / 작성자: "+comment_name+" / 수정일: "+f.format(comment_mod_dt);
    }
  }

  public String makeDataString() {
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return comment_no + "," + fboard_no + "," + comment_name + "," + comment_content + "," + comment_id + ","
        + f.format(comment_mod_dt) + "," + f.format(comment_mod_dt) + "," + comment_state;
  }
}
