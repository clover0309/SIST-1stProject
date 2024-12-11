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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import p.vo.FixVO;
import p.vo.InfoVO;
import p.vo.MemberVO;

@SuppressWarnings("serial")
public class AddFixDialog extends JDialog {

	ProjectFrame parent;
	MemberVO mvo;
	
	JPanel north_p, center_p1, center_p2, center_p, south_p;
	JPanel n_p1, n_p2, n_p3, n_p4;
	JPanel cb_p;
	JLabel lb1, lb2, lb3, lb4, path_lb;
	JTextField tf1, tf2;
	String[] use_ar = { "엔진오일", "에어컨 필터", "브레이크 오일", "점화 플러그" };
	JCheckBox[] use_cb = new JCheckBox[use_ar.length];
	String[] fix_type = { "일일정비", "정기정비" };
	JComboBox<String> type;
	
	JLabel lb5;
	JTextArea ta;
	
	JButton save, cancel, choose_bt;
	
	File selected_file;
	
	public AddFixDialog(ProjectFrame parent, MemberVO mvo) {
		this.parent = parent;
		this.mvo = mvo;
		north_p = new JPanel(new GridLayout(4,1));
		n_p1 = new JPanel(new BorderLayout());
		n_p2 = new JPanel(new BorderLayout());
		n_p3 = new JPanel(new BorderLayout());
		n_p4 = new JPanel(new BorderLayout());

		cb_p = new JPanel(new GridLayout(1,4));
		for(int i=0;i<use_cb.length;i++) {
			use_cb[i] = new JCheckBox(use_ar[i]);
			cb_p.add(use_cb[i]);
		}
		lb1 = new JLabel("정비 날짜");
		lb2 = new JLabel("정비 비용");
		lb3 = new JLabel("소모품 선택");
		lb4 = new JLabel("정비 타입");

		tf1 = new JTextField();
		tf2 = new JTextField();
		
		type = new JComboBox<String>(fix_type);

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
		
		center_p = new JPanel(new GridLayout(1,2));
		
		center_p1 = new JPanel(new BorderLayout());
		lb5 = new JLabel("정비 내용");
		ta = new JTextArea();
		center_p1.add(lb5, BorderLayout.NORTH);
		center_p1.add(ta, BorderLayout.CENTER);
		
		center_p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		center_p2.add(path_lb = new JLabel());
		path_lb.setPreferredSize(new Dimension(180, 120));
		path_lb.setBorder(new BevelBorder(BevelBorder.RAISED));
		center_p2.add(choose_bt = new JButton("파일선택"));
		
		center_p.add(center_p1);
		center_p.add(center_p2);
		
		this.add(center_p, BorderLayout.CENTER);
		
		south_p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		save = new JButton("저장");
		cancel = new JButton("취소");
		south_p.add(save);
		south_p.add(cancel);
		this.add(south_p, BorderLayout.SOUTH);
		
		this.setTitle("정비 추가");
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int ok = parent.isFix(mvo);
				if(ok>0&&type.getSelectedItem().equals("정기정비")) {
					JOptionPane.showMessageDialog(AddFixDialog.this, "해당 월에 이미 정기 정비가 등록되었습니다.");
					return;
				}
				FixVO fvo = new FixVO();
				fvo.setM_code(mvo.getM_code());
				fvo.setC_code(mvo.getC_code());
				fvo.setFix_date(tf1.getText());
				fvo.setFix_price(tf2.getText());
				if(use_cb[0].isSelected())
					fvo.setU_code1("1");
				if(use_cb[1].isSelected())
					fvo.setU_code2("2");
				if(use_cb[2].isSelected())
					fvo.setU_code3("3");
				if(use_cb[3].isSelected())
					fvo.setU_code4("4");
				
				if(type.getSelectedIndex()==0)
					fvo.setFix_type("0");
				else
					fvo.setFix_type("1");
				fvo.setFix_text(ta.getText());
				
				if(selected_file != null) {
					fvo.setImg_path(selected_file.getName());
					BufferedInputStream bis = null;
					BufferedOutputStream bos = null;
					try {
						bis = new BufferedInputStream(new FileInputStream(selected_file));
						bos = new BufferedOutputStream(new FileOutputStream(new File("src/images/FixImage", selected_file.getName())));
						byte[] buf = new byte[2048];
						int size = -1;
						while((size = bis.read(buf)) != -1) {
							bos.write(buf, 0, size);
							bos.flush();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				
				parent.savefix(fvo);
				
				InfoVO ivo = new InfoVO();
				ivo.setI_text("회원 정비 이력 추가");
				ivo.setI_type("2");
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
		
		choose_bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser("c:/my_study/mybatis_study/work/First_Project/src/images");
				jfc.setFileFilter(new javax.swing.filechooser.FileFilter() {
					
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
				int cmd = jfc.showOpenDialog(AddFixDialog.this);
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
		
		
		this.setBounds(parent.getX()*2, parent.getY()+50, 500, 450);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});
		
	}
	
}
