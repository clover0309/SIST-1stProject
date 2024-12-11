package p.client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import p.vo.CarVO;
import p.vo.ChangeVO;
import p.vo.FixVO;
import p.vo.InfoVO;
import p.vo.MemberVO;
import p.vo.UseVO;

@SuppressWarnings("serial")
public class ProjectFrame extends JFrame {
	String pw = "1111";
	
    SqlSessionFactory factory;
	MemberVO mvo;
	
	JPanel login; //로그인 화면
	JPanel user_main; //유저 화면
	JPanel admin_main; //관리자 화면으로 이동 시 뜨게할 화면
	JPanel fix_main; //정비 관리 화면
	
	//로그인 화면 객체----------------------
	JPanel login_p; //하단 로그인 객체들 들어갈 패널
	JTextField id_tf; //아이디 텍스트 필드
	JPasswordField pw_tf; //패스워드 필드
	JPanel id_p, pw_p, lg_p; //아이디, 비밀번호, 로그인 버튼 넣을 패널
	JButton login_bt; //로그인 버튼

	ImageIcon ci;
	FlowLayout login_flow;
	GridLayout login_grid;
	
	//사용자 화면 객체----------------------
	JPanel north_mainp; // 상단에 들어갈 패널, 사용자 이름 띄우고, 로그아웃 버튼 넣기
	JPanel center_mainp; // 센터에 들어갈 패널, 사용자 기능으로 이동할 버튼이 들어감, girdlayout에 간격 줘서 만들 예정
	JPanel south_mainp; // 하단에 들어갈 패널, 뭐넣을지는 고민중
	
	JLabel username; //상단 패널에 유저 이름 받아서 넣기
	String uname;
	JButton logout; //유저 정보 초기화 하고 로그인 화면으로 이동
	
	JButton km; //본인 소유 차량의 km수 띄우고 누르면 km 수정다이얼로그 띄우기
	JButton carinfo; //차량이 없다면 차량 등록 다이얼로그, 차량이 있다면 차량 정보 수정 다이얼로그 띄우기
	JButton carfix; //차량 정비창으로 이동
	JButton mycars; //본인 소유 차량 이력 확인 다이얼로그 띄우기
	
	JPanel[] s_p = new JPanel[5]; //하단의 특정 부분을 클릭할 때 관리자 화면으로 넘어가도록
	GridLayout usermain_grid;
	
	//관리자 화면 객체----------------------
	JPanel north_adminp; //관리자 화면 메인 상단 패널
	
	JPanel center_adminp; //관리자 화면 메인 센터 패널
	
	//수정 내역 확인 버튼
	JButton log_info;
	//사용자,차,정비,소모품,소유주 변경 내역 확인 버튼들
	JButton member_info;
	JButton car_info;
	JButton fix_info;
	JButton use_info;
	JButton change_info;
	JButton change_bt;
	
	JPanel south_adminp; //관리자 화면 메인 하단 패널
	JButton back_bt; //관리자 화면에서 유저 화면으로 뒤로가기 버튼
	
	//정비 화면 객체----------------------
	JPanel north_fixp; //정비 화면 상단 패널
	JPanel center_fixp; //정비 화면 센터 패널
	JLabel carnum;
	String cnum;
	JButton backUser; //유저 메인 화면으로 돌아가는 버튼
	JButton his_bt, add_bt, price_bt; //정비 이력, 정비 추가, 월간정산 버튼
	
	CardLayout card; //화면 전환용 카드 레이아웃
	
