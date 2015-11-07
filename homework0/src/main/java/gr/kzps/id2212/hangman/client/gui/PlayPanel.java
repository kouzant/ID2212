package gr.kzps.id2212.hangman.client.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import gr.kzps.id2212.hangman.client.Connection;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PlayPanel extends JPanel {
	private Connection connection;
	private JTextField jtxt_notifications;
	private byte[] hint;
	private Integer score;
	private Integer lifes;
	
	public PlayPanel (Connection connection, JTextField jtxt_notifications, byte[] initialHint) {
		super();
		this.connection = connection;
		this.jtxt_notifications = jtxt_notifications;
		this.hint = initialHint;
		this.score = 0;
		this.lifes = 10;
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		create();
	}
	
	private void create() {
		JPanel jp_hint = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel jp_guess = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel jp_status = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel jp_buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		add(jp_hint);
		add(jp_guess);
		add(jp_status);
		add(jp_buttons);
		
		JLabel jl_hint = new JLabel("Hint:");
		
		JTextField jtxf_hint = new JTextField();
		jtxf_hint.setText(new String(hint));
		jtxf_hint.setEditable(false);
		jtxf_hint.setColumns(hint.length);
		
		jp_hint.add(jl_hint);
		jp_hint.add(jtxf_hint);
		
		JLabel jl_guess = new JLabel("Guess: ");
		
		JTextField jtxf_guess = new JTextField();
		jtxf_guess.setEditable(true);
		jtxf_guess.setText("Guess");
		jtxf_guess.setColumns(jtxf_hint.getColumns());
		
		jtxf_guess.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				jtxf_guess.setText("");
			}
		});
		
		jp_guess.add(jl_guess);
		jp_guess.add(jtxf_guess);
		
		JLabel jl_score = new JLabel("Score: " + score);
		JLabel jl_lifes = new JLabel("Lifes: " + lifes);
		
		jp_status.add(jl_score);
		jp_status.add(jl_lifes);
		
		JButton jbtn_guess = new JButton();
		jbtn_guess.setText("Guess");
		jbtn_guess.setEnabled(true);
		
		// TODO Handle responses from the server
		
		JButton jbtn_exit = new JButton();
		jbtn_exit.setText("Exit");
		jbtn_exit.setEnabled(true);
		
		jbtn_exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				connection.close();
				jtxt_notifications.setText("Exiting...");
				System.exit(-1);
			}
		});
		
		jp_buttons.add(jbtn_guess);
		jp_buttons.add(jbtn_exit);
	}
	
	private void incrScore() {
		score++;
	}
	
	private void decrLifes() {
		lifes--;
	}
}
