package p.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import p.vo.CarVO;
import p.vo.InfoVO;
import p.vo.MemberVO;

@SuppressWarnings("serial")
public class AddCarDialog extends JDialog {
	//사용자의 차가 없을 때 새로운 차를 넣는 Dialog
	ProjectFrame parent;
	MemberVO mvo;
	
	JPanel left_p;
	GridLayout l_g;
	
	JLabel cnum;
	JLabel ckm;
	JLabel cfix;
	JLabel ctype;
	JLabel cname;
	
	//-------------------------
	
	JPanel right_p;
	GridLayout r_g;
	
	JTextField cnum_tf;
	JTextField ckm_tf;
	JComboBox<String> cfix_cb;
	String[] fix = {"O", "X"};
	JComboBox<String> ctype_cb;
	String[] type = {"승용", "화물"};
	JTextField cname_tf;
	
	//------------------------
	
	JPanel bt_p; // 버튼 넣을 패널
	JButton add, cancel; //추가, 취소 버튼

	//------------------------
	JPanel main_p;
	Font label;

	
	
	public AddCarDialog(ProjectFrame parent, MemberVO mvo) {
		this.parent = parent;
		this.mvo = mvo;
		
		setUndecorated(true);

		main_p = new JPanel();
		this.add(main_p, BorderLayout.CENTER);
		
		label = new Font("맑은 고딕", Font.PLAIN, 14);
		
		left_p = new JPanel(l_g = new GridLayout(5, 1, 20, 20));
		cnum = new JLabel("차량 번호 :");
		cnum.setFont(label);
		ckm = new JLabel("키로수 :");
		ckm.setFont(label);
		cfix = new JLabel("정기 정비여부 :");
		cfix.setFont(label);
		ctype = new JLabel("차종 :");
		ctype.setFont(label);
		cname = new JLabel("차이름 :");
		cname.setFont(label);
		
		left_p.add(cnum);
		left_p.add(ckm);
		left_p.add(cfix);
		left_p.add(ctype);
		left_p.add(cname);
		
		main_p.add(left_p, BorderLayout.WEST);
		
		
		right_p = new JPanel(r_g = new GridLayout(5, 1, 20, 15));
		cnum_tf = new JTextField(8);
		ckm_tf = new JTextField(8);
		cfix_cb = new JComboBox<>(fix);
		cfix_cb.setSelectedIndex(0);
		ctype_cb = new JComboBox<>(type);
		cfix_cb.setSelectedIndex(0);
		cname_tf = new JTextField(15);
		right_p.add(cnum_tf);
		right_p.add(ckm_tf);
		right_p.add(cfix_cb);
		right_p.add(ctype_cb);
		right_p.add(cname_tf);
		
		main_p.add(right_p, BorderLayout.CENTER);
		
		bt_p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add = new JButton("추가");
		cancel = new JButton("취소");
		bt_p.add(add);
		bt_p.add(cancel);
		
		this.add(bt_p, BorderLayout.SOUTH);

		TitledBorder title = BorderFactory.createTitledBorder("새로운 차량 등록");
        title.setTitleFont(new Font("맑은 고딕", Font.BOLD, 18)); // 굵은 글씨로 설정     
        title.setTitleJustification(TitledBorder.CENTER);
        main_p.setBorder(title);

        // EmptyBorder로 바깥쪽 여백 설정
        main_p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 20, 10, 20), // 상, 좌, 하, 우 바깥쪽 여백
            title // 제목 테두리
        ));
		
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CarVO cvo = new CarVO();
				cvo.setC_number(cnum_tf.getText());
				cvo.setC_km(ckm_tf.getText());
				String ok = "0";
				if(cfix_cb.getSelectedIndex()==0) {
					ok = "0";
				}
				else if(cfix_cb.getSelectedIndex()==1) {
					ok = "1";
				}
				cvo.setC_ok(ok);
				String ok2 = "0";
				if(ctype_cb.getSelectedIndex()==0) {
					ok2 = "0";
				}
				else if(ctype_cb.getSelectedIndex()==1) {
					ok2 = "1";
				}
				cvo.setC_type(ok2);
				cvo.setC_name(cname_tf.getText());
				parent.addCar(mvo, cvo);
				
				InfoVO ivo = new InfoVO();
				ivo.setI_text("회원 차량 추가");
				ivo.setI_type("1");
				ivo.setM_code(mvo.getM_code());
				parent.addInfo(ivo);
				
				dispose();
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		this.setBounds(parent.getX()+100, parent.getY()+100, 400, 300);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});
	}
}
