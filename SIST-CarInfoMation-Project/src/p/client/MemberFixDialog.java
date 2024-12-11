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

import p.vo.FixVO;
import p.vo.MemberVO;

@SuppressWarnings("serial")
public class MemberFixDialog extends JDialog {
	JTable table;
	String [] fix = {"정비등록번호", "회원이름", "차량등록번호", "정비날짜"};
	
	
	ProjectFrame parent;
	List<FixVO> list;
	
	public MemberFixDialog(ProjectFrame parent, MemberVO mvo) {
		this.parent = parent;
		this.list = parent.memberFix(mvo);
		this.add(new JScrollPane(table = new JTable(new DefaultTableModel(fix, 0))));
		
		this.setBounds(parent.getX()+50, parent.getY()+50, 400, 450);
		this.setVisible(true);
		viewData();
		
		this.setTitle("유저 정비 목록");
		
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				FixVO fvo = list.get(row);
				// 눌렀을 때 해당 정보를 가진 새로운 창을 띄움
				new MemberFixOneDialog(MemberFixDialog.this, fvo);
			}
		});
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}
	
	private String[][] makeArray() {
		String[][] ar = null;
		if(list != null && list.size() > 0) {
			ar = new String[list.size()][fix.length];
			int i = 0;
			for(FixVO vo : list) {
				ar[i][0] = vo.getF_code();
				ar[i][1] = parent.getName(vo.getM_code());
				ar[i][2] = parent.getC_num(vo.getC_code());
				ar[i][3] = vo.getFix_date();
				i++;
			}
		}
		return ar;
	}
	
	private void viewData() {
		
		String [][] ar = makeArray();
		table.setModel(new DefaultTableModel(ar, fix));
	}


}

