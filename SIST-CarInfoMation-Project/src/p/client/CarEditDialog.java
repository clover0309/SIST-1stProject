package p.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import p.vo.CarVO;
import p.vo.InfoVO;

@SuppressWarnings("serial")
public class CarEditDialog extends JDialog {

	CarInfoDialog parent;
	ProjectFrame parent_p;
	CarVO cvo;
	
	String type[] = {"정기 점검 완료", "정기 점검 미완료"};
	String type2[] = {"추가필요", "승용", "화물"};
	
	JTextField cn_tf, ck_tf;
	JComboBox<String> co_jc, ct_jc;
	
	JTextField fix, change;
	JButton edit_bt, cancel_bt;
	
	JPanel p1, p2, p3, p4, s_p, n_p, c_p1, c_p2, c_p;
	JTextArea ta;
	
	public CarEditDialog(CarInfoDialog parent, CarVO cvo) {
		this.parent = parent;
		ProjectFrame parent_p = parent.parent;
		this.cvo = cvo;
		this.setTitle(cvo.getC_number()+"번 차량정보");
		n_p = new JPanel();
		n_p.setLayout(new GridLayout(4,1));

		p1 = new JPanel(new BorderLayout());
		p1.add(new JLabel("차량 번호"), BorderLayout.WEST);
		cn_tf = new JTextField(8);
		cn_tf.setText(cvo.getC_number());
		p1.add(cn_tf, BorderLayout.CENTER);
		n_p.add(p1);
		
		p2 = new JPanel(new BorderLayout());
		p2.add(new JLabel("주행 거리"), BorderLayout.WEST);
		ck_tf = new JTextField(8);
		ck_tf.setText(cvo.getC_km());
		p2.add(ck_tf, BorderLayout.CENTER);
		n_p.add(p2);
		
		p3 = new JPanel(new BorderLayout());
		p3.add(new JLabel("정기 점검 여부"), BorderLayout.WEST);
		co_jc = new JComboBox<>(type);
		p3.add(co_jc, BorderLayout.CENTER);
		n_p.add(p3);
		int i;
		if(cvo.getC_ok().contains("0"))
			i = 0;
		else
			i = 1;
		co_jc.setSelectedIndex(i);
		
		p4 = new JPanel(new BorderLayout());
		p4.add(new JLabel("차량 종류"), BorderLayout.WEST);
		ct_jc = new JComboBox<>(type2);
		p4.add(ct_jc, BorderLayout.CENTER);
		n_p.add(p4);
		int i2;
		if (cvo.getC_type() == null)
			i2 = 0;
		else if(cvo.getC_type().contains("0"))
			i2 = 1;
		else if(cvo.getC_type().contains("1"))
			i2 = 2;
		else
			i2 = 0;
		ct_jc.setSelectedIndex(i2);
		
		this.add(n_p, BorderLayout.NORTH);
		
		c_p1 = new JPanel(new GridLayout(1, 2));
		fix = new JTextField("차량 정비 정보");
		fix.setEditable(false);
		change = new JTextField("차량 소유주 정보");
		change.setEditable(false);
		c_p1.add(fix);
		c_p1.add(change);
		
		c_p2 = new JPanel(new BorderLayout());
		ta = new JTextArea();
		JPanel nc_p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nc_p.add(new JLabel("수정 내용"));
		c_p2.add(nc_p, BorderLayout.NORTH);
		c_p2.add(new JScrollPane(ta), BorderLayout.CENTER);
		Dimension minSize = new Dimension(200, 100);
		ta.setMinimumSize(minSize);
		ta.setPreferredSize(minSize);
		
		c_p = new JPanel(new BorderLayout());
		c_p.add(c_p2, BorderLayout.CENTER);		
		c_p.add(c_p1, BorderLayout.NORTH);
		
		this.add(c_p, BorderLayout.CENTER);
		
		
		s_p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		s_p.add(edit_bt = new JButton("수정"));
		s_p.add(cancel_bt = new JButton("취소"));
		this.add(s_p, BorderLayout.SOUTH);
		
		//창 크기 설정 및 창표시 설정
		this.setBounds(parent.getX()+50, parent.getY()+50, 230, 200);
		pack();
		this.setVisible(true);
		
		this.setTitle("차량 정보 수정");
		
		//edit_bt를 눌렀을 때 이벤트 감지자 추가.
		edit_bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//공백 값을 제거하기 위해, trim을 사용해 공백 값을
				//전부 제거함. (해당영역은 공백을 허용하지않음.)
				String c_code = cn_tf.getText().trim();
				String c_km = ck_tf.getText().trim();
				
				CarVO vo = new CarVO();
				vo.setC_code(cvo.getC_code());
				
				if(c_code.length() > 0)
					vo.setC_number(c_code);
				if(c_km.length() > 0)
					vo.setC_km(c_km);
				String c_ok;
				if(co_jc.getSelectedIndex()==0)
					c_ok = "0";
				else
					c_ok = "1";
				vo.setC_ok(c_ok);
				String c_type;
				if(ct_jc.getSelectedIndex()==0) {
					c_type = null;
					JOptionPane.showMessageDialog(CarEditDialog.this, "차종을 선택하세요.");
					return;
				}
				else if (ct_jc.getSelectedIndex()==1)
					c_type = "0";
				else
					c_type = "1";
				vo.setC_type(c_type);
				
				parent_p.editCar(vo);
				parent.viewCar();

				InfoVO ivo = new InfoVO();
				ivo.setI_text(ta.getText());
				ivo.setI_type("1");
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
		
		fix.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new CarFixDialog(CarEditDialog.this, cvo);
			}
		});
		
		change.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new CarChangeDialog(CarEditDialog.this, cvo);
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
