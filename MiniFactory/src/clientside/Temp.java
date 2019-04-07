package clientside;

import java.util.TreeMap;
import java.util.TreeSet;

public class Temp {
	private static double x1, x2;
	private static double y1, y2;
	private static double vx1, vx2;
	private static double vy1, vy2;
	private static double r2;
	private static int Radius = 1;
	
	public static void main(String[] args) {
		x1 = 2;
		x2 = 0;
		y1 = 0;
		y2 = 0;
		
		vx1 = 0;
		vy2 = 0;
		vy1 = 0;
		vy2 = 0;
		
		r2 = Radius*2;
		
		System.out.println(check());
	}
	
	private static double check() {
		double x = x1 - x2;
		double y = y1 - y2;
		
		double vx = vx1 - vx2;
		double vy = vy1 - vy2;
		
		x -= vx;
		y -= vy;
		
		
		double a = vx*vx + vy*vy;
		double b = 2*(x*vx + y*vy);
		double c = x*x + y*y - r2*r2;
		
		if (a != 0) {
			System.out.print(a);
			System.out.print("t2");
			System.out.print(" ");
		}
		if (b != 0) {
			System.out.print(" ");
			System.out.print(b > 0 ? "+ "+b : "- "+-b);
			System.out.print("t");
			System.out.print(" ");
		}
		if (c != 0) {
			System.out.print(c > 0 ? "+ "+c : "- "+-c);
			System.out.print(" ");
		}
		System.out.println("= 0");
		
		Function f = new Function(a, b, c);
		
		if (f.всеѕодход€т) return 0;
		if (f.вывести орни().size() > 0) {
			if (f.вывести орни().get(0) >= 0) {
				return f.вывести орни().get(0) - 1;
			}
		}
		
		return -1;
	}
}
