package p.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import p.vo.CarVO;
import p.vo.MemberVO;

@SuppressWarnings("serial")
public class KmDialog extends JDialog {

	ProjectFrame parent;
	MemberVO mvo;
	JPanel up; //현재 키로수 레이블, tf 넣을 패널
	JPanel down; //추가할 키로수 레이블, tf 넣을 패널
	
	JLabel thiskm;
	JTextField this_km; //멤버가 가진 차량의 km수를 가져와서 여기에 입력 / 수정 불가
	
	JLabel addkm;
	JTextField add_km;	//추가로 넣을 km수
	
	JPanel bt_p; // 버튼 넣을 패널
	JButton add, cancel; //추가, 취소 버튼
	
	public KmDialog(ProjectFrame parent, MemberVO mvo) {
		this.parent = parent;
		this.mvo = mvo;
		
		CarVO cvo = parent.getCarVO(mvo);
		up = new JPanel();
		thiskm = new JLabel("현재 키로수 : ");
		this_km = new JTextField(8); //member_t의 c_code를 값으로 받아 차량을 찾고 해당 차량의 km을 가져와서 삽입
		this_km.setText(cvo.getC_km());
		this_km.setEditable(false);
		up.add(thiskm, BorderLayout.WEST);
		up.add(this_km, BorderLayout.EAST);
		this.add(up, BorderLayout.NORTH);
		
		down = new JPanel();
		addkm = new JLabel("추가 키로수 : ");
		add_km = new JTextField(8); 
		down.add(addkm, BorderLayout.WEST);
		down.add(add_km, BorderLayout.EAST);
		this.add(down, BorderLayout.CENTER);

		bt_p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add = new JButton("추가");
		cancel = new JButton("취소");
		bt_p.add(add);
		bt_p.add(cancel);
		
		this.setTitle(parent.getC_num(mvo.getC_code())+" 키로수 추가");
		
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.addKm(add_km.getText());
				JOptionPane.showMessageDialog(parent, "키로수 수정 완료");
				dispose();
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		this.add(bt_p, BorderLayout.SOUTH);
		
		
		this.parent = parent;
		
		this.setBounds(parent.getX()+100, parent.getY()+100, 200, 140);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});
	}

	
}
