package clientside;

import java.util.ArrayList;
import java.util.List;

public class Function {
	private List<Double> �����;
	boolean ����������� = false;
	
	Function(double a, double b, double c) {
		�����������(a, b ,c);
	}
	
	private void �����������(double a, double b, double c) {
		����� = new ArrayList<Double>();
		����������� = false;
		
		if (a*b*c != 0) {
			double D = �����������������(a, b, c);
			if (D < 0) {
			}
			else if (D == 0) {
				//D == 0 -> x = -b / 2a
				�����.add(-b / (2*a));
			}
			else {
				//D == 0 -> x = -b +- vD / 2a
				�����.add((-b - Math.sqrt(D)) / (2*a));
				�����.add((-b + Math.sqrt(D)) / (2*a));
			}
		}
		else if (c == 0 && a*b != 0) {
			�����.add(0d);
			�����.add(-b / a);
		}
		else if (b == 0 && a*c != 0) {
			if (c < 0) {
				double x2 = -c / a;
				�����.add(Math.sqrt(x2));
				�����.add(-Math.sqrt(x2));
			}
		}
		else if (a == 0 && b*c != 0) {
			double x = -c / b;
			�����.add(x);
		}
		else if ((a == 0 || b == 0) && c == 0) {
			����������� = true;
		}
		else if (a*b == 0 && c != 0) {
			//��� ������
		}
	}
	
	private double �����������������(double a, double b, double c) {
		//D = bb - 4ac
		return b*b - 4*a*c;
	}
	
	List<Double> ������������() {
		return �����;
	}
}
