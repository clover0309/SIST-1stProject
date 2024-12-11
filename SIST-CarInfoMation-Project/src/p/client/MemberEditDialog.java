package p.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import p.vo.InfoVO;
import p.vo.MemberVO;

@SuppressWarnings("serial")
public class MemberEditDialog extends JDialog {
	MemberInfoDialog parent;
	MemberVO mvo;
	ProjectFrame parent_p;
	
	JTextField mc_tf,//m_code 
	mn_tf, //m_name 
	mp_tf, //m_password
	cc_tf; // c_code
	JButton edit_bt, cancel_bt;
	
	JTextArea ta;
	
	JPanel p1, p2, p3, p4, p5, n_p, c_p;
	public MemberEditDialog(MemberInfoDialog parent, MemberVO mvo) {
		this.parent = parent;
		this.mvo = mvo;
		ProjectFrame parent_p = parent.parent;
		this.setTitle(mvo.getM_name() + "님 사용자정보");
		n_p = new JPanel();
		n_p.setLayout(new GridLayout(4,1));

		p1 = new JPanel(new BorderLayout());
		p1.add(new JLabel("회원번호"), BorderLayout.WEST);
		mc_tf = new JTextField(8);
		mc_tf.setText(mvo.getM_code());
		mc_tf.setEditable(false);
		p1.add(mc_tf, BorderLayout.CENTER);
		n_p.add(p1);
		
		p2 = new JPanel(new BorderLayout());
		p2.add(new JLabel("이름"), BorderLayout.WEST);
		mn_tf = new JTextField(8);
		mn_tf.setText(mvo.getM_name());
		p2.add(mn_tf, BorderLayout.CENTER);
		n_p.add(p2);
		
		p3 = new JPanel(new BorderLayout());
		p3.add(new JLabel("비밀번호"), BorderLayout.WEST);
		mp_tf = new JTextField(8);
		mp_tf.setText(mvo.getM_password());
		p3.add(mp_tf, BorderLayout.CENTER);
		n_p.add(p3);
		
		p4 = new JPanel(new BorderLayout());
		p4.add(new JLabel("차량등록순번"), BorderLayout.WEST);
		cc_tf = new JTextField(8);
		cc_tf.setText(mvo.getC_code());
		if(mvo.getC_code()!=null)
			cc_tf.setEditable(false);
		p4.add(cc_tf, BorderLayout.CENTER);
		n_p.add(p4);
		
		this.add(n_p, BorderLayout.NORTH);
		
		c_p = new JPanel(new BorderLayout());
		ta = new JTextArea();
		JPanel nc_p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nc_p.add(new JLabel("수정 내용"));
		c_p.add(nc_p, BorderLayout.NORTH);
		c_p.add(new JScrollPane(ta), BorderLayout.CENTER);
		Dimension minSize = new Dimension(200, 100);
		ta.setMinimumSize(minSize);
		ta.setPreferredSize(minSize);
		this.add(c_p, BorderLayout.CENTER);
		
		p5 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p5.add(edit_bt = new JButton("수정"));
		p5.add(cancel_bt = new JButton("취소"));
		this.add(p5, BorderLayout.SOUTH);
		
		//창 크기 설정 및 창표시 설정
		this.setBounds(parent.getX()+50, parent.getY()+50, 200, 250);
		pack();
		this.setVisible(true);

		this.setTitle("회원정보 수정");
		
		edit_bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//공백 값을 제거하기 위해, trim을 사용해 공백 값을
				//전부 제거함. (해당영역은 공백을 허용하지않음.)
				String m_code = mc_tf.getText().trim();
				String m_name = mn_tf.getText().trim();
				String m_password = mp_tf.getText().trim();
				String c_code = cc_tf.getText().trim();
				
				//Member_VO를 사용하기위해 vo를 선언하고 초기화
				MemberVO vo = new MemberVO();
				
				//회원번호 부터 차량등록번호까지 조건문을 사용하여
				//텍스트필드에 적힌 값을 전부 vo값에 설정해준다.
				if(m_code.length() > 0)
				vo.setM_code(m_code);
				if(m_name.length() > 0)
				vo.setM_name(m_name);
				if(m_password.length() > 0)
				vo.setM_password(m_password);
				if(c_code.length() > 0)
				vo.setC_code(c_code);
				
				parent_p.editUser(vo);
				parent.viewUser();
				
				InfoVO ivo = new InfoVO();
				ivo.setI_text(ta.getText());
				ivo.setI_type("0");
				parent_p.addInfo(ivo);
				dispose();
			}
		});
		
		cancel_bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		
	}
}
