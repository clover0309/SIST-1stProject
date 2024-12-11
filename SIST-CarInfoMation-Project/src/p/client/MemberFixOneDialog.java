package p.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import p.vo.FixVO;

@SuppressWarnings("serial")
public class MemberFixOneDialog extends JDialog {
	MemberFixDialog parent;
	CarFixDialog parent2;
	FixVO fvo;
	
	JPanel north_p, center_p;
	JPanel n_p1, n_p2, n_p3, n_p4;
	JPanel cb_p;
	JLabel lb1, lb2, lb3, lb4;
	JTextField tf1, tf2;
	String[] use_ar = { "엔진오일", "에어컨 필터", "브레이크 오일", "점화 플러그" };
	JCheckBox[] use_cb = new JCheckBox[use_ar.length];
	String[] fix_type = { "일일정비", "정기정비" };
	JComboBox<String> type;
	
	JLabel lb5;
	JTextArea ta;
	
    JLabel lb6, img_lb;
    JPanel c_p1, c_p2;
    Dimension d = new Dimension(235, 170); // 이미지 크기 지정
	
	public MemberFixOneDialog(MemberFixDialog parent, FixVO fvo) {
		this.parent = parent;
		this.fvo = fvo;
		
		north_p = new JPanel(new GridLayout(4,1));
		n_p1 = new JPanel(new BorderLayout());
		n_p2 = new JPanel(new BorderLayout());
		n_p3 = new JPanel(new BorderLayout());
		n_p4 = new JPanel(new BorderLayout());

		cb_p = new JPanel(new GridLayout(1,4));
		for(int i=0;i<use_cb.length;i++) {
			use_cb[i] = new JCheckBox(use_ar[i]);
			cb_p.add(use_cb[i]);
			switch(i) {
				case 0:
					if(fvo.getU_code1() !=null)
						use_cb[i].setSelected(true);
					break;
				case 1:
					if(fvo.getU_code2() !=null)
						use_cb[i].setSelected(true);
					break;
				case 2:
					if(fvo.getU_code3() !=null)
						use_cb[i].setSelected(true);
					break;
				case 3:
					if(fvo.getU_code4() !=null)
						use_cb[i].setSelected(true);
					break;
			}
			use_cb[i].setEnabled(false);
		}
		lb1 = new JLabel("정비 날짜");
		lb2 = new JLabel("정비 비용");
		lb3 = new JLabel("소모품 선택");
		lb4 = new JLabel("정비 타입");

		tf1 = new JTextField();
		tf1.setText(fvo.getFix_date());
		tf1.setEditable(false);
		tf2 = new JTextField();
		tf2.setText(fvo.getFix_price());
		tf2.setEditable(false);
		
		type = new JComboBox<String>(fix_type);
		if(fvo.getFix_type().contains("0"))
			type.setSelectedIndex(0);
		else if(fvo.getFix_type().contains("1"))
			type.setSelectedIndex(1);
		type.setEnabled(false);
		
		n_p1.add(lb1, BorderLayout.WEST);
		n_p1.add(tf1, BorderLayout.CENTER);
		
		n_p2.add(lb2, BorderLayout.WEST);
		n_p2.add(tf2, BorderLayout.CENTER);
		
		n_p3.add(lb3, BorderLayout.WEST);
		n_p3.add(cb_p, BorderLayout.CENTER);
		
		n_p4.add(lb4, BorderLayout.WEST);
		n_p4.add(type, BorderLayout.CENTER);

		north_p.add(n_p1);
		north_p.add(n_p2);
		north_p.add(n_p3);
		north_p.add(n_p4);
		this.add(north_p, BorderLayout.NORTH);
		
		center_p = new JPanel(new GridLayout(1, 2));
		c_p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		c_p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		lb5 = new JLabel("정비 내용");
		ta = new JTextArea();
		ta.setText(fvo.getFix_text());
		ta.setEditable(false);
		ta.setPreferredSize(d);
		c_p1.add(lb5, BorderLayout.NORTH);
		c_p1.add(new JScrollPane(ta), BorderLayout.CENTER);
		
		lb6 = new JLabel("정비 이미지");
		img_lb = new JLabel();
		img_lb.setPreferredSize(d);
		img_lb.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		String imgPath = fvo.getImg_path();
		if (imgPath != null && !imgPath.isEmpty()) {
			File imgFile = new File("src/images/FixImage/" + imgPath);
			if (imgFile.exists()) {
				ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
				Image img = icon.getImage().getScaledInstance(d.width, d.height, Image.SCALE_SMOOTH);
				img_lb.setIcon(new ImageIcon(img));
			} else {
				img_lb.setText("이미지를 찾을 수 없음");
			}
		} else {
			img_lb.setText("이미지 없음");
		}
		c_p2.add(lb6);
		c_p2.add(img_lb);

		center_p.add(c_p1);
		center_p.add(c_p2);
		
		this.add(center_p, BorderLayout.CENTER);
		
		this.setTitle(fvo.getFix_date() + " 정비 세부 항목");
		
		this.setBounds(parent.getX()+50, parent.getY()+50, 500, 350);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});
		
	}
	
	public MemberFixOneDialog(CarFixDialog parent2, FixVO fvo) {
		this.parent2 = parent2;
		this.fvo = fvo;
		
		north_p = new JPanel(new GridLayout(4,1));
		n_p1 = new JPanel(new BorderLayout());
		n_p2 = new JPanel(new BorderLayout());
		n_p3 = new JPanel(new BorderLayout());
		n_p4 = new JPanel(new BorderLayout());

		cb_p = new JPanel(new GridLayout(1,4));
		for(int i=0;i<use_cb.length;i++) {
			use_cb[i] = new JCheckBox(use_ar[i]);
			cb_p.add(use_cb[i]);
			switch(i) {
				case 0:
					if(fvo.getU_code1() !=null)
						use_cb[i].setSelected(true);
					break;
				case 1:
					if(fvo.getU_code2() !=null)
						use_cb[i].setSelected(true);
					break;
				case 2:
					if(fvo.getU_code3() !=null)
						use_cb[i].setSelected(true);
					break;
				case 3:
					if(fvo.getU_code4() !=null)
						use_cb[i].setSelected(true);
					break;
			}
			use_cb[i].setEnabled(false);
		}
		lb1 = new JLabel("정비 날짜");
		lb2 = new JLabel("정비 비용");
		lb3 = new JLabel("소모품 선택");
		lb4 = new JLabel("정비 타입");

		tf1 = new JTextField();
		tf1.setText(fvo.getFix_date());
		tf1.setEditable(false);
		tf2 = new JTextField();
		tf2.setText(fvo.getFix_price());
		tf2.setEditable(false);
		
		type = new JComboBox<String>(fix_type);
		if(fvo.getFix_type().contains("0"))
			type.setSelectedIndex(0);
		else if(fvo.getFix_type().contains("1"))
			type.setSelectedIndex(1);
		type.setEnabled(false);
		n_p1.add(lb1, BorderLayout.WEST);
		n_p1.add(tf1, BorderLayout.CENTER);
		
		n_p2.add(lb2, BorderLayout.WEST);
		n_p2.add(tf2, BorderLayout.CENTER);
		
		n_p3.add(lb3, BorderLayout.WEST);
		n_p3.add(cb_p, BorderLayout.CENTER);
		
		n_p4.add(lb4, BorderLayout.WEST);
		n_p4.add(type, BorderLayout.CENTER);

		north_p.add(n_p1);
		north_p.add(n_p2);
		north_p.add(n_p3);
		north_p.add(n_p4);
		this.add(north_p, BorderLayout.NORTH);
		
		center_p = new JPanel(new GridLayout(1, 2));
		c_p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		c_p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		lb5 = new JLabel("정비 내용");
		ta = new JTextArea();
		ta.setText(fvo.getFix_text());
		ta.setEditable(false);
		ta.setPreferredSize(d);
		c_p1.add(lb5, BorderLayout.NORTH);
		c_p1.add(new JScrollPane(ta), BorderLayout.CENTER);
		
		lb6 = new JLabel("정비 이미지");
		img_lb = new JLabel();
		img_lb.setPreferredSize(d);
		img_lb.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		String imgPath = fvo.getImg_path();
		if (imgPath != null && !imgPath.isEmpty()) {
			File imgFile = new File("src/images/FixImage/" + imgPath);
			if (imgFile.exists()) {
				ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
				Image img = icon.getImage().getScaledInstance(d.width, d.height, Image.SCALE_SMOOTH);
				img_lb.setIcon(new ImageIcon(img));
			} else {
				img_lb.setText("이미지를 찾을 수 없음");
			}
		} else {
			img_lb.setText("이미지 없음");
		}
		c_p2.add(lb6);
		c_p2.add(img_lb);

		center_p.add(c_p1);
		center_p.add(c_p2);
		
		this.add(center_p, BorderLayout.CENTER);
		
		this.setTitle(fvo.getFix_date() + " 정비 세부 항목");
		
		this.setBounds(parent2.getX()+50, parent2.getY()+50, 500, 350);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});
		
	}
}
