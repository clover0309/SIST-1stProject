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

import p.vo.InfoVO;

@SuppressWarnings("serial")
public class LogInfoDialog extends JDialog {

	ProjectFrame parent;
	
	String[] log_info = {"순번", "수정 유저", "수정 일자", "수정 위치"};
	JTable table;
	
	List<InfoVO> list;

	
	
	public LogInfoDialog(ProjectFrame parent) {
		this.parent = parent;
		
		list = parent.getInfoList();
		String[][] ar = makeArray(list);
		table = new JTable(new DefaultTableModel(ar, log_info));
		this.add(new JScrollPane(table));

		this.setBounds(parent.getX()+50, parent.getY()+50, 500, 550);
		this.setVisible(true);
		
		this.setTitle("수정 이력");
		
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				InfoVO ivo = list.get(row);
				new InfoDialog(LogInfoDialog.this, ivo);
			}
		
		
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});
		
	}
	
	private String[][] makeArray(List<InfoVO> list) {
		String[][] ar = null;
		if(list != null && list.size() > 0) {
			ar = new String[list.size()][log_info.length];
			
			int i = 0;
			
			for(InfoVO vo : list) {				
				ar[i][0] = vo.getI_code();
				if(vo.getM_code()==null) {
					ar[i][1] = "관리자";
				}
				else {
					ar[i][1] = parent.getName(vo.getM_code());
				}
				ar[i][2] = vo.getI_date();
				if(vo.getI_type().contains("0")) {
					ar[i][3] = "사용자 정보 수정";
				}
				else if(vo.getI_type().contains("1")) {
					ar[i][3] = "차량 정보 수정";
				}
				else if(vo.getI_type().contains("2")) {
					ar[i][3] = "정비 내역 수정";
				}
				else if(vo.getI_type().contains("3")) {
					ar[i][3] = "소모품 정보 수정";
				}
				else if(vo.getI_type().contains("4")) {
					ar[i][3] = "소유주 변경 이력 수정";
				}
				else {
					ar[i][3] = "-";
				}
				i++;
			}
		}
		return ar;
	}
	
	public void viewUse() {
		list = parent.getInfoList();
		String[][] ar = makeArray(list);
		table.setModel(new DefaultTableModel(ar, log_info));
	}


}
