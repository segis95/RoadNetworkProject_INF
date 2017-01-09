package test;

public class Coordinate {
	
	//This class describes the point on the edge
	//Edge is represented by the names of the first and the last points
	///x describes the precise position of this edge in millisecond
	long from;
	long to;
	long x;
	
	Coordinate(long x1, long x2, long x3){
		from = x1;
		to = x2;
		x = x3;
	}

	//we have to override these methods to compare 
	//Coordinates correctly
	@Override
	public int hashCode() {
	    final long prime = 31;
	    long result = 1;
	    result = prime * result + from;
	    result = prime * result + to;
	    result = prime * result + x;
	    return (int)result;
	}
	

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    Coordinate other = (Coordinate) obj;
	    if (from != other.from)
	        return false;
	    if (to != other.to)
	        return false;
	    if (x != other.x)
	        return false;
	    return true;
	}
	
	
	
}
