package p.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;

import p.vo.FixVO;
import p.vo.InfoVO;

@SuppressWarnings("serial")
public class FixEditDialog extends JDialog {

	FixInfoDialog parent;
	ProjectFrame parent_p;
	FixVO fvo;
	
	JPanel north_p, center_p, south_p;
	JPanel n_p1, n_p2, n_p3, n_p4, c_p;
	JPanel cb_p;
	JLabel lb1, lb2, lb3, lb4;
	JTextField tf1, tf2;
	String[] use_ar = { "엔진오일", "에어컨 필터", "브레이크 오일", "점화 플러그" };
	JCheckBox[] use_cb = new JCheckBox[use_ar.length];
	String[] fix_type = { "일일정비", "정기정비" };
	JComboBox<String> type;
	
	JLabel lb5;
	JTextArea ta1;
	
	JButton save, cancel;
	JTextArea ta;
	
	File selected_file;
	JButton choose_bt;
	JPanel s_p1, s_p2, c_p1, c_p2;
	JLabel path_lb, lb6, img_lb;
	Dimension d = new Dimension(180, 120);
	
	public FixEditDialog(FixInfoDialog parent, FixVO fvo) {
		this.parent = parent;
		this.parent_p = parent.parent;
		this.fvo = fvo;
		
		String name = parent_p.getName(fvo.getM_code());
		String c_num = parent_p.getC_num(fvo.getC_code());
		this.setTitle(name+"님 "+c_num+"번 정비 정보");
		
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
		}
		lb1 = new JLabel("정비 일자");
		lb2 = new JLabel("정비 비용");
		lb3 = new JLabel("소모품 선택");
		lb4 = new JLabel("정비 타입");

		tf1 = new JTextField();
		tf1.setText(fvo.getFix_date());
		tf2 = new JTextField();
		tf2.setText(fvo.getFix_price());
		
		type = new JComboBox<String>(fix_type);
		if(fvo.getFix_type().contains("0"))
			type.setSelectedIndex(0);
		else if(fvo.getFix_type().contains("1"))
			type.setSelectedIndex(1);
		
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
		c_p1 = new JPanel(new BorderLayout());
		c_p2 = new JPanel(new BorderLayout());
		
		lb5 = new JLabel("정비 내용");
		ta1 = new JTextArea();
		ta1.setText(fvo.getFix_text());
		c_p1.add(lb5, BorderLayout.NORTH);
		c_p1.add(new JScrollPane(ta1), BorderLayout.CENTER);
		
		lb6 = new JLabel("정비 이미지");
		img_lb = new JLabel();
		img_lb.setPreferredSize(d);
		img_lb.setBorder(new BevelBorder(BevelBorder.RAISED));
		String imgPath = fvo.getImg_path(); // FixVO에서 이미지 경로 가져오기
		if (imgPath != null && !imgPath.isEmpty()) {
			File imgFile = new File("src/images/FixImage/" + imgPath);
			if (imgFile.exists()) {
				ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
				Image img = icon.getImage().getScaledInstance(d.width, d.height, Image.SCALE_SMOOTH);
				img_lb.setIcon(new ImageIcon(img));
			} else {
				img_lb.setText("이미지를 찾을 수 없음");
			}
		}
		else {
			img_lb.setText("이미지 없음");
		}
		JPanel c_p22 = new JPanel(new BorderLayout());
		c_p22.add(img_lb, BorderLayout.CENTER);
		c_p22.add(choose_bt = new JButton("파일선택"), BorderLayout.EAST);
		c_p2.add(lb6, BorderLayout.NORTH);
		c_p2.add(c_p22, BorderLayout.CENTER);
		
		c_p = new JPanel(new BorderLayout());
		ta = new JTextArea();
		JPanel nc_p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nc_p.add(new JLabel("수정 내용"));
		c_p.add(nc_p, BorderLayout.NORTH);
		c_p.add(new JScrollPane(ta), BorderLayout.CENTER);
		ta.setMinimumSize(d);
		ta.setPreferredSize(d);
		c_p2.add(c_p, BorderLayout.SOUTH);
		
		center_p.add(c_p1);
		center_p.add(c_p2);
		
		this.add(center_p, BorderLayout.CENTER);
		
		south_p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		save = new JButton("수정");
		cancel = new JButton("취소");
		south_p.add(save);
		south_p.add(cancel);
		this.add(south_p, BorderLayout.SOUTH);
		
		this.setTitle("정비 정보 수정");
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FixVO fvo2 = new FixVO();
				fvo2.setF_code(fvo.getF_code());
				fvo2.setFix_date(tf1.getText());
				fvo2.setFix_price(tf2.getText());
				if(use_cb[0].isSelected())
					fvo2.setU_code1("1");
				if(use_cb[1].isSelected())
					fvo2.setU_code2("2");
				if(use_cb[2].isSelected())
					fvo2.setU_code3("3");
				if(use_cb[3].isSelected())
					fvo2.setU_code4("4");
				
				if(type.getSelectedIndex()==0)
					fvo2.setFix_type("0");
				else
					fvo2.setFix_type("1");
				fvo2.setFix_text(ta1.getText());
				parent_p.editFix(fvo2);
				parent.viewdata();

				InfoVO ivo = new InfoVO();
				ivo.setI_text(ta.getText());
				ivo.setI_type("2");
				parent_p.addInfo(ivo);
				
				dispose();
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		choose_bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser("c:/my_study/mybatis_study/work/First_Project/src/images");
				jfc.setFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						return "Image Files (*.gif)";
					}
					
					@Override
					public boolean accept(File f) {
						return f.isDirectory() || f.getAbsolutePath().endsWith(".gif") 
								|| f.getAbsolutePath().endsWith(".jpg")
								 || f.getAbsolutePath().endsWith(".png");
					}
				});
				
				//이미지 크기 path_lb에 자동 맞춤
				int cmd = jfc.showOpenDialog(FixEditDialog.this);
				if(cmd == JFileChooser.APPROVE_OPTION) {
					selected_file = jfc.getSelectedFile();
					ImageIcon icon = new ImageIcon(selected_file.getAbsolutePath());
					Image img = icon.getImage();
					Image changeImg = img.getScaledInstance(path_lb.getSize().width, path_lb.getSize().height,Image.SCALE_SMOOTH);
					ImageIcon changeIcon = new ImageIcon(changeImg);					
					
					path_lb.setIcon(changeIcon);
					path_lb.updateUI();
				}
			}
		});

		
		this.setBounds(parent.getX()+50, parent.getY()+50, 500, 450);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});
		
	}
}
