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

import p.vo.InfoVO;

@SuppressWarnings("serial")
public class InfoDialog extends JDialog {
	LogInfoDialog parent;
	InfoVO ivo;
	ProjectFrame parent_p;
	
	JTextField date_tf ,type_tf, user_tf;
	JTextArea text;
	JButton cancel_bt;
	
	JPanel p1, p2, p3, p4, p5, n_p, c_p;
	
	public InfoDialog(LogInfoDialog parent, InfoVO ivo) {
		this.parent = parent;
		this.ivo = ivo;
		ProjectFrame parent_p = parent.parent;
		
		this.setTitle(ivo.getI_date() + "수정 내역");
		n_p = new JPanel();
		n_p.setLayout(new GridLayout(3,1));		

		p1 = new JPanel(new BorderLayout());
		p1.add(new JLabel("수정 일자"), BorderLayout.WEST);
		date_tf = new JTextField();
		date_tf.setText(ivo.getI_date());
		date_tf.setEditable(false);
		p1.add(date_tf, BorderLayout.CENTER);
		n_p.add(p1);
		
		p2 = new JPanel(new BorderLayout());
		p2.add(new JLabel("수정 위치"), BorderLayout.WEST);
		type_tf = new JTextField(8);
		if(ivo.getI_type().contains("0")) {
			type_tf.setText("사용자 정보 수정");
		}
		else if(ivo.getI_type().contains("1")) {
			type_tf.setText("차량 정보 수정");
		}
		else if(ivo.getI_type().contains("2")) {
			type_tf.setText("정비 내역 수정");
		}
		else if(ivo.getI_type().contains("3")) {
			type_tf.setText("소모품 정보 수정");
		}
		else if(ivo.getI_type().contains("4")) {
			type_tf.setText("소유주 변경 이력 수정");
		}
		else
			type_tf.setText("-");
		type_tf.setEditable(false);
		p2.add(type_tf, BorderLayout.CENTER);
		n_p.add(p2);
		
		p3 = new JPanel(new BorderLayout());
		p3.add(new JLabel("수정 유저"), BorderLayout.WEST);
		user_tf = new JTextField(8);
		if(ivo.getM_code()==null) {
			user_tf.setText("관리자");
		}
		else {
			user_tf.setText(parent_p.getName(ivo.getM_code()));
		}
		user_tf.setEditable(false);
		p3.add(user_tf, BorderLayout.CENTER);
		n_p.add(p3);
		
		this.add(n_p, BorderLayout.NORTH);
		
		c_p = new JPanel(new BorderLayout());
		p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p4.add(new JLabel("수정 내용"));
		text = new JTextArea();
		text.setText(ivo.getI_text());
		Dimension minSize = new Dimension(200, 100);
		text.setMinimumSize(minSize);
		text.setPreferredSize(minSize);
		text.setEditable(false);
		c_p.add(p4, BorderLayout.NORTH);
		c_p.add(new JScrollPane(text), BorderLayout.CENTER);
		this.add(c_p, BorderLayout.CENTER);
		
		p5 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p5.add(cancel_bt = new JButton("닫기"));
		this.add(p5, BorderLayout.SOUTH);
		
		//창 크기 설정 및 창표시 설정
		this.setBounds(parent.getX()+50, parent.getY()+50, 200, 200);
		pack();
		this.setVisible(true);
		
		this.setTitle("소모품 정보 수정");
		
		//edit_bt를 눌렀을 때 이벤트 감지자 추가.
		
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
