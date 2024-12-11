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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import p.vo.InfoVO;
import p.vo.MemberVO;

@SuppressWarnings("serial")
public class ChangeDialog extends JDialog {

	ProjectFrame parent;
	JPanel n_p;
	JTextField search_tf;
	JButton search_bt;
	
	JTable table;
	
	List<MemberVO> m_list;
	String[] info = {"차량번호", "사용자명"};
	
	JPanel e_p, ec_p;
	int selected = 0;
	JPanel m_p, m_p1, m_p2;
	JTextField name1, number1; //차량 매매할 사람
	MemberVO mvo1; //차량 매매할 사람 객체 vo
	
	JTextField name2; //차량 받을 사람(차량 없어야 함)
	MemberVO mvo2; //차량 받을 사람 객체 vo (차량이 없어야 함)
	
	JPanel bt_p;
	JButton ok_bt, cancel_bt;
	JTextArea ta;
	
	public ChangeDialog(ProjectFrame parent) {
		this.parent = parent;
		
		n_p = new JPanel(new BorderLayout());
		search_tf = new JTextField();
		search_bt = new JButton("검색");
		n_p.add(search_tf, BorderLayout.CENTER);
		n_p.add(search_bt, BorderLayout.EAST);
		this.add(n_p, BorderLayout.NORTH);
		
		table = new JTable(new DefaultTableModel(info, 0));
		this.add(new JScrollPane(table));

		
		e_p = new JPanel(new BorderLayout());
		m_p = new JPanel(new GridLayout(4, 1));
		m_p.add(new JLabel("차량 인도자"));
		m_p1 = new JPanel(new BorderLayout());
		number1 = new JTextField();
		number1.setEditable(false);
		name1 = new JTextField();
		name1.setEditable(false);
		number1.setText("00-0000");
		m_p1.add(number1, BorderLayout.WEST);
		m_p1.add(name1, BorderLayout.CENTER);
		m_p.add(m_p1);

		m_p.add(new JLabel("차량 인수자"));
		m_p2 = new JPanel(new BorderLayout());
		name2 = new JTextField();
		name2.setEditable(false);
		m_p2.add(name2, BorderLayout.CENTER);
		m_p.add(m_p2);
		
		e_p.add(m_p, BorderLayout.NORTH);
		
		bt_p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ok_bt = new JButton("확인");
		cancel_bt = new JButton("취소");
		bt_p.add(ok_bt);
		bt_p.add(cancel_bt);
		
		ec_p = new JPanel(new BorderLayout());
		ta = new JTextArea();
		JPanel nc_p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nc_p.add(new JLabel("수정 내용"));
		ec_p.add(nc_p, BorderLayout.NORTH);
		ec_p.add(new JScrollPane(ta), BorderLayout.CENTER);
		Dimension minSize = new Dimension(200, 100);
		ta.setMinimumSize(minSize);
		ta.setPreferredSize(minSize);
		e_p.add(ec_p, BorderLayout.CENTER);
		
		e_p.add(bt_p, BorderLayout.SOUTH);
		
		this.add(e_p, BorderLayout.EAST);
		
		
		this.setBounds(parent.getX()+50, parent.getY()+50, 400, 450);
		this.setVisible(true);
		
		this.setTitle("차량 소유주 변경");
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				MemberVO mvo = m_list.get(row);
				if(selected == 0) {
					if(mvo.getC_code()==null) {
						JOptionPane.showMessageDialog(ChangeDialog.this, "인도자는 차량을 보유한 사용자를 선택하여야 합니다.");
						return;
					}
					else {
						name1.setText(mvo.getM_name());
						number1.setText(parent.getC_num(mvo.getC_code()));
						mvo1 = mvo;
						selected = 1;
					}
				}
				else {
					if(mvo.getC_code()!=null) {
						JOptionPane.showMessageDialog(ChangeDialog.this, "인수자는 차량이 없는 사용자를 선택하여야 합니다.");
						return;
					}
					else {
						name2.setText(mvo.getM_name());
						mvo2 = mvo;
					}
				}
			}
		});
		
		name1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				name1.setText("");
				number1.setText("00-0000");
				mvo1 = null;
				selected = 0;
			}
		});
		
		number1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				name1.setText("");
				number1.setText("00-0000");
				mvo1 = null;
				selected = 0;
			}
		});
		
		name2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				name2.setText("");
				mvo2 = null;
				selected = 1;
			}
		});
		
		
		
		search_bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = search_tf.getText();
				m_list = parent.searchMemberList(str);
				String[][] ar = makeArray(m_list);
				table.setModel(new DefaultTableModel(ar, info));
			}
		});
		
		ok_bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mvo1==null) {
					JOptionPane.showMessageDialog(ChangeDialog.this, "인도자는 필수 입력사항입니다.");
					return;
				}
				if(mvo2==null) {
					//mvo1의 차를 폐차하는 parent 함수 호출
					parent.deleteCar(mvo1);
					
					InfoVO ivo = new InfoVO();
					ivo.setI_text(ta.getText());
					ivo.setI_type("4");
					parent.addInfo(ivo);
					
					dispose();
				}
				else {
					//mvo1과 mvo2에 저장된 값을 통해 parent에서 mvo1의 car를 mvo2에 넘겨주고 mvo1의 c_code를 null로 변경
					parent.changeCar(mvo1, mvo2);
					
					InfoVO ivo = new InfoVO();
					ivo.setI_text(ta.getText());
					ivo.setI_type("4");
					parent.addInfo(ivo);

					dispose();
				}
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
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});
	}
	
	private String[][] makeArray(List<MemberVO> list) {
		String[][] ar = null;
		if(list != null && list.size() > 0) {
			ar = new String[list.size()][info.length];
			
			int i = 0;
			
			for(MemberVO vo : list) {
				ar[i][0] = parent.getC_num(vo.getC_code());
				ar[i][1] = vo.getM_name();
				i++;
			}
		}
		return ar;
	}
}
