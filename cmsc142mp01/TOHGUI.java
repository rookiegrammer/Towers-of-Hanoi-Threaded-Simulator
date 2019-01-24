package cmsc142mp01;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileSystemView;

import cmsc142mp01.TOHRunnable.TOHRDelegate;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TOHGUI implements TOHRDelegate {

	private JFrame frmTimeMyTowers;
	private PrintWriter out;
	
	private String[] lineBuffer;
	
	private boolean isUpdating = false;
	private boolean needsUpdate = false;
	
	private int lineStart;
	
	private int lineBufferSize = 16;
	private int threadPoolSize = 8;
	
	private JTextField textFieldThreads;
	private JTextField textFieldLines;
	private JTextField textFieldNStart;
	private JTextField textFieldNSize;
	private JTextField textFieldNInterval;
	private JTextArea textAreaConsole;
	
	private Runnable updateTask = new Runnable() {
		
		@Override
		public void run() {
			int lineNow = lineStart;
			
			String console = "";
			for (int i=0; i<lineBufferSize; i++) {
				int line = (i+lineNow)%lineBufferSize;
				console += lineBuffer[line] + "\n";
			}
			textAreaConsole.setText(console);
			
			if (needsUpdate) {
				needsUpdate = false;
				EventQueue.invokeLater(updateTask);
			} else isUpdating = false;
		}
		
	};
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (System.getProperty("os.name").startsWith("Windows"))
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					TOHGUI window = new TOHGUI();
					window.frmTimeMyTowers.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TOHGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTimeMyTowers = new JFrame();
		frmTimeMyTowers.setTitle("Time My Towers Of Hanoi");
		frmTimeMyTowers.setBounds(100, 100, 450, 300);
		frmTimeMyTowers.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 20, 5, 20));
		frmTimeMyTowers.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lineBufferSize = Integer.parseInt(textFieldLines.getText());
				threadPoolSize = Integer.parseInt(textFieldThreads.getText());
				
				int start = Integer.parseInt(textFieldNStart.getText());
				int interval = Integer.parseInt(textFieldNInterval.getText());
				int stepSize = Integer.parseInt(textFieldNSize.getText());
				
				if (start <= 0 || stepSize <= 0 || stepSize > 0 && interval <= 0) return;
				
				System.out.println("OK Let's Go!");
				
				execute(start, interval, stepSize);
			}
		});
		panel.add(btnExecute);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		textFieldThreads = new JTextField(threadPoolSize+"");
		panel_1.add(textFieldThreads, BorderLayout.CENTER);
		textFieldThreads.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Threads ");
		panel_1.add(lblNewLabel, BorderLayout.WEST);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EmptyBorder(0, 0, 5, 0));
		panel_1.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Lines Buffer ");
		panel_2.add(lblNewLabel_1, BorderLayout.WEST);
		
		textFieldLines = new JTextField(lineBufferSize+"");
		panel_2.add(textFieldLines, BorderLayout.CENTER);
		textFieldLines.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel_2.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_2 = new JLabel("Start, Interval , Step Size (N)");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblNewLabel_2, BorderLayout.NORTH);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EmptyBorder(5, 20, 5, 20));
		panel_3.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new GridLayout(1, 0, 0, 0));
		
		textFieldNStart = new JTextField();
		panel_4.add(textFieldNStart);
		textFieldNStart.setColumns(10);
		
		textFieldNInterval = new JTextField();
		panel_4.add(textFieldNInterval);
		textFieldNInterval.setColumns(10);
		
		textFieldNSize = new JTextField();
		panel_4.add(textFieldNSize);
		textFieldNSize.setColumns(10);
		
		textAreaConsole = new JTextArea();
		textAreaConsole.setEditable(false);
		frmTimeMyTowers.getContentPane().add(textAreaConsole, BorderLayout.CENTER);
	}
	
	private void execute(int start, int interval, int stepSize) {
		TOHGUI self = this;
		
		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		
		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue != JFileChooser.APPROVE_OPTION)
			return;
		
		File selectedFile = fileChooser.getSelectedFile();
		
		Thread branchedThread = new Thread() {
			public void run() {
				try {
					// Get file
					FileWriter fileWriter = new FileWriter(selectedFile);
					
					// Setup print writer
					out = new PrintWriter(fileWriter);
					lineBuffer = new String[lineBufferSize];
				    lineStart = 0;
				    
				    ExecutorService threadpool = Executors.newFixedThreadPool(threadPoolSize);
				    
				    for (int i=0; i<stepSize; i++) {
				    	int discs = start+i*interval;
				    	TOHRunnable runnable = new TOHRunnable(self, discs);
				    	threadpool.execute(runnable);
				    }
				    
				    threadpool.shutdown();
				    threadpool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
				    
				    // Close print writer
				    out.close();
				    
				} catch (IOException|InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		branchedThread.start();
	}
	
	private void println(String line) {
		lineBuffer[lineStart] = line;
		lineStart = (lineStart+1)%lineBufferSize;
		
		if (!isUpdating) {
			isUpdating = true;
			EventQueue.invokeLater(updateTask);
		} else needsUpdate = true;
	}

	@Override
	public void updateStatus(String status, int discs) {
		println("TOH "+discs+": "+status);
	}

	@Override
	public void finished(long[] elapsed, int discs) {
		println("TOH "+discs+": FINISHED "+elapsed[0]+" "+elapsed[1]+" "+elapsed[2]);
		out.println("TOH "+discs+": FINISHED "+elapsed[0]+" "+elapsed[1]+" "+elapsed[2]);
	}

}
