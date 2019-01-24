package cmsc142mp01;

import cmsc142mp01.TowersOfHanoi.TOHDelegate;

public class TOHRunnable implements Runnable, TOHDelegate {
	
	public interface TOHRDelegate {
		void updateStatus(String status, int discs);
		void finished(long[] elapsed, int discs);
	}
	
	private TOHRDelegate delegate;
	private int discs;
	
	public TOHRunnable(TOHRDelegate delegate, int discs) {
		super();
		this.delegate = delegate;
		this.discs = discs;
	}
	
	@Override
	public void println(String x) {
		delegate.updateStatus(x, discs);
	}
	
	@Override
    public void run() {
		TowersOfHanoi problem = new TowersOfHanoi(this);
		long[] elapsed = new long[3];
		delegate.updateStatus("RUN 1", discs);
		elapsed[0] = problem.solve(discs);
		delegate.updateStatus("RUN 2", discs);
		elapsed[1] = problem.solve(discs);
		delegate.updateStatus("RUN 3", discs);
		elapsed[2] = problem.solve(discs);
		delegate.finished(elapsed, discs);
    }
	
}