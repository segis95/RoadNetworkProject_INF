package test;

public class Point {
	
	//int id;
	float x;
	float y;
	Point(String s1, String s2)
	{
		
		//id = Integer.parseInt(s0);
		StringBuilder l = new StringBuilder(s1);
		StringBuilder s = new StringBuilder(s2);
		x = Float.parseFloat((l.insert(l.length() - 6, '.')).toString());
		y = Float.parseFloat((s.insert(s.length() - 6, '.')).toString());
	}
}
