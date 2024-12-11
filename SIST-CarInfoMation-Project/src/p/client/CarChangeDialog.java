package p.client;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import p.vo.CarVO;
import p.vo.ChangeVO;

@SuppressWarnings("serial")
public class CarChangeDialog extends JDialog {

	CarEditDialog parent;
	ProjectFrame parent_pp;
	CarVO cvo;
	
	JTable table;
	String[] c_name = { "차량 번호", "전소유주", "현소유주", "상태", "변경일자" };
	
    List<ChangeVO> list;
	
	public CarChangeDialog(CarEditDialog parent, CarVO cvo) {
		this.parent = parent;
		parent_pp = parent.parent.parent;
		this.cvo = cvo;
		
		table = new JTable(new DefaultTableModel(c_name, 0));
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		
		this.setBounds(parent.getX()*2, parent.getY()+200, 400, 450);
		this.setVisible(true);
		
		this.setTitle(parent_pp.getC_num(cvo.getC_code()) + "번 차량 소유주 변경 이력");
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});
		
        viewData(); // 데이터 로드 및 테이블 업데이트
	}
	
	 private void viewData() {
         list = parent_pp.getCarChangeList(cvo);
         String[][] ar = makeArray(list);

         table.setModel(new DefaultTableModel(ar, c_name)); // JTable 데이터 설정
	 }

	 private String[][] makeArray(List<ChangeVO> list) {
		 if (list == null || list.isEmpty()) {
			 JOptionPane.showMessageDialog(this, "조회 결과가 없습니다.", "결과 없음", JOptionPane.INFORMATION_MESSAGE);
			 dispose();
			 return new String[0][0];
		 }
		 String[][] ar = new String[list.size()][c_name.length];
		 int i = 0;
		 for (ChangeVO vo : list) {
			 ar[i][0] = parent_pp.getC_num(vo.getC_code()); // 차량 번호
			 ar[i][1] = parent_pp.getName(vo.getM1_code());
			 if(vo.getM2_code()==null)
				 ar[i][2] = "-";
			 else
				 ar[i][2] = parent_pp.getName(vo.getM2_code());
			 ar[i][3] = vo.getCh_type().equals("0") ? "폐차" : "매매"; // 상태
			 ar[i][4] = vo.getCh_date(); // 날짜
			 i++;
		 }
		 return ar;
	 }
}