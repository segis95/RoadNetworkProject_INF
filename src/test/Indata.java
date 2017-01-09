package test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;




public class Indata {
	
	//links names of the points with physical coordinates
	static HashMap<Long, Point> points = new HashMap<Long, Point>();
	
	//the representation of our graph
	static HashMap<Long, HashMap<Long, Long>> g = new HashMap<Long, HashMap<Long, Long>>();
	
	//Length of the longest edge in our graph
	public static long max_edge_len;
	
	//Reading the graph function
	static void graph_read() throws FileNotFoundException
	{
		System.out.println("Reading the graph from \"france.in\" in process...");
		long max_edge = 0;
		Scanner file = new Scanner(new FileReader("france.in"));//reader from file
		String s;
		String[]sarr;
		int i = 0;
		
		//reading loop
		while(file.hasNextLine()){
			i++;
			if (i % 1000000 == 0)
				System.out.println(i + " points read...");
			s = file.nextLine();
			sarr = s.split("\\s");//we separate the numbers in the line by spaces
			
			//we parse a vertex
			if (sarr[0].contains("v")){
				//we parse the line and add points; 
				//they are parsed from strings later in the constructor of Point
				points.put(Long.parseLong(sarr[1]), new Point(sarr[2],sarr[3]));
			}
			else if (sarr[0].contains("a")){	
				///we parse an edge
				long m,n,k;
				m = Long.parseLong(sarr[1]);
				n = Long.parseLong(sarr[2]);
				k = Long.parseLong(sarr[3]);
				
				//we find the longest edge in the graph
				if (k > max_edge)
					max_edge = k;
				
				//adding the edge to our graph
				if (g.get(m) == null){
					g.put(m ,new HashMap<Long, Long>());
					(g.get(m)).put(n,k);
				}
				else{
					(g.get(m)).put(n,k);

				}
			}	
		}
		
		file.close();
		
		//we set max edge value
		max_edge_len = max_edge;
	}
	
	//Infinity in terms of Dijkstra
	final static long INF = 2000000000;
	
	//represents the shortest distances to all points
	static Map<Long, Long> d = new HashMap<Long, Long>(points.keySet().size() * 3);
	
	//represents the previous point on the way from the source to a given point 
	static Map<Long, Long> p = new HashMap<Long, Long>(points.keySet().size() * 3);
	
	
	//priority queue needed for Dijkstra with the need comparator
	static Queue<Pair> q = new PriorityQueue<Pair>(Pair.comparator);
	
	//Dijkstra algorithm implementation for source s
	static void Dijkstra(long s)
	{
		System.out.println("Dijkstra : calculating shortest paths...*** may take up to 40 seconds");
		//we put shortest known distance to all points INF
		for (long x:points.keySet())
		{
			d.put(x, INF);
		}
		//auxiliary Map
		HashMap<Long, Long> list_incid;
		
		//We put distance to source 0
		d.put(s,0L);
		
		//adding pair to queue
		q.add(new Pair((long)d.get(s), s));
		
		//auxiliary variables
		long len;
		long curr;
		long v;
		
		//main loop
		while (!q.isEmpty()) {
			
			//we take a point from the top of our queue
			v = q.peek().id;
			curr = q.peek().dist;
			q.remove();
			
			//if this point is already not up to date(we know a shorter path)
			//We just omit it
			if (curr > (long)d.get(v))
				continue;
			
			//points incident with v
			list_incid = g.get(v);
			
			try{
				for (long to : list_incid.keySet()) //we check all incident points with v(call them to) 
				{
					//shortest known distance to to
					len = list_incid.get(to);
					
					//if we can make a relaxation we do it
					if ((long)d.get(v) + len < (long)d.get(to)) {
							d.put(to, (long)d.get(v) + len);
							p.put(to, v);
							q.offer(new Pair((long)d.get(to), to));
					}
				}
			}catch(Throwable t){
				//catches cases when list_incid is empty or null
			}
		}
	}
	
	//This method prints on the way from s in direction of any other point
	//if 
	static void print_path_from_to(long s, long to)
	{
		System.out.println("Points on the way from s to TO");
		if ((long)d.get(to) == INF)
		{
			System.out.println("This point is not reachable from s");
			return;
		}
		
		long curr = to;
		while(curr != s)
		{	
			System.out.println(curr);
			curr = (long)p.get(curr);
			
		}
		System.out.println(s);
	}
	
	//Method printing into a file all reachable points from source s in time <= t
	static void print_reachable_points(double t, long s) throws FileNotFoundException, UnsupportedEncodingException
	{
		System.out.println("Printing to file reachable points from " + s + " in time " + t);
		HashSet<Long> reachable = new HashSet<Long>();
		long dist;
		double time = t * 60 * 60 * 1000;
		
		
		for(long x: points.keySet()){
			dist = (long)d.get(x);
			if (dist != INF &&  dist <= time)
			{
				reachable.add(x);
			}
		}
		
		PrintWriter writer = new PrintWriter("points1.js", "UTF-8");	    
		
		writer.println("var plottedPoints = [\n");
		
		Point po;
		for (long x:reachable)
		{
			 po = points.get(x);
			writer.println("[" + po.y + ", " + po.x + "]" + ",");
		}
		
		writer.println("];\n\n\n\n");
		po = points.get(s);
		writer.println("var centralMarker = [" + po.y + ", " + po.x + "];");
		writer.close();
	}
	
