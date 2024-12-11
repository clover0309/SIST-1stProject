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
import p.vo.UseVO;

@SuppressWarnings("serial")
public class UseEditDialog extends JDialog {
	UseInfoDialog parent;
	UseVO uvo;
	ProjectFrame parent_p;
	
	JTextField unum_tf ,un_tf, p_tf;
	JButton edit_bt, cancel_bt;
	
	JPanel p1, p2, p3, s_p, n_p, c_p;
	JTextArea ta;
	
	public UseEditDialog(UseInfoDialog parent, UseVO uvo) {
		this.parent = parent;
		this.uvo = uvo;
		ProjectFrame parent_p = parent.parent;
		this.setTitle(uvo.getU_name()+" 소모품 정보");
		n_p = new JPanel();
		n_p.setLayout(new GridLayout(3,1));		

		p1 = new JPanel(new BorderLayout());
		p1.add(new JLabel("품목번호"), BorderLayout.WEST);
		unum_tf = new JTextField();
		unum_tf.setText(uvo.getU_code());
		p1.add(unum_tf, BorderLayout.CENTER);
		n_p.add(p1);
		
		p2 = new JPanel(new BorderLayout());
		p2.add(new JLabel("소모품 이름"), BorderLayout.WEST);
		un_tf = new JTextField(8);
		un_tf.setText(uvo.getU_name());
		p2.add(un_tf, BorderLayout.CENTER);
		n_p.add(p2);
		
		p3 = new JPanel(new BorderLayout());
		p3.add(new JLabel("수리가격"), BorderLayout.WEST);
		p_tf = new JTextField(8);
		p_tf.setText(uvo.getPrice());
		p3.add(p_tf, BorderLayout.CENTER);
		n_p.add(p3);
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
		s_p.add(edit_bt = new JButton("수정"));
		s_p.add(cancel_bt = new JButton("취소"));
		this.add(s_p, BorderLayout.SOUTH);
		
		//창 크기 설정 및 창표시 설정
		this.setBounds(parent.getX()+50, parent.getY()+50, 200, 200);
		pack();
		this.setVisible(true);
		
		this.setTitle("소모품 정보 수정");
		
		//edit_bt를 눌렀을 때 이벤트 감지자 추가.
		edit_bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//공백 값을 제거하기 위해, trim을 사용해 공백 값을
				//전부 제거함. (해당영역은 공백을 허용하지않음.)
				String u_name = un_tf.getText().trim();
				String price = p_tf.getText().trim();
				
				UseVO vo = new UseVO();
				vo.setU_code(uvo.getU_code());
				
				if(u_name.length() > 0)
					vo.setU_name(u_name);
				if(price.length() > 0)
					vo.setPrice(price);
				
				parent_p.editUse(vo);
				parent.viewUse();

				InfoVO ivo = new InfoVO();
				ivo.setI_text(ta.getText());
				ivo.setI_type("3");
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
