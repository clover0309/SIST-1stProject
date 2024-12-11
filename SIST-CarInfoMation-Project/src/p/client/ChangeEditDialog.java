package p.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import p.vo.ChangeVO;
import p.vo.InfoVO;

@SuppressWarnings("serial")
public class ChangeEditDialog extends JDialog {

	ChangeInfoDialog parent;
	ProjectFrame parent_p;
	ChangeVO chvo;
	
	
	JTextField car_tf, m1_tf, m2_tf, date_tf;
	JTextField car_num, m1_name, m2_name;
	JPanel p1, p2, p3, p4, n_p, s_p, c_p;
	
	JButton edit_bt, cancel_bt;
	JTextArea ta;
	
	public ChangeEditDialog(ChangeInfoDialog parent, ChangeVO chvo) {
		this.parent = parent;
		ProjectFrame parent_p = parent.parent;
		this.chvo = chvo;
		
		n_p = new JPanel(new GridLayout(4,1));
		
		p1 = new JPanel(new BorderLayout());
		p1.add(new JLabel("차량번호"), BorderLayout.WEST);
		car_num = new JTextField();
		car_num.setText(parent_p.getC_num(chvo.getC_code()));
		car_num.setEditable(false);
		p1.add(car_num, BorderLayout.CENTER);
		car_tf = new JTextField();
		car_tf.setText(chvo.getC_code());
		p1.add(car_tf, BorderLayout.EAST);
		n_p.add(p1);
		m1_tf = new JTextField();
		p2 = new JPanel(new BorderLayout());
		p2.add(new JLabel("전소유주"), BorderLayout.WEST);
		if(chvo.getM1_code()==null) {
			m1_tf.setText("등록");
			p2.add(m1_tf, BorderLayout.CENTER);
		}
		else {
			m1_name = new JTextField();
			m1_name.setText(parent_p.getName(chvo.getM1_code()));
			p2.add(m1_name, BorderLayout.CENTER);
			m1_name.setEditable(false);
			m1_tf = new JTextField();
			m1_tf.setText(chvo.getM1_code());
			p2.add(m1_tf, BorderLayout.EAST);
		}
		n_p.add(p2);
		
		p3 = new JPanel(new BorderLayout());
		p3.add(new JLabel("현소유주"), BorderLayout.WEST);
		m2_tf = new JTextField();
		if(chvo.getM2_code()!=null) {
			m2_name = new JTextField();
			m2_name.setText(parent_p.getName(chvo.getM2_code()));
			p3.add(m2_name, BorderLayout.CENTER);
			m2_name.setEditable(false);
			m2_tf.setText(chvo.getM2_code());
			p3.add(m2_tf, BorderLayout.EAST);
		}
		else if(chvo.getM2_code()==null){
			m2_tf.setText("폐차");
			p3.add(m2_tf, BorderLayout.CENTER);
		}
		
		n_p.add(p3);
		
		p4 = new JPanel(new BorderLayout());
		p4.add(new JLabel("변경일자"), BorderLayout.WEST);
		date_tf = new JTextField();
		date_tf.setText(chvo.getCh_date());
		p4.add(date_tf, BorderLayout.CENTER);
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
		
		s_p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		edit_bt = new JButton("수정");
		cancel_bt = new JButton("취소");
		s_p.add(edit_bt);
		s_p.add(cancel_bt);
		this.add(s_p, BorderLayout.SOUTH);
		
		this.setTitle("차량 소유주 변경 이력 수정");
		
		//창 크기 설정 및 창표시 설정
		this.setBounds(parent.getX()+50, parent.getY()+50, 250, 200);
		pack();
		this.setVisible(true);
		
		//edit_bt를 눌렀을 때 이벤트 감지자 추가.
		edit_bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeVO vo = new ChangeVO();
				vo.setCh_code(chvo.getCh_code());
				vo.setC_code(car_tf.getText());
				vo.setM1_code(m1_tf.getText());
				if(!m2_tf.getText().trim().isEmpty()) {
					vo.setM2_code(m2_tf.getText());
					vo.setCh_type("1");
				}
				else
					vo.setCh_type("0");
				vo.setCh_date(date_tf.getText().trim());
				parent_p.editChange(vo);
				parent.viewChange();

				InfoVO ivo = new InfoVO();
				ivo.setI_text(ta.getText());
				ivo.setI_type("4");
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
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				dispose();
			}
		
		});

		
	}
}
