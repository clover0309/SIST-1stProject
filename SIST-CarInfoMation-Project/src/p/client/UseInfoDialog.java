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

import p.vo.UseVO;

@SuppressWarnings("serial")
public class UseInfoDialog extends JDialog {

	ProjectFrame parent;
	
	String[] use_info = {"품목번호", "소모품명", "가격"};
	JTable table;
	
	List<UseVO> list;

	
	
	public UseInfoDialog(ProjectFrame parent) {
		this.parent = parent;
		
		list = parent.getUseList();
		String[][] ar = makeArray(list);
		table = new JTable(new DefaultTableModel(ar, use_info));
		this.add(new JScrollPane(table));

		this.setBounds(parent.getX()+50, parent.getY()+50, 300, 350);
		this.setVisible(true);
		
		this.setTitle("소모품 정보");
		
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				UseVO uvo = list.get(row);
				new UseEditDialog(UseInfoDialog.this, uvo);
			}
		
		
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});
		
	}
	
	private String[][] makeArray(List<UseVO> list) {
		String[][] ar = null;
		if(list != null && list.size() > 0) {
			ar = new String[list.size()][use_info.length];
			
			int i = 0;
			
			for(UseVO vo : list) {				
				ar[i][0] = vo.getU_code();
				ar[i][1] = vo.getU_name();
				ar[i][2] = vo.getPrice();
				i++;
			}
		}
		return ar;
	}
	
	public void viewUse() {
		list = parent.getUseList();
		String[][] ar = makeArray(list);
		table.setModel(new DefaultTableModel(ar, use_info));
	}


}
