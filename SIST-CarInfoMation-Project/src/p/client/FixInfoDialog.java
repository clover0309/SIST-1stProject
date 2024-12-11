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

@SuppressWarnings("serial")
public class FixInfoDialog extends JDialog {
	ProjectFrame parent;
	
	String[] fix_info = {"정비등록번호", "회원번호", "차량등록번호", "정비일자"};
	JTable table;
	
	List<FixVO> list;
	
	public FixInfoDialog(ProjectFrame parent) {
		this.parent = parent;
		
		list = parent.getFixList();
		String[][] ar = makeArray(list);
		table = new JTable(new DefaultTableModel(ar, fix_info));
		this.add(new JScrollPane(table));

		this.setBounds(parent.getX()+50, parent.getY()+50, 400, 450);
		this.setVisible(true);
		
		this.setTitle("정비 정보");
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				FixVO fvo = list.get(row);
				new FixEditDialog(FixInfoDialog.this, fvo);
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});
	}
	
	private String[][] makeArray(List<FixVO> list) {
		String[][] ar = null;
		if(list != null && list.size() > 0) {
			ar = new String[list.size()][fix_info.length];
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
	public void viewdata() {
		list = parent.getFixList();
		String[][] ar = makeArray(list);
		table.setModel(new DefaultTableModel(ar, fix_info));
	}
	
}
