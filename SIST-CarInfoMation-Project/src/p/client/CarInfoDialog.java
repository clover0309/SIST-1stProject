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

import p.vo.CarVO;

@SuppressWarnings("serial")
public class CarInfoDialog extends JDialog {

	ProjectFrame parent;
	
	List<CarVO> list;
	//초기에 필요한 테이블, 컬럼명 지정.
	String[] car_info = {"차량순번", "차량번호", "주행거리", "차량이름", "정기점검 유무", "차량종류"};
	JTable table;

	public CarInfoDialog(ProjectFrame parent) {
		this.parent = parent;

		list = parent.getCarList();
		String[][] ar = makeArray(list);
		table = new JTable(new DefaultTableModel(ar, car_info));
		this.add(new JScrollPane(table));

		this.setBounds(parent.getX()+50, parent.getY()+50, 600, 650);
		this.setVisible(true);
		
		this.setTitle("등록 차량 정보");
		
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				CarVO cvo = list.get(row);
				new CarEditDialog(CarInfoDialog.this, cvo);
			}
		
		
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}	
		});

		
	}
	
	private String[][] makeArray(List<CarVO> list) {
		String[][] ar = null;
		if(list != null && list.size() > 0) {
			ar = new String[list.size()][car_info.length];
			int i = 0;
			for(CarVO vo : list) {
				ar[i][0] = vo.getC_code();
				ar[i][1] = vo.getC_number();
				ar[i][2] = vo.getC_km(); 
				ar[i][3] = vo.getC_name();
				if(vo.getC_ok().contains("0"))
					ar[i][4] = "정기 정비 완료";
				else if(vo.getC_ok().contains("1"))
					ar[i][4] = "정기 정비 미완료";
				if(vo.getC_type()==null)
					ar[i][5] = "";
				else if(vo.getC_type().contains("0"))
					ar[i][5] = "승용";
				else if(vo.getC_type().contains("1"))
					ar[i][5] = "화물";
				else
					ar[i][5] = "";
				
				i++;
			}
		}
		return ar;
	}
	
	public void viewCar() {
		list = parent.getCarList();
		String[][] ar = makeArray(list);
		table.setModel(new DefaultTableModel(ar, car_info));
	}

}
