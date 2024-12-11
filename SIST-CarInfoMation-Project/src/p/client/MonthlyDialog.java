package p.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import p.vo.FixVO;
import p.vo.MemberVO;

@SuppressWarnings("serial")
public class MonthlyDialog extends JDialog {

	ProjectFrame parent;
	MemberVO mvo;
	
	List<FixVO> list;
	JPanel north_p, south_p;
	JTable table;
	JTextField tf0, tf1, tf2, tf3, tf4, tf5, tf6;
    String[] c_name = { "정비일자", "차량번호", "엔진오일", "에어컨 필터", "브레이크 오일", "점화 플러그" , "정비 비용"};
	    
	public MonthlyDialog(ProjectFrame parent, MemberVO mvo) {
		this.parent = parent;
		this.mvo = mvo;
		
        
        north_p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String Month = parent.getMonth();
        north_p.add(new JLabel(Month + "월 결산" ));
        this.add(north_p, BorderLayout.NORTH);
		
		table = new JTable(new DefaultTableModel(c_name, 0));
		this.add(new JScrollPane(table), BorderLayout.CENTER);
        viewlist();

        south_p = new JPanel(new GridLayout(1,7));
        this.add(south_p, BorderLayout.SOUTH);
        
        tf0 = new JTextField();
        tf0.setEditable(false);
        south_p.add(tf0);
        
        tf1 = new JTextField("총계");
        tf1.setEditable(false);
        south_p.add(tf1);
        
        tf2 = new JTextField();
        tf2.setEditable(false);
        south_p.add(tf2);
        
        tf3 = new JTextField();
        tf3.setEditable(false);
        south_p.add(tf3);
        
        tf4 = new JTextField();
        tf4.setEditable(false);
        south_p.add(tf4);
        
        tf5 = new JTextField();
        tf5.setEditable(false);
        south_p.add(tf5);
        
        tf6 = new JTextField();
        tf6.setEditable(false);
        south_p.add(tf6);
        
        this.setBounds(parent.getX()+50, parent.getY()+50, 800, 350);
        this.setVisible(true);
        this.setTitle("월간 정산");
        setTF();
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
	}
	
	private void setTF() {
		int[] count = parent.getMonthUseCount(mvo);
		tf2.setText(count[0]+"개");
		tf3.setText(count[1]+"개");
		tf4.setText(count[2]+"개");
		tf5.setText(count[3]+"개");
		String price = priceToString(sumPrice(list));
		tf6.setText(price+"원");
	}
	
	private String[][] makeArray(List<FixVO> list) {
        String[][] ar = null;
        if (list != null && list.size() > 0) {
            ar = new String[list.size()][c_name.length];
            int i = 0;
            for (FixVO vo : list) {
                ar[i][0] = vo.getFix_date();
                ar[i][1] = parent.getC_num(vo.getC_code());
            	if (vo.getU_code1() != null) 
	            	ar[i][2] = "사용";
	            else
	            	ar[i][2] = "미사용";
	            if (vo.getU_code2() != null) 
	            	ar[i][3] = "사용";
	            else
	            	ar[i][3] = "미사용";
	            if (vo.getU_code3() != null) 
	            	ar[i][4] = "사용";
	            else
	            	ar[i][4] = "미사용";
	            if (vo.getU_code4() != null) 
	            	ar[i][5] = "사용";
	            else
	            	ar[i][5] = "미사용";
	            
	            ar[i][6] = String.valueOf(vo.getFix_price());
                i++;
            }
        }
        return ar;
    }
	
	private int sumPrice(List<FixVO> list) {
    	int total = 0;
    	int[] price = parent.getUsePrice();
		int[] count = parent.getMonthUseCount(mvo);
        if (list != null) {
            for (FixVO vo : list) {
                total += (int)Double.parseDouble(vo.getFix_price());
            }
            for(int i=0;i<count.length;i++) {
            	total += count[i]*price[i];
            }
        }
        return total;
    }
	
	private String priceToString(int price) {
		String numStr = Integer.toString(price);
		String result = "";
		
		int len = numStr.length();
		int commaPosition = len % 3;
		
		if (commaPosition > 0) {
			result += numStr.substring(0, commaPosition);
		} // 3자리마다 쉼표 추가 
		for (int i = commaPosition; i < len; i += 3) {
			if (result.length() > 0) {
				result += ","; 
			}
			result += numStr.substring(i, i + 3);
		}
		
		return result;
	}
	
	public void viewlist() {
		list = parent.getMonthly(mvo);
		String[][] ar = makeArray(list);
		table.setModel(new DefaultTableModel(ar, c_name));
	}

}
