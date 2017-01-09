package test;

public class Point {
	//This class represents a point of our graph
	//Each point is described by it's two coordinates on the map
	float x;
	float y;
	Point(String s1, String s2)
	{
		StringBuilder l = new StringBuilder(s1);
		StringBuilder s = new StringBuilder(s2);
		x = Float.parseFloat((l.insert(l.length() - 6, '.')).toString());//we know that 6 right chars represents the
		y = Float.parseFloat((s.insert(s.length() - 6, '.')).toString());//fractional part of a coordinate
	}
}
