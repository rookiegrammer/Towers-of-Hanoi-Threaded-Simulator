package cmsc142mp01;

import java.time.Duration;
import java.time.Instant;

public class TowersOfHanoi {
	private String[] poleNames;
	private TOHDelegate delegate;
	
	public interface TOHDelegate {
		void println(String x);
	}
	
	public TowersOfHanoi(TOHDelegate delegate) {
		initialize(delegate, null);
	}
	
	public TowersOfHanoi(TOHDelegate delegate, String[] poleNames) {
		initialize(delegate, poleNames);
	}
	
	private void initialize(TOHDelegate delegate, String[] poleNames) {
		this.delegate = delegate;
		if (poleNames == null) {
			String[] names = {"a", "b", "c"};
			this.poleNames = names;
		} else 
			this.poleNames = poleNames;
	}
	
	public long solve(int n) {
		return solve(n, poleNames[0], poleNames[2]);
	}
	
	public long solve(int n, String fromPole, String toPole) {
		
		// Find the auxiliary pole
		String auxPole = null;
		for (int i = 0; i < poleNames.length; i++) {
			String thisPole = poleNames[i];
			if (!thisPole.equals(fromPole) && !thisPole.equals(toPole)) {
				auxPole = thisPole;
				break;
			}
		}
		if (auxPole == null) return 0L;
		
		// Write headers
		delegate.println("SOLVE "+fromPole+" -> "+toPole+"");
		
		// Get time started
		Instant start = Instant.now(); // Java 8 new thread-safe method
		
		// Do solving
		_solve(n, fromPole, auxPole, toPole);
		
		// Get time finished
		Instant finish = Instant.now();
		 
		// Get running time
	    long timeElapsed = Duration.between(start, finish).toMillis(); // Java 8 new thread-safe method
		
		return timeElapsed;
	}
	
	// Note to self:
	// The program should not rely on writing to command line or file
	// Remember 2^10000-1 is big data :)
	// You should use a GUI instead that displays current move only...
	private void _solve(int n, String fromPole, String auxPole, String toPole) {
		if (n == 1) {
			delegate.println("DISC 1:"+fromPole+"->"+toPole); // Move last disc to target pole
		} else {
			_solve(n-1, fromPole, toPole, auxPole); // Move top discs to aux
			delegate.println("DISC "+n+":"+fromPole+"->"+toPole); // Move bottom disc to target pole
			_solve(n-1, auxPole, fromPole, toPole); // Move top discs back to source pole
		}
	}
}