	public void makeLogin() { //로그인 화면 생성
		login_grid = new GridLayout(3, 1, 0, 0);
		login_flow = new FlowLayout(FlowLayout.RIGHT);
		
		login = new JPanel(new BorderLayout());
		login_p = new JPanel(login_grid);
		
		id_p = new JPanel(login_flow);
		id_tf = new JTextField(10);
		id_p.add(new JLabel("ID : "));
		id_p.add(id_tf);
		
		pw_p = new JPanel(login_flow);
		pw_tf = new JPasswordField(10);
		pw_p.add(new JLabel("PW : "));
		pw_p.add(pw_tf);
		
		lg_p = new JPanel(login_flow);
		login_bt = new JButton("로그인");
		lg_p.add(login_bt);
		
		login_p.add(id_p);
		login_p.add(pw_p);
		login_p.add(lg_p);
		
		Image img = new ImageIcon("src/images/carfix.png").getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		ci = new ImageIcon(img);
		login.add(new JLabel(ci), BorderLayout.CENTER);
		login.add(login_p, BorderLayout.SOUTH);
		
		login_bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SqlSession ss = factory.openSession();
				
				String id = id_tf.getText().trim();
				String pw = String.valueOf(pw_tf.getPassword());
				
				int a = ss.selectOne("member.search_id", id);
				
				if(a != 0) {
					String pass = ss.selectOne("member.search_pass", id);
					if(pw.compareTo(pass)==0) {
						HashMap<String, String> map = new HashMap<>();
						map.put("id", id);
						map.put("pw", pw);
						
						mvo = ss.selectOne("member.setMember", map);
						JOptionPane.showMessageDialog(ProjectFrame.this, "로그인 성공");
						username.setText(mvo.getM_name()+"님");
						if(mvo.getC_code()==null) {
							String nowkm = "차량이 없습니다.";
							km.setText(nowkm);
						}
						else {
							String nowkm = ss.selectOne("car.search_km", mvo.getC_code());
							km.setText(nowkm+"km");
						}
						card.show(ProjectFrame.this.getContentPane(), "card2");
			    		ProjectFrame.this.setTitle("UserMain");
			    		fixcalc();
					}	
					else {
						JOptionPane.showMessageDialog(ProjectFrame.this, "ID 또는 비밀번호가 잘못되었습니다.");
					}
				}else {
					JOptionPane.showMessageDialog(ProjectFrame.this, "ID 또는 비밀번호가 잘못되었습니다.");
				}
				
				ss.close();
			}
		});
		
		card = new CardLayout();
		this.setLayout(card);
		this.getContentPane().add(login, "card1");
	}
	
	public void makeUsermain() { //로그인 후 유저 화면 생성
		user_main = new JPanel(new BorderLayout());
		
		north_mainp = new JPanel(new BorderLayout());
		username = new JLabel(uname);
		logout = new JButton("Logout");
		north_mainp.add(username, BorderLayout.WEST);
		north_mainp.add(logout, BorderLayout.EAST);
		
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mvo = null;
				id_tf.setText("");
				pw_tf.setText("");
				JOptionPane.showMessageDialog(ProjectFrame.this, "로그아웃");
				card.show(ProjectFrame.this.getContentPane(), "card1");
	    		ProjectFrame.this.setTitle("차량 관리 프로그램");
			}
		});
		
		user_main.add(north_mainp, BorderLayout.NORTH);
		
		//------------------------------------------------
		
		usermain_grid = new GridLayout(2, 2, 50, 50);
		center_mainp = new JPanel(usermain_grid);
		String user_km = "km";
		km = new JButton(user_km);
		carinfo = new JButton("차량 등록 / 수정");
		carfix = new JButton("정비 관리");
		mycars = new JButton("차량 이력");
		
		km.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mvo.getC_code()==null)
					JOptionPane.showMessageDialog(ProjectFrame.this, "차량이 없습니다.");
				else
					new KmDialog(ProjectFrame.this, mvo);
			}
		});
		
		carinfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//if차가 없다면 AddCarDialog, 없다면 EditCarDialog
				if(mvo.getC_code()==null) {
					new AddCarDialog(ProjectFrame.this, mvo);	
				}
				else {
					new EditCarDialog(ProjectFrame.this, mvo);
				}
			}
		});
		
		mycars.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MyCarDialog(ProjectFrame.this, mvo);
			}
		});
		
		carfix.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mvo.getC_code()!=null) {
					cnum = "차번호 : " + getCarVO(mvo).getC_number();
					carnum.setText(cnum);
					card.show(ProjectFrame.this.getContentPane(), "card4");
		    		ProjectFrame.this.setTitle("User 차량 정비");
				}
				else {
					JOptionPane.showMessageDialog(ProjectFrame.this, "현재 차량을 소유하고 있지 않습니다.");
				}
			}
		});
		
		
		center_mainp.add(km);
		center_mainp.add(carinfo);
		center_mainp.add(carfix);
		center_mainp.add(mycars);
		
		user_main.add(center_mainp, BorderLayout.CENTER);
		
		//------------------------------------------------
		
		south_mainp = new JPanel(new GridLayout(1, 5));
		for(int i=0; i<s_p.length;i++) {
			s_p[i] = new JPanel();
			s_p[i].setBackground(Color.RED);
			south_mainp.add(s_p[i]);
		}
		
		s_p[3].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
					String password = JOptionPane.showInputDialog("");
					if(password!=null&&password.equals(pw)) {
						card.show(ProjectFrame.this.getContentPane(), "card3");
			    		ProjectFrame.this.setTitle("관리자 페이지");
					}
					else {
						JOptionPane.showMessageDialog(ProjectFrame.this, "비밀번호가 틀렸습니다.");
					}
				}
			}
		
		});
		
		user_main.add(south_mainp, BorderLayout.SOUTH);
		this.getContentPane().add(user_main, "card2");
	}
	
	public void makeAdminmain() { //관리자 화면 생성
		admin_main = new JPanel(new BorderLayout());
		mvo = new MemberVO();
		
		
		center_adminp = new JPanel(new GridLayout(2, 3, 10, 10));

		member_info = new JButton("사용자 정보");
		car_info = new JButton("차량 정보");
		fix_info = new JButton("정비 내역");
		center_adminp.add(member_info);
		center_adminp.add(car_info);
		center_adminp.add(fix_info);
		
		use_info = new JButton("소모품 정보");
		change_info = new JButton("소유주 변경 내역");
		change_bt = new JButton("차량 소유주 변경");
		center_adminp.add(use_info);
		center_adminp.add(change_info);
		center_adminp.add(change_bt);
		
		south_adminp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		log_info = new JButton("수정 내역");
		back_bt = new JButton("뒤로");
		south_adminp.add(log_info);
		south_adminp.add(back_bt);
		
		member_info.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MemberInfoDialog(ProjectFrame.this);
			}
		});
		
		car_info.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CarInfoDialog(ProjectFrame.this);
			}
		});
		
		fix_info.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new FixInfoDialog(ProjectFrame.this);
			}
		});
		
		use_info.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new UseInfoDialog(ProjectFrame.this);
			}
		});
		
		change_info.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ChangeInfoDialog(ProjectFrame.this);
			}
		});
		
		log_info.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new LogInfoDialog(ProjectFrame.this);
			}
		});
		
		change_bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ChangeDialog(ProjectFrame.this);
			}
		});
		
		back_bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				card.show(ProjectFrame.this.getContentPane(), "card2");
	    		ProjectFrame.this.setTitle("UserMain");
			}
		});

		north_adminp = new JPanel();
		admin_main.add(north_adminp, BorderLayout.CENTER);
		admin_main.add(center_adminp, BorderLayout.NORTH);
		admin_main.add(south_adminp, BorderLayout.SOUTH);
		
		this.getContentPane().add(admin_main, "card3");
	}
	
	public void makeFixmain() { //정비 화면 생성
		fix_main = new JPanel(new BorderLayout());
		north_fixp = new JPanel(new BorderLayout());
		carnum = new JLabel(cnum);
		backUser = new JButton("뒤로");
		north_fixp.add(carnum, BorderLayout.WEST);
		north_fixp.add(backUser, BorderLayout.EAST);
		fix_main.add(north_fixp, BorderLayout.NORTH);
		
		center_fixp = new JPanel(new FlowLayout(FlowLayout.CENTER));
		center_fixp.add(his_bt = new JButton("정비 이력"));
		center_fixp.add(add_bt = new JButton("정비 추가"));
		center_fixp.add(price_bt = new JButton("월간 정산"));
		fix_main.add(center_fixp, BorderLayout.CENTER);
		
		backUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				card.show(ProjectFrame.this.getContentPane(), "card2");
	    		ProjectFrame.this.setTitle("UserMain");
			}
		});
		his_bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MemberFixDialog(ProjectFrame.this, mvo);
			}
		});
		add_bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddFixDialog(ProjectFrame.this, mvo);
			}
		});
		price_bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MonthlyDialog(ProjectFrame.this, mvo);
			}
		});
		
		this.getContentPane().add(fix_main, "card4");
	}
	
	public ProjectFrame() {
		dbConnect();
		
		makeLogin();
		
		makeUsermain();
		
		makeAdminmain();
		
		makeFixmain();
		
		this.setBounds(300, 50, 400, 600);
		this.setVisible(true);

		this.setTitle("차량 관리 프로그램");
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}	
		});
	}
	
	private void dbConnect() {
    	try {
    		Reader r = Resources.getResourceAsReader("p/config/config.xml");
    		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
    		factory = builder.build(r);
    		r.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	//현재 로그인 한 멤버의 차량 번호를 기준으로 키로수 추가
	public void addKm(String additionalKmText) {
		SqlSession ss = factory.openSession();
		if (additionalKmText.isEmpty()) {
			JOptionPane.showMessageDialog(ProjectFrame.this, "추가 키로수를 입력하세요!");
			return;
		}
		int additionalKm = Integer.parseInt(additionalKmText);
		String cCode = mvo.getC_code();
		int rowsAffected = ss.update("car.update_km", java.util.Map.of("c_code", cCode, "new_km", additionalKm));
		if (rowsAffected > 0) {
			ss.commit(); // 업데이트 성공 시 커밋
			String newkm = ss.selectOne("car.search_km", mvo.getC_code());
			km.setText(newkm + "km"); // UI 갱신
		} else {
			ss.rollback();
        }
		ss.close();
    }
	
	//로그인 한 유저의 차량 정보 반환
	public CarVO getCarVO(MemberVO mvo) {
		CarVO cvo = null;
		SqlSession ss = factory.openSession();
		String cCode = mvo.getC_code();
		
		cvo = ss.selectOne("car.getCar", cCode);

		ss.close();
		return cvo;
	}
	
	//차량 변경 이력 list 반환 함수
	public List<ChangeVO> getC_List(MemberVO mvo) {
		SqlSession ss = factory.openSession();
		List<ChangeVO> list;
		list = ss.selectList("change.selectChangesByMember", mvo.getM_code());
		ss.close();
		return list;
	}
	
	public void addCar(MemberVO mvo2, CarVO cvo) { //로그인 한 유저가 차량이 없을 경우 차량을 추가하는 함수
		SqlSession ss = factory.openSession();
		int cnt = ss.insert("car.addCar", cvo);
		if (cnt > 0) {
			ss.commit(); // 업데이트 성공 시 커밋
			String cCode = ss.selectOne("getC_code", cvo.getC_number());
			int cnt2 = ss.insert("car.addCarToMember", java.util.Map.of("c_code", cCode, "m_code", mvo2.getM_code()));
			if (cnt2 > 0) {
				ss.commit(); // 업데이트 성공 시 커밋
				String nowkm = ss.selectOne("car.search_km", cCode);
				km.setText(nowkm+"km");
				mvo = ss.selectOne("member.resetMember", mvo.getM_code());
				int cnt3 = ss.insert("change.addChange3", 
						java.util.Map.of("c_code", cCode, "m2_code", mvo.getM_code()));
				if(cnt3>0) {
					ss.commit();
					JOptionPane.showMessageDialog(this, "저장완료");
				} else {
					ss.rollback();
					JOptionPane.showMessageDialog(this, "저장실패");
				}
				
			} else {
				ss.rollback();
				JOptionPane.showMessageDialog(this, "저장실패");
	        }
			
		} else {
			ss.rollback();
        }
		ss.close();
	}
	
	public void editCar(CarVO cvo) { //로그인 한 유저가 차량이 있을 경우 차량 정보를 변경하는 함수
		SqlSession ss = factory.openSession();
		int cnt = ss.update("car.updateCar", cvo);
		if (cnt > 0) {
			ss.commit(); // 업데이트 성공 시 커밋
			JOptionPane.showMessageDialog(this, "저장완료");
			String cCode = ss.selectOne("getC_code", cvo.getC_number());
			String nowkm = ss.selectOne("car.search_km", cCode);
			km.setText(nowkm+"km");
			
		} else {
			ss.rollback();
			JOptionPane.showMessageDialog(this, "저장실패");
        }
		ss.close();
	}
	
	public void editUser(MemberVO mvo) { //관리자 화면 유저의 정보를 변경하는 함수
		SqlSession ss = factory.openSession();
		int cnt = ss.update("member.editMember", mvo);
		if (cnt > 0) {
			ss.commit(); // 업데이트 성공 시 커밋
			JOptionPane.showMessageDialog(this, "저장완료");
			
		} else {
			ss.rollback();
			JOptionPane.showMessageDialog(this, "저장실패");
        }
		ss.close();
	}
	
	public void editFix(FixVO fvo) { //관리자 화면 정비 정보를 변경하는 함부
		SqlSession ss = factory.openSession();
		int cnt = ss.update("fix.editFix", fvo);
		if (cnt > 0) {
			ss.commit(); // 업데이트 성공 시 커밋
			JOptionPane.showMessageDialog(this, "저장완료");
			
		} else {
			ss.rollback();
			JOptionPane.showMessageDialog(this, "저장실패");
        }
		ss.close();
	}
	
	public void editUse(UseVO uvo) { //관리자 화면 소모품 목록을 변경하는 함수
		SqlSession ss = factory.openSession();
		int cnt = ss.update("use.editUse", uvo);
		if (cnt > 0) {
			ss.commit(); // 업데이트 성공 시 커밋
			JOptionPane.showMessageDialog(this, "저장완료");
			
		} else {
			ss.rollback();
			JOptionPane.showMessageDialog(this, "저장실패");
        }
		ss.close();
	}
	
	public void editChange(ChangeVO chvo) { //관리자 화면 소모품 목록을 변경하는 함수
		SqlSession ss = factory.openSession();
		int cnt = ss.update("change.editChange", chvo);
		if (cnt > 0) {
			ss.commit(); // 업데이트 성공 시 커밋
			
			JOptionPane.showMessageDialog(this, "저장완료");
			
		} else {
			ss.rollback();
			JOptionPane.showMessageDialog(this, "저장실패");
        }
		ss.close();
	}
	
	public void savefix(FixVO fvo) { //로그인 한 유저가 새로운 정비 목록을 작성하면 db에 저장하는 함수
		SqlSession ss = factory.openSession();
		int cnt = ss.insert("fix.addFix", fvo);
		if (cnt > 0) {
			ss.commit(); // 업데이트 성공 시 커밋
			if(getCarVO(mvo).getC_ok().contains("1")&&fvo.getFix_type().contains("1")) {
				int cnt2 = ss.update("car.updateOk", fvo.getC_code());
				if(cnt2>0) {
					ss.commit();
					JOptionPane.showMessageDialog(this, "저장완료");
				}
				else {
					ss.rollback();
					JOptionPane.showMessageDialog(this, "저장실패");
				}
			}
		} else {
			ss.rollback();
			JOptionPane.showMessageDialog(this, "저장실패");
        }
		ss.close();
	}
	
	public List<CarVO> getCarList(){ //전체 차량 목록 list 반환 함수
		SqlSession ss = factory.openSession();
		List<CarVO> list = ss.selectList("car.showCar");
		ss.close();
		return list;
	}
	
	public List<MemberVO> getMemberList(){ //전체 member 목록 list 반환 함수
		SqlSession ss = factory.openSession();
		List<MemberVO> list = ss.selectList("member.showMember");
		ss.close();
		return list;
	}
	
	public List<FixVO> getFixList(){ //전체 정비 목록 list 반환 함수
		SqlSession ss = factory.openSession();
		List<FixVO> list = ss.selectList("fix.showFix");
		ss.close();
		return list;
	}
	
	public List<FixVO> getCarFixList(CarVO cvo){ //전체 정비 목록 list 반환 함수
		SqlSession ss = factory.openSession();
		List<FixVO> list = ss.selectList("fix.showCarFix", cvo.getC_code());
		ss.close();
		return list;
	}
	
	
	public List<UseVO> getUseList(){ //전체 소모품 목록 list 반환 함수
		SqlSession ss = factory.openSession();
		List<UseVO> list = ss.selectList("use.showUse");
		ss.close();
		return list;
	}
	
	public List<ChangeVO> getChangeList(){ //전체 차량 변경 목록 list 반환 함수
		SqlSession ss = factory.openSession();
		List<ChangeVO> list = ss.selectList("change.showChange");
		ss.close();
		return list;
	}
	
	public List<ChangeVO> getCarChangeList(CarVO cvo){ //전체 차량 변경 목록 list 반환 함수
		SqlSession ss = factory.openSession();
		List<ChangeVO> list = ss.selectList("change.showCarChange", cvo.getC_code());
		ss.close();
		return list;
	}
	
	public List<FixVO> memberFix(MemberVO mvo){ //사용자의 정비 목록 list 반환 함수
		SqlSession ss = factory.openSession();
		List<FixVO> list = ss.selectList("fix.showMemberFix", mvo.getM_code());
		ss.close();
		return list;
	}
	
	public String getName(String code) {
		SqlSession ss = factory.openSession();
		String name = ss.selectOne("member.getName", code);
		ss.close();
		return name;
	}
	
	public String getC_num(String code) {
		SqlSession ss = factory.openSession();
		String num = ss.selectOne("car.getNum", code);
		ss.close();
		return num;
	}
	
	
	public List<FixVO> getMonthly(MemberVO mvo) { //이번달 사용자의 정비 목록을 반환
		SqlSession ss = factory.openSession();
		List<FixVO> list = ss.selectList("fix.getMonthly", mvo.getM_code());
		ss.close();		
		return list;
	}
	
	public List<MemberVO> searchMemberList(String str){ //전체 member 목록 list 반환 함수
		SqlSession ss = factory.openSession();
		List<MemberVO> list = ss.selectList("member.searchBy", str);
		ss.close();
		return list;
	}
	
	public List<InfoVO> getInfoList(){
		SqlSession ss = factory.openSession();
		List<InfoVO> list = ss.selectList("info.total");
		ss.close();
		return list;
	}
	
	public void addInfo(InfoVO ivo) {
		SqlSession ss = factory.openSession();
		int cnt = ss.insert("info.addInfo", ivo);
		if (cnt > 0) {
			ss.commit(); // 업데이트 성공 시 커밋
			JOptionPane.showMessageDialog(this, "저장완료");
		} else {
			ss.rollback();
			JOptionPane.showMessageDialog(this, "저장실패");
        }
		ss.close();
	}
	
	public int[] getMonthUseCount(MemberVO mvo) {
		SqlSession ss = factory.openSession();
		int count1 = ss.selectOne("fix.getCount1", mvo.getM_code());
		int count2 = ss.selectOne("fix.getCount2", mvo.getM_code());
		int count3 = ss.selectOne("fix.getCount3", mvo.getM_code());
		int count4 = ss.selectOne("fix.getCount4", mvo.getM_code());
		int[] counts = {count1, count2, count3, count4};
		ss.close();
		return counts;
	}
	
	public String getMonth() {
		SqlSession ss = factory.openSession();
		String month = ss.selectOne("fix.getMonth");
		ss.close();
		return month;
	}
	
	public int[] getUsePrice() {
		int[] price = new int[4];
		SqlSession ss = factory.openSession();
		List<UseVO> list = ss.selectList("use.showUse");
		ss.close();
		int i=0;
		for(UseVO vo : list) {
			price[i] =  (int)Double.parseDouble(vo.getPrice());
			i++;
		}
		return price;
	}
	
	public void changeCar(MemberVO mvo1, MemberVO mvo2) {
		SqlSession ss = factory.openSession();
		//mvo1의 차량을 mvo2에 전달
		int cnt = ss.update("member.addTo",java.util.Map.of("c_code", mvo1.getC_code(), "m_code", mvo2.getM_code())); 
		if (cnt > 0) {
			ss.commit(); // 업데이트 성공 시 커밋
			//전달이 성공했다면 mvo1에게서 차량 삭제
			int cnt2 = ss.update("member.deleteCar", mvo1.getM_code());
			
			if (cnt2 > 0) {
				ss.commit(); // 업데이트 성공 시 커밋
				//차량 변경 이력을 change_t에 추가
				int cnt3 = ss.insert("change.addChange1", java.util.Map.of("m1_code", mvo1.getM_code(), "m2_code", mvo2.getM_code(), "c_code", mvo1.getC_code()));
				if (cnt3 > 0) {
					ss.commit(); // 업데이트 성공 시 커밋
					JOptionPane.showMessageDialog(this, "저장완료");
					mvo = ss.selectOne("member.resetMember",mvo.getM_code());
				} else {
					ss.rollback();
					JOptionPane.showMessageDialog(this, "저장실패");
		        }
			} else {
				ss.rollback();
				JOptionPane.showMessageDialog(this, "저장실패");
	        }
			
		} else {
			JOptionPane.showMessageDialog(this, "저장실패");
			ss.rollback();
        }
		ss.close();
	}
	
	public void deleteCar(MemberVO mvo1) {
		SqlSession ss = factory.openSession();
		int cnt = ss.update("member.deleteCar", mvo1.getM_code());
		if(cnt>0) {
			ss.commit();
			int cnt2 = ss.insert("change.addChange2", java.util.Map.of("m1_code", mvo1.getM_code(), "c_code", mvo1.getC_code()));
			if(cnt2>0) {
				ss.commit();
				JOptionPane.showMessageDialog(this, "저장완료");
				mvo = ss.selectOne("member.resetMember",mvo.getM_code());
			}
			else {
				ss.rollback();
				JOptionPane.showMessageDialog(this, "저장실패");
			}
		}
		else {
			ss.rollback();
			JOptionPane.showMessageDialog(this, "저장실패");
		}
		ss.close();
	}
	
	public int isFix(MemberVO mvo) {
		SqlSession ss = factory.openSession();
		int ok = ss.selectOne("fix.checkFix", mvo.getC_code());
		ss.close();
		return ok;
	}
	
	public void fixcalc() {
		SqlSession ss = factory.openSession();
		try {
			String str = ss.selectOne("fix.find_car", mvo.getM_code());
			if(str==null||mvo.getC_code()==null||!mvo.getC_code().equals(str)) {
				return;
			}
			Integer fixdate_calc = ss.selectOne("fix.fixdate_calc", mvo.getM_code());
			if(fixdate_calc == null) {
				return;
			}
			if(fixdate_calc.intValue() == 1) {
				int response = JOptionPane.showConfirmDialog(ProjectFrame.this,
						"차량 정기검사 기간이 얼마 남지않았습니다. 정비화면으로 이동하시겠습니까?", "정기검사 알림",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE	);

				if(response == JOptionPane.YES_OPTION) {
					CardLayout card = (CardLayout) getContentPane().getLayout();
					card.show(getContentPane(), "card4");
					new AddFixDialog(ProjectFrame.this, mvo);
				}
			}
		} finally {
			ss.close();
		}
	}
	
	public static void main(String[] args) {
		new ProjectFrame();
	}

}
