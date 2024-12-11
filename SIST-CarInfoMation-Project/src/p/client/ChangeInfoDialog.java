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

import p.vo.ChangeVO;

@SuppressWarnings("serial")
public class ChangeInfoDialog extends JDialog {

	ProjectFrame parent;
	
	List<ChangeVO> list;
	//초기에 필요한 테이블, 컬럼명 지정.
	String[] change_info = {"변경순번", "차량번호", "전소유주", "현소유주", "변경종류", "변경일자"};
	JTable table;

	public ChangeInfoDialog(ProjectFrame parent) {
		this.parent = parent;

		list = parent.getChangeList();
		String[][] ar = makeArray(list);
		table = new JTable(new DefaultTableModel(ar, change_info));
		this.add(new JScrollPane(table));

		this.setBounds(parent.getX()+50, parent.getY()+50, 600, 650);
		this.setVisible(true);
		
		this.setTitle("차량 소유주 변경 이력");
		
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				ChangeVO chvo = list.get(row);
				new ChangeEditDialog(ChangeInfoDialog.this, chvo);
			}
		
		
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});

		
	}
	
	private String[][] makeArray(List<ChangeVO> list) {
		String[][] ar = null;
		if(list != null && list.size() > 0) {
			ar = new String[list.size()][change_info.length];
			int i = 0;
			for(ChangeVO vo : list) {
				ar[i][0] = vo.getCh_code();
				ar[i][1] = parent.getC_num(vo.getC_code());
				ar[i][2] = parent.getName(vo.getM1_code());
				ar[i][3] = parent.getName(vo.getM2_code());
				if(vo.getCh_type().contains("1")) {
					ar[i][4] = "매매";
				}
				else if(vo.getCh_type().contains("0")){
					ar[i][4] = "폐차";
				}
				else {
					ar[i][4] = "등록";
				}
				ar[i][5] = vo.getCh_date();
				i++;
			}
		}
		return ar;
	}
	
	public void viewChange() {
		list = parent.getChangeList();
		String[][] ar = makeArray(list);
		table.setModel(new DefaultTableModel(ar, change_info));
	}

}
