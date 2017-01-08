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
	
	static HashMap<Long, Point> points = new HashMap<Long, Point>();
	
	static HashMap<Long, HashMap<Long, Long>> g = new HashMap<Long, HashMap<Long, Long>>();
	
	static void graph_read() throws FileNotFoundException
	{
		Scanner file = new Scanner(new FileReader("france.in"));
		String s;
		String[]sarr;
		int i = 0;
		while(file.hasNextLine()){
			i++;
			if (i % 1000000 == 0)
				System.out.println(i);
			s = file.nextLine();
			sarr = s.split("\\s");
			if (sarr[0].contains("v")){
				
				points.put(Long.parseLong(sarr[1]), new Point(sarr[2],sarr[3]));
				
			}
			else if (sarr[0].contains("a")){					
				long m,n,k;
				m = Long.parseLong(sarr[1]);
				n = Long.parseLong(sarr[2]);
				k = Long.parseLong(sarr[3]);
				if (g.get(m) == null){
					g.put(m ,new HashMap<Long, Long>());
					(g.get(m)).put(n,k);
				}
				else{
					(g.get(m)).put(n,k);
					//if (m == 150592286 && n == 246389249)
					///System.out.println((g.get(m)).get(n));
				}
				
				//System.out.println((g.get(m)).get(n));
			}	
		}
		//System.out.println((g.get(150592286)).get(246389249)); 
		
		file.close();
	}
	
	final static long INF = 2000000000;
	static Map d = new HashMap<Long, Long>(points.keySet().size() * 3);
	static Map p = new HashMap<Long, Long>(points.keySet().size() * 3);
	
	
	
	static Comparator<Pair> comparator = new Comparator<Pair>() {
		@Override
		public int compare(Pair o1, Pair o2) {
			if( o1.dist < o2.dist ){
				return -1;
			}
			if( o1.dist > o2.dist ){
				return 1;
			}
			return 0;
		}
	};

	static Queue<Pair> q = new PriorityQueue<Pair>(comparator);
	
	static void Dijkstra(long s)
	{
		int n =  points.keySet().size();
		//vector < vector < pair<int,int> > > g (n);
		//long s = 412523641L; // стартовая вершина
		//vector<int> d (n, INF),  p (n);
		for (long x:points.keySet())
		{
			d.put(x, INF);
		}
		
		HashMap<Long, Long> list_incid;
		d.put(s,0L);
		q.add(new Pair((long)d.get(s), s));
		
		
		long len;
		long curr;
		long v;
		while (!q.isEmpty()) {
			v = q.peek().id;
			curr = q.peek().dist;
			q.remove();
			if (curr > (long)d.get(v))
				continue;
			list_incid = g.get(v);
			try{
				for (long to : list_incid.keySet()) 
				{
					len = list_incid.get(to);
					if ((long)d.get(v) + len < (long)d.get(to)) {
							//q.remove(new Pair((long)d.get(to), to));
							d.put(to, (long)d.get(v) + len);
							p.put(to, v);
							q.offer(new Pair((long)d.get(to), to));
					}
				}
			}catch(Throwable t){
				//System.out.println(v+"!!!");
			}
		}
	}
	
	static void print_path_from_to(long s, long to)
	{
		long curr = to;
		while(curr != s)
		{	
			System.out.println(curr);
			curr = (long)p.get(curr);
			
		}
		System.out.println(s);
	}
	
	static void print_reachable_points(double t, long s) throws FileNotFoundException, UnsupportedEncodingException
	{
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
	
	
	public static void Dijkstra_to_file() throws IOException
	{
		FileWriter writer = new FileWriter("ready_dijkstra_distance.in");
		FileWriter writer1 = new FileWriter("ready_dijkstra_pathTo.in");
		int i = 0;
		
		for (long x: points.keySet())
		{
			writer.write(x + " " + (long)d.get(x) + '\n');
			System.out.println("d" + ++i);
		}
		
		writer.flush();
		writer.close();
		
		i = 0;
		for (Object x: p.keySet())
		{
			writer1.write(x + " " + (long)p.get(x) + '\n');
			System.out.println("p" + ++i);
		}
		writer1.flush();
		writer1.close();
	}
	
	public static void Dijkstra_from_file() throws FileNotFoundException
	{
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
			//System.out.println(x + " " + dist);
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
	
	public static void calculate_reachable_points1(long t)
	{
		long time = t * 1000 * 3600;
		
		int N = 0;
		
		for (long x: points.keySet())
		{
			if ((long)d.get(x) == time)
				N++;
		}
		
		System.out.println("Number of reachable points in " + t + " hour is: " + N);
	}
	
	public static void main(String []args) throws IOException
	{
		long s = 382076L;//122640L-idf;//382076L - france;//289880509L - malta;
		//long to = 246154693L;
		
		//double rt = 3820706.0;
		//System.out.println(s == rt);
		graph_read(); //***************
		//Dijkstra(s);

		//System.out.println("Happy New Year!!!");
		//System.out.println(points.keySet().size());
		//System.out.println(p.keySet().size());
		//System.out.println(d.keySet().size());
	    //Dijkstra_to_file();
	    Dijkstra_from_file();//*************
		//print_reachable_points(0.25, s);
		//print_path_from_to(s,to);
	    calculate_reachable_points1(2L);//***********
		
		//System.out.println((points.get(1509529134L).y));
		//System.out.println((g.get(150592286L)).get(246389249L)); 
		
	}
		/*Scanner file = new Scanner(new FileReader("malta.in"));
		
		int i = 0;
		while(file.hasNextLine())
		{
			String s;
			i++;
			if (i == 200){
				s = file.nextLine();
				System.out.println(s);
				String[] sarr = s.split ("\\s");  
				System.out.println(sarr[3]);
				//System.out.println(Float.parseFloat((l.insert(s.length() - 6, '.')).toString()));
				Point one = new Point(sarr[2], sarr[3]);
				System.out.println(one.y);
		     }	
			s = file.nextLine();
			
		}
		file.close();*/
}
