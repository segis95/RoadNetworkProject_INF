package test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeMap;




public class Indata {
	
	static HashMap<Long, Point> points = new HashMap<Long, Point>();
	
	static HashMap<Long, HashMap<Long, Long>> g = new HashMap<Long, HashMap<Long, Long>>();
	
	static void graph_read() throws FileNotFoundException
	{
		Scanner file = new Scanner(new FileReader("malta.in"));
		String s;
		String[]sarr;
		while(file.hasNextLine()){
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
	static Map d = new HashMap<Long, Long>();
	static Map p = new HashMap<Long,Long>();
	
	
	
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
		while (!q.isEmpty()) {
			long v = q.peek().id;
			q.remove();
			list_incid = g.get(v);
			try{
				for (long to : list_incid.keySet()) 
				{
					len = list_incid.get(to);
					if ((long)d.get(v) + len < (long)d.get(to)) {
							q.remove(new Pair((long)d.get(to), to));
							d.put(to, (long)d.get(v) + len);
							p.put(to, v);
							q.add(new Pair((long)d.get(to), to));
					}
				}
			}catch(Throwable t){
				System.out.println(v+"!!!");
			}
		}
	}
	
	static void print_path_from_to(long s, long to)
	{
		long curr = to;
		while(curr != s)
		{
			Point po = points.get(curr);
			System.out.println("[" + po.y + ", " + po.x + "]" + ",");
			curr = (long)p.get(curr);
		}
		System.out.println(s);
	}
	public static void main(String []args) throws FileNotFoundException
	{
		long s = 246149772L;
		long to = 246154693L;

		graph_read();
		Dijkstra(s);
		print_path_from_to(s,to);
		
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
