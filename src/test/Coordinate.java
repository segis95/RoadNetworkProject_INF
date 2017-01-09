package test;

public class Coordinate {
	
	long from;
	long to;
	long x;
	
	Coordinate(long x1, long x2, long x3){
		from = x1;
		to = x2;
		x = x3;
	}

	
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