	//This method saves the results of Dijkstra algorithm into two files
	public static void Dijkstra_to_file() throws IOException
	{
		FileWriter writer = new FileWriter("ready_dijkstra_distance.in");
		FileWriter writer1 = new FileWriter("ready_dijkstra_pathTo.in");

		System.out.println("Saving Dijkstra to file *** This may take about 1 min");
		for (long x: points.keySet())
		{
			writer.write(x + " " + (long)d.get(x) + '\n');
		}
		
		writer.flush();
		writer.close();
		

		for (Object x: p.keySet())
		{
			writer1.write(x + " " + (long)p.get(x) + '\n');

		}
		writer1.flush();
		writer1.close();
	}
	
	//This method initializes p and d from Dijkstra saved before into a file
	public static void Dijkstra_from_file() throws FileNotFoundException
	{
		System.out.println("Reading Dikstra from file *** This may take about 1 min");
		long x, dist, pathTo;
		String s[];
		String str;
		
		Scanner file = new Scanner(new FileReader("ready_dijkstra_distance.in"));
		
		
		while(file.hasNextLine())
		{
			str = file.nextLine();
			if (str == "")
				break;
			s = str.split(" ");
			x = Long.parseLong(s[0]);
			dist = Long.parseLong(s[1]);
			d.put(x,dist);
		}
		
		file.close();
		
		
		Scanner file1 = new Scanner(new FileReader("ready_dijkstra_pathTo.in"));
		
		while(file1.hasNextLine())
		{
			str = file1.nextLine();
			if (str == "")
				break;
			s = str.split(" ");
			x = Long.parseLong(s[0]);
			pathTo = Long.parseLong(s[1]);
			//System.out.println(x + " " + pathTo);
			p.put(x, pathTo);
		}
		file1.close();
	}
	
	//This method counts reachable Coordinates from s
	//in time t (FOR TASKS 1.1 and 1.2)
	public static void count_reachable_points(long t1, long s)
	{
		System.out.println("Calculating reachable points in time " + t1 + " min from point " + s);
		//boarders of the searching RING
		long time1 = t1 * 1000 * 60 - max_edge_len - 1000;
		long time2 = t1 * 1000 * 60;
		
		Set<Coordinate> reachable_coordinates = new HashSet<Coordinate>();
		
		long dist;
		for (long x : points.keySet())
		{
			dist = (long)d.get(x);
			if (dist > time1 && dist <= time2)//if point is in the RING of search
			{
				
				if (dist == time2)
					reachable_coordinates.add(new Coordinate(x,x,0));
				else{
					if (g.get(x) == null || g.get(x).keySet().isEmpty())//we omit it if there are no edges
						continue;
					else
						for (long y : g.get(x).keySet())//we check all edges coming from it
						{
							if (dist + g.get(x).get(y) > time2){
								reachable_coordinates.add(new Coordinate(x,y, dist + g.get(x).get(y) - time2));
							}
						}
					}
			}
		}
		
		//Uncomment to print all reached point
		/*
		for (Coordinate x:reachable_coordinates )
		{
			System.out.println("[" + points.get(x.from).y + "," + points.get(x.from).x + "],");
		}
		*/
		
		System.out.println("Number of points accessible from sourse in " + t1 + " min(s) is " + reachable_coordinates.size());
		
		//System.out.println("   " + reachable_coordinates.size());
	}
	
	
	public static void count_reachable_points(long t1, long t2, long s)
	{
		System.out.println("Calculating reachable points in time " + t1 + " min from point " + s + " if the destination is not less than in " + t2 + " min");
		Set<Coordinate> reachable_coordinates = new HashSet<Coordinate>();
		
		//defines the distance in ms that we are looking for
		long time1 = t1 * 1000 * 60;
		
		//defines the search RING
		long time2 = t2 * 1000 * 60;
		long time3 = t2 * 1000 * 60 + max_edge_len + 100000;
		
		//auxiliary variables
		long dist, distance;
		long current_point, previous_point;
		
		for (long x : points.keySet())
		{
			dist = (long)d.get(x);
			if (dist >= time2 && dist < time3)//check if the points is in the search RING
			{	
				current_point = x;
				previous_point = (long)p.get(current_point);
				distance = (long)d.get(current_point);
				
				//we pass by it's ancestors while we don't achieve the border of search RING or the source itself
				while(distance >= time1 && current_point != s)
				{ 
					if ((long)d.get(previous_point) <= time1){//if we found such an edge that intersects with our needed distance time1
						reachable_coordinates.add(new Coordinate(previous_point, current_point, time1 - (long)d.get(previous_point) ));
						break;
					}
					else//make one more step
					{
						current_point = previous_point;
						if (current_point == s)
							break;
						previous_point = (long)p.get(current_point);
						distance = (long)d.get(current_point);
					}
				}
			}
		}
		
		//Uncomment to print all points reached
		/*
		for (Coordinate x:reachable_coordinates )
		{
			System.out.println("[" + points.get(x.from).y + "," + points.get(x.from).x + "],");
		}
		*/
		System.out.println("Number of reachable points from source " + s + " in " + t1 + " min(s) (if destination is more than in " + t2 +" min(s)) is: " + reachable_coordinates.size());
		
		//System.out.println("   " + reachable_coordinates.size());


		
	}
	
	public static void main(String []args) throws IOException
	{
		//Starting point
		long s = 2008900362L;
		
		//reading the graph
		graph_read(); 
		
		//calculate shortest paths from s
		Dijkstra(s);

		//Write results to file is we need
	    //Dijkstra_to_file();
		
		//If we had saved our Dijkstra results to a file earlier
		//we can read it from file directly not to remake calculations
	    //Dijkstra_from_file();

	    //Prints reachable points for the question 3 and their overall number
	    //**if we uncomment the printing part in it's code!!!
	     
	    count_reachable_points(60, s);

	    
	    //Used to plot the graphs in part A
	    /*
	     for (long i = 1; i < 31; i++)
	    	count_reachable_points(120, 120 + 12L * i ,s);//***********
		*/
		
	}

}
