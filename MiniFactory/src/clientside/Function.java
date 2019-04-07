package clientside;

import java.util.ArrayList;
import java.util.List;

public class Function {
	private List<Double> корни;
	boolean всеѕодход€т = false;
	
	Function(double a, double b, double c) {
		найти орень(a, b ,c);
	}
	
	private void найти орень(double a, double b, double c) {
		корни = new ArrayList<Double>();
		всеѕодход€т = false;
		
		if (a*b*c != 0) {
			double D = найтиƒискременант(a, b, c);
			if (D < 0) {
			}
			else if (D == 0) {
				//D == 0 -> x = -b / 2a
				корни.add(-b / (2*a));
			}
			else {
				//D == 0 -> x = -b +- vD / 2a
				корни.add((-b - Math.sqrt(D)) / (2*a));
				корни.add((-b + Math.sqrt(D)) / (2*a));
			}
		}
		else if (c == 0 && a*b != 0) {
			корни.add(0d);
			корни.add(-b / a);
		}
		else if (b == 0 && a*c != 0) {
			if (c < 0) {
				double x2 = -c / a;
				корни.add(Math.sqrt(x2));
				корни.add(-Math.sqrt(x2));
			}
		}
		else if (a == 0 && b*c != 0) {
			double x = -c / b;
			корни.add(x);
		}
		else if ((a == 0 || b == 0) && c == 0) {
			всеѕодход€т = true;
		}
		else if (a*b == 0 && c != 0) {
			//нет корней
		}
	}
	
	private double найтиƒискременант(double a, double b, double c) {
		//D = bb - 4ac
		return b*b - 4*a*c;
	}
	
	List<Double> вывести орни() {
		return корни;
	}
}
