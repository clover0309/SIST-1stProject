package p.client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import p.vo.MemberVO;

@SuppressWarnings("serial")
public class MemberInfoDialog extends JDialog {

	ProjectFrame parent;
	
	String[] user_info = {"회원번호", "이름", "비밀번호", "차량등록번호"};
	JTable table;
	
	List<MemberVO> list;

	
	
	public MemberInfoDialog(ProjectFrame parent) {
		this.parent = parent;
		
		list = parent.getMemberList();
		String[][] ar = makeArray(list);
		table = new JTable(new DefaultTableModel(ar, user_info));
		this.add(new JScrollPane(table));

		this.setBounds(parent.getX()+50, parent.getY()+50, 400, 450);
		this.setVisible(true);
		
		this.setTitle("회원정보");
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				MemberVO mvo = list.get(row);
				new MemberEditDialog(MemberInfoDialog.this, mvo);
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
			ar = new String[list.size()][user_info.length];
			
			int i = 0;
			
			for(MemberVO vo : list) {
				ar[i][0] = vo.getM_code();
				ar[i][1] = vo.getM_name();
				ar[i][2] = vo.getM_password();
				ar[i][3] = vo.getC_code();
				i++;
			}
		}
		return ar;
	}
	
	public void viewUser() {
		list = parent.getMemberList();
		String[][] ar = makeArray(list);
		table.setModel(new DefaultTableModel(ar, user_info));
	}


}
