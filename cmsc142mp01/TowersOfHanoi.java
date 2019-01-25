package cmsc142mp01;

import java.time.Duration;
import java.time.Instant;

public class TowersOfHanoi {
	private String[] poleNames;
	private TOHDelegate delegate;

	public interface TOHDelegate {
		void println(int id, int pole1, int pole2);
	}

	public TowersOfHanoi(TOHDelegate delegate) {
		initialize(delegate);
	}

	private void initialize(TOHDelegate delegate) {
		this.delegate = delegate;
		if (poleNames == null) {
			String[] names = {"a", "b", "c"};
			this.poleNames = names;
		} else
			this.poleNames = poleNames;
	}

	public long solve(int n) {

		// Write headers
		delegate.println(0, 1, 3);

		// Get time started
		Instant start = Instant.now(); // Java 8 new thread-safe method

		// Do solving
		_solve(n, 1, 2, 3);

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
	private void _solve(int n, int fromPole, int auxPole, int toPole) {
		if (n == 1) {
			delegate.println(1, fromPole, toPole); // Move last disc to target pole
		} else {
			_solve(n-1, fromPole, toPole, auxPole); // Move top discs to aux
			delegate.println(n, fromPole, toPole); // Move bottom disc to target pole
			_solve(n-1, auxPole, fromPole, toPole); // Move top discs back to source pole
		}
	}
}
