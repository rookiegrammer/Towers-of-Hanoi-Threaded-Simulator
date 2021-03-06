package cmsc142mp01;

import cmsc142mp01.TowersOfHanoi.TOHDelegate;

public class TOHRunnable implements Runnable, TOHDelegate {

	public interface TOHRDelegate {
		void updateStatus(int discs, int id, int pole1, int pole2);
		void finished(long[] elapsed, int discs);
	}

	private TOHRDelegate delegate;
	private int discs;
	private TowersOfHanoi problem;
	private boolean single;
	private int run;

	public TOHRunnable(TOHRDelegate delegate, int discs, boolean silently, int identify) {
		super();
		this.delegate = delegate;
		this.discs = discs;
		this.problem = new TowersOfHanoi(this, silently);

		if (identify == 0){
			single = false;
		} else {
			single = true;
			run = identify;
		}
	}

	@Override
	public void println(int id, int pole1, int pole2) {
		delegate.updateStatus(discs, id, pole1, pole2);
	}

	@Override
    public void run() {
		long[] elapsed;
		if (single) {
			elapsed = new long[1];
			delegate.updateStatus(discs, -run, 0, 0);
			elapsed[0] = problem.solve(discs);
		} else {
			elapsed = new long[3];
			delegate.updateStatus(discs, -1, 0, 0);
			elapsed[0] = problem.solve(discs);
			delegate.updateStatus(discs, -2, 0, 0);
			elapsed[1] = problem.solve(discs);
			delegate.updateStatus(discs, -3, 0, 0);
			elapsed[2] = problem.solve(discs);
		}
		delegate.finished(elapsed, discs);
    }

}
