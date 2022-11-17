package data;

public class Member {
  public String id;
  public String pwd; //암호화된 비밀번호로 저장된다.
  public String name;
  public String regist_number; //주민번호
  public Integer member_state;

  public Member(){}

  public Member(String id, String pwd, String name, String regist_number, Integer member_state) {
    this.id = id;
    this.pwd = pwd;
    this.name = name;
    this.regist_number = regist_number;
    this.member_state = member_state; //0.탈퇴, 1.일반, 2.휴면, 3.관리자
  }

  @Override
  public String toString() {
    String type = "";
    if (member_state == 0) {
      type = "삭제";
    } else if (member_state == 1) {
      type = "일반";
    } else if (member_state == 2) {
      type = "휴면";
    } else if (member_state == 3) {
      type = "관리자";
    }
    return "아이디 : " + id + " / 이름 : " + name + " / 회원유형 : " + type;
  }

  public String makeDataString() {
    return id + "," + pwd + "," + name + "," + regist_number + "," + member_state;
  }
}
