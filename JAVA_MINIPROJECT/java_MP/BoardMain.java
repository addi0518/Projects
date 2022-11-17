import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import data.FinanceComment;
import data.Financeboard;
import data.Member;
import service.BoardService;
import service.CommentService;
import service.MemberService;

public class BoardMain {
    public static Scanner sc = new Scanner(System.in);

    public static void initializeMembers() {
        // 멤버 데이터 파일 읽어오기
        try {
            BufferedReader r = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File("data_files/member.dat")), "UTF-8"));
            while (true) {
                String line = r.readLine();
                if (line == null)
                    break;
                String[] split = line.split(",");
                Member m = new Member();
                m.id = split[0];
                m.pwd = split[1];
                m.name = split[2];
                m.regist_number = split[3];
                m.member_state = Integer.parseInt(split[4]);
                MemberService.member_List.add(m);
            }
            r.close();
        } catch (Exception e) {
        }
    }

    public static void initializeBoard() {
        // 게시판 데이터 파일 읽어오기
        try {
            BufferedReader r = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File("data_files/board.dat")), "UTF-8"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (true) {
                String line = r.readLine();
                if (line == null)
                    break;
                String[] split = line.split(",");
                Financeboard fb = new Financeboard();
                fb.post_no = Integer.parseInt(split[0]);
                fb.post_title = split[1];
                fb.post_content = split[2];
                fb.post_id = split[3];
                fb.post_name = split[4];
                fb.post_reg_dt = formatter.parse(split[5]);
                fb.post_mod_dt = formatter.parse(split[6]);
                fb.post_state = Integer.parseInt(split[7]);
                BoardService.financeboard_List.add(fb);
            }
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initializeComment() {
        // 댓글 데이터 파일 읽어오기
        try {
            BufferedReader r = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File("data_files/comment.dat")), "UTF-8"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (true) {
                String line = r.readLine();
                if (line == null)
                    break;
                String[] split = line.split(",");
                FinanceComment fc = new FinanceComment();
                fc.comment_no = Integer.parseInt(split[0]);
                fc.fboard_no = Integer.parseInt(split[1]);
                fc.comment_name = split[2];
                fc.comment_content = split[3];
                fc.comment_id = split[4];
                fc.comment_reg_dt = formatter.parse(split[5]);
                fc.comment_mod_dt = formatter.parse(split[6]);
                fc.comment_state = Integer.parseInt(split[7]);
                CommentService.financecomment_List.add(fc);
            }
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        initializeMembers();
        initializeBoard();
        initializeComment();
        try {
            while (true) {
                System.out.println("0.종료");
                System.out.println("1.금융 게시판");
                System.out.println("97.로그인");
                System.out.println("98.회원가입");
                System.out.print("번호: ");
                Integer sel = sc.nextInt();
                sc.nextLine();
                if (sel == 0) {
                    System.out.println("종료");
                    return;
                } else if (sel == 1) {
                    System.out.println("=====금융 게시판=====");
                    while (true) {
                        Boolean exist = BoardService.showFinaceboardList();
                        if (!exist) {
                            break;
                        } else if (exist) {
                            System.out.print("게시글 번호(b.뒤로): "); // 게시글 내용 보기
                            try {
                                String b_no = sc.nextLine();
                                if (b_no.equals("b")) {
                                    break;
                                }
                                Integer sel_num = Integer.parseInt(b_no);
                                BoardService.readFinanceboard(sel_num);
                                CommentService.showFinanceComment(sel_num);
                            } catch (Exception e) {
                                System.out.println("잘못된 입력값 입니다.");
                            }
                        }
                    }
                } else if (sel == 97) { // 로그인
                    System.out.print("아이디: ");
                    String tmp_id = sc.nextLine();
                    System.out.print("비밀번호: ");
                    String tmp_pwd = sc.nextLine();
                    Member m = MemberService.logIn(tmp_id, tmp_pwd);
                    if (m == null) { // 다시 처음 메뉴로 돌아감
                        continue;
                    }
                    while (true) {
                        if (m != null) {
                            System.out.println("0.종료");
                            System.out.println("1.금융 게시판");
                            System.out.println("2.내 게시글 보기");
                            System.out.println("3.내 댓글 보기");
                            System.out.println("97.로그아웃");
                            System.out.println("98.회원수정");
                            System.out.println("99.회원탈퇴");
                            System.out.print("번호: ");
                            sel = sc.nextInt();
                            if (sel == 1) { // 금융 게시판
                                Boolean exist = BoardService.showFinaceboardList();
                                sc.nextLine();
                                System.out.print("내용 보기(숫자) / 글 쓰기(w): ");
                                String w = sc.nextLine();
                                try {
                                    if (w.equalsIgnoreCase("w")) { // 글쓰기
                                        BoardService.addFinaceboardList(m);
                                    } else if (exist) { // 게시글이 있으면 내용 보기
                                        Integer sel_num = Integer.parseInt(w);
                                        BoardService.readFinanceboard(sel_num);
                                        CommentService.showFinanceComment(sel_num);
                                        System.out.print("댓글 쓰기(w) 취소(아무키): ");
                                        String cw = sc.nextLine();
                                        if (cw.equalsIgnoreCase("w")) {
                                            CommentService.addFinanceCommentBoard(sel_num, m);
                                        } else {
                                            System.out.println("댓글 쓰기 취소");
                                            continue;
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("잘못된 입력값 입니다.");
                                }                           
                            } else if (sel == 2) { // 내 게시글 보기
                                sc.nextLine();
                                Boolean exist = BoardService.showMemberFinanceboard(m);
                                if (exist) {
                                    System.out.print("0.삭제, 1.뒤로, 2.수정: "); // 삭제,수정 여부
                                    try {
                                        Integer yn = sc.nextInt();
                                        if (yn == 0) {
                                            BoardService.deleteMemberFinanceboard(m);
                                        } else if (yn == 1) {
                                            System.out.println("돌아갑니다.");
                                        } else if (yn == 2) {
                                            BoardService.modifyMemberFinanceboard(m);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("잘못된 번호 입력");
                                    }
                                }
                            } else if (sel == 3) { // 내 댓글 보기
                                Boolean exist = CommentService.showMemberFinanceComment(m);
                                if (exist) {
                                    System.out.print("0.삭제, 1.뒤로, 2.수정: "); // 삭제 여부
                                    try {
                                        Integer yn = sc.nextInt();
                                        if (yn == 0) {
                                            CommentService.deleteMemberFinanceComment(m);
                                        } else if (yn == 1) {
                                            System.out.println("돌아갑니다.");
                                        } else if (yn == 2) {
                                            CommentService.modifyMemberComment(m);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("잘못된 번호 입력");
                                    }
                                }
                            } else if (sel == 97) { // 로그아웃
                                MemberService.logOut(m);
                                break;
                            } else if (sel == 98) { // 회원수정
                                MemberService.memberModi(m);
                                break;
                            } else if (sel == 99) { // 회원탈퇴
                                MemberService.memberLeave(m);
                                break;
                            } else if (sel == 0) {
                                System.out.println("종료");
                                return;
                            }
                        }
                    }
                } else if (sel == 98) { // 회원 가입
                    MemberService.memberJoin();
                } else {
                    System.out.println("잘못된 번호 입력");
                }
            }
        
        } catch (Exception e) {
            System.out.println("잘못된 입력 입니다.");
        }
    }
}
