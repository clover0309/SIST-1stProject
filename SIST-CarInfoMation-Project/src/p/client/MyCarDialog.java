package p.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import p.vo.ChangeVO;
import p.vo.MemberVO;

@SuppressWarnings("serial")
public class MyCarDialog extends JDialog {

	ProjectFrame parent;
	MemberVO mvo;
	
	JTable table;
	String[] c_name = { "차량 번호", "상태", "날짜" };
	JTextField num, stat, date;
	JPanel s_p;
	
    List<ChangeVO> list;
    
	public MyCarDialog(ProjectFrame parent, MemberVO mvo) {
		this.parent = parent;
		this.mvo = mvo;
		
		table = new JTable(new DefaultTableModel(c_name, 0));
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		
		s_p = new JPanel(new GridLayout(1, 3));
		num = new JTextField();
		stat = new JTextField();
		date = new JTextField();
		s_p.add(num);
		s_p.add(stat);
		s_p.add(date);
		if(mvo.getC_code()!=null) {
			num.setText(parent.getC_num(mvo.getC_code()));
			stat.setText("소유중");
			date.setText("-");
		}
		else {
			num.setText("-");
			stat.setText("-");
			date.setText("-");
		}
		num.setEditable(false);
		stat.setEditable(false);
		date.setEditable(false);
		this.add(s_p, BorderLayout.SOUTH);
		
		this.setBounds(parent.getX()*2, parent.getY()+200, 400, 450);
		this.setVisible(true);
		
		this.setTitle("본인 소유 차량 이력");
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});
		
        viewData(); // 데이터 로드 및 테이블 업데이트
	}
	
	 private void viewData() {
         list = parent.getC_List(mvo);
         String[][] ar = makeArray(list);

         table.setModel(new DefaultTableModel(ar, c_name)); // JTable 데이터 설정
	 }

	 private String[][] makeArray(List<ChangeVO> list) {
		 if (list == null || list.isEmpty()) {
			 JOptionPane.showMessageDialog(this, "조회 결과가 없습니다.", "결과 없음", JOptionPane.INFORMATION_MESSAGE);
			 return new String[0][0];
		 }
		 String[][] ar = new String[list.size()][c_name.length];
		 int i = 0;
		 for (ChangeVO vo : list) {
			 ar[i][0] = parent.getC_num(vo.getC_code()); // 차량 번호
			 if(vo.getCh_type().contains("0"))
				 ar[i][1] = "폐차";
			 else if(vo.getCh_type().contains("1"))
				 ar[i][1] = "매매";
			 else if(vo.getCh_type().contains("2"))
				 ar[i][1] = "등록";
			 ar[i][2] = vo.getCh_date(); // 날짜
			 i++;
		 }
		 return ar;
	 }
}
