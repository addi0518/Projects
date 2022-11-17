package data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Financeboard  {
    // 전체글: 번호, 제목, 글쓴이, 작성일, 조회수, (추천)
    //제목, 내용, 작성자(현재로그인 사용자), 작성일, 수정일, 글상태 값
    public Integer post_no; // 글 번호
    public String post_title; // 글 제목
    public String post_content; //글 내용
    public String post_id; // 작성자 아이디
    public String post_name; // 작성자
    public Date post_reg_dt; // 작성일
    public Date post_mod_dt; // 수정일
    public Integer post_state; // 글 상태값 0.삭제 1.일반 2.수정

    public Financeboard(){}
    public Financeboard(Integer post_no, String post_title, String post_id, String post_name, String post_content, Date post_reg_date, Date post_mod_dt,
            Integer post_state) {
        this.post_no = post_no;
        this.post_title = post_title;
        this.post_id = post_id;
        this.post_name = post_name;
        this.post_content = post_content;
        this.post_reg_dt = post_reg_date;
        this.post_mod_dt = post_mod_dt;
        this.post_state = post_state;
    }


    @Override
    public String toString() { // 데이터 프린트
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Boolean b = post_reg_dt.equals(post_mod_dt);
        // Date dt = null;
        // if(b) dt = post_reg_dt;
        // else dt = post_mod_dt;
        // return "번호: " + post_no + " / 제목: " + post_title + " / 작성자: " + post_name + "
        // / 날짜: " + f.format(dt);

        if (b) {
            return "번호: " + post_no + " / 제목: " + post_title + " / 작성자: " + post_name + " / 날짜: "
                    + f.format(post_reg_dt);
        } else {
            return "번호: " + post_no + " / 제목: " + post_title + " / 작성자: " + post_name + " / 수정일: "
                    + f.format(post_mod_dt);
        }
    }

    public String makeDataString() { //데이터 저장
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return post_no + "," + post_title + "," + post_content + "," + post_id + "," + post_name + "," + f.format(post_reg_dt)
                + "," + f.format(post_mod_dt) + "," + post_state;
                // f.parse(post_content)
      }
}
