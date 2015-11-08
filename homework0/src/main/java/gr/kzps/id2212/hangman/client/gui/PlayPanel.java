package gr.kzps.id2212.hangman.client.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import gr.kzps.id2212.hangman.client.Connection;
import gr.kzps.id2212.hangman.client.RecvWorker;
import gr.kzps.id2212.hangman.client.SendWorker;
import gr.kzps.id2212.hangman.general.Alphabet;
import gr.kzps.id2212.hangman.general.OpCodes;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayPanel extends JPanel {

	private static final Logger LOG = LogManager.getLogger(PlayPanel.class);

	private static final long serialVersionUID = -8656315627648253460L;

	private Connection connection;
	private JTextField jtxt_notifications;
	private JLabel jl_win;
	private JLabel jl_lose;
	private JLabel jl_score;
	private JLabel jl_lifes;
	private JLabel jl_w_guess;
	private JTextField jtxf_score;
	private JTextField jtxf_lifes;
	private JTextField jtxf_hint;
	private JTextField jtxf_guess;
	private JButton jbtn_replay;
	private JButton jbtn_guess;
	private JTextField[] alphabet;

	private byte[] hint;
	private Integer score;
	private Integer lifes;
	private SendWorker sendWrk;
	private RecvWorker recvWrk;

	public PlayPanel(Connection connection, JTextField jtxt_notifications,
			byte[] initialHint) {
		super();
		this.connection = connection;
		this.jtxt_notifications = jtxt_notifications;
		this.hint = initialHint;
		this.score = 0;
		this.lifes = 10;
		alphabet = new JTextField[26];

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		create();
	}

	private void create() {
		JPanel jp_hint = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel jp_guess = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel jp_status = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel jp_msgs = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel jp_buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel jp_alphabet = new JPanel(new FlowLayout(FlowLayout.CENTER));

		add(jp_hint);
		add(jp_alphabet);
		add(jp_guess);
		add(jp_status);
		add(jp_msgs);
		add(jp_buttons);

		JLabel jl_hint = new JLabel("Hint:");

		jtxf_hint = new JTextField();
		jtxf_hint.setText(new String(hint));
		jtxf_hint.setEditable(false);
		jtxf_hint.setColumns(hint.length);

		jp_hint.add(jl_hint);
		jp_hint.add(jtxf_hint);

		JLabel jl_alpha = new JLabel("History");
		createAlphabet();

		for (int i = 0; i < alphabet.length; i++) {
			jp_alphabet.add(alphabet[i]);
		}

		JLabel jl_guess = new JLabel("Guess: ");

		jtxf_guess = new JTextField();
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
				jl_w_guess.setVisible(false);
			}
		});

		jp_guess.add(jl_guess);
		jp_guess.add(jtxf_guess);

		jl_score = new JLabel("Score: ");
		jtxf_score = new JTextField();
		jtxf_score.setEditable(false);
		jtxf_score.setText(Integer.toString(score));
		jtxf_score.setColumns(3);

		jl_lifes = new JLabel("Lifes: ");
		jtxf_lifes = new JTextField();
		jtxf_lifes.setEditable(false);
		jtxf_lifes.setText(Integer.toString(lifes));
		jtxf_lifes.setColumns(3);

		jp_status.add(jl_score);
		jp_status.add(jtxf_score);
		jp_status.add(jl_lifes);
		jp_status.add(jtxf_lifes);

		jl_w_guess = new JLabel();
		jl_w_guess.setVisible(false);
		jl_w_guess.setText("Wrong guess. Try again");
		jl_w_guess.setForeground(Color.ORANGE);

		JLabel jl_guess_empty = new JLabel();
		jl_guess_empty.setVisible(false);
		jl_guess_empty.setText("Guess cannot be empty");
		jl_guess_empty.setForeground(Color.RED);

		jl_win = new JLabel();
		jl_win.setVisible(false);
		jl_win.setText("Congratulations, you win!");
		jl_win.setForeground(Color.GREEN);

		jl_lose = new JLabel();
		jl_lose.setVisible(false);
		jl_lose.setText("Sorry, you lose!");
		jl_lose.setForeground(Color.RED);

		jp_msgs.add(jl_w_guess);
		jp_msgs.add(jl_guess_empty);
		jp_msgs.add(jl_win);
		jp_msgs.add(jl_lose);

		jbtn_guess = new JButton();
		jbtn_guess.setText("Guess");
		jbtn_guess.setEnabled(true);

		jbtn_guess.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// If guess is empty do nothing
				if (jtxf_guess.getText().equals("")) {
					jl_guess_empty.setVisible(true);
				} else {
					jl_guess_empty.setVisible(false);

					// Send guess to server
					String guess = jtxf_guess.getText();
					byte[] request = new byte[guess.getBytes().length + 1];
					request[0] = OpCodes.GUESS;

					System.arraycopy(guess.getBytes(), 0, request, 1,
							guess.getBytes().length);

					sendWrk = new SendWorker(connection, request,
							jtxt_notifications);
					sendWrk.execute();

					// Receive response

					recvWrk = new RecvWorker(connection, jtxt_notifications);

					recvWrk.addPropertyChangeListener(new PropertyChangeListener() {

						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							if (evt.getPropertyName().equals("state")) {
								if ((SwingWorker.StateValue) evt.getNewValue() == SwingWorker.StateValue.DONE) {
									try {
										byte[] response = recvWrk.get();
										// Change colour to history panel
										// Only if player proposed a single letter
										if (guess.length() == 1) {
											if (response[0] == OpCodes.G_GUESS) {
												Integer idx = Alphabet
														.getPosition(guess);
												alphabet[idx]
														.setBackground(Color.GREEN);
											} else if (response[0] == OpCodes.W_GUESS) {
												Integer idx = Alphabet
														.getPosition(guess);
												alphabet[idx]
														.setBackground(Color.RED);
											}
										}

										// Handle the response based on the OP
										// code
										handleResponse(response);
									} catch (InterruptedException e) {
										e.printStackTrace();
									} catch (ExecutionException e) {
										e.printStackTrace();
									}
								}
							}
						}
					});

					recvWrk.execute();
				}
			}
		});

		jbtn_replay = new JButton();
		jbtn_replay.setText("Play again");
		jbtn_replay.setVisible(false);

		jbtn_replay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetAlphabet();

				byte[] message = new byte[] { OpCodes.PL_AGAIN };
				sendWrk = new SendWorker(connection, message,
						jtxt_notifications);
				sendWrk.execute();

				recvWrk = new RecvWorker(connection, jtxt_notifications);

				recvWrk.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals("state")) {
							if ((SwingWorker.StateValue) evt.getNewValue() == SwingWorker.StateValue.DONE) {
								try {
									byte[] response = recvWrk.get();

									handleResponse(response);
								} catch (InterruptedException ex) {
									ex.printStackTrace();
								} catch (ExecutionException ex) {
									ex.printStackTrace();
								}
							}
						}

					}
				});

				recvWrk.execute();
			}
		});

		JButton jbtn_exit = new JButton();
		jbtn_exit.setText("Exit");
		jbtn_exit.setEnabled(true);

		jbtn_exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				byte[] message = new byte[] { OpCodes.CLOSE };
				sendWrk = new SendWorker(connection, message,
						jtxt_notifications);
				sendWrk.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals("state")) {
							if ((SwingWorker.StateValue) evt.getNewValue() == SwingWorker.StateValue.DONE) {
								connection.close();
								jtxt_notifications.setText("Exiting...");
								System.exit(-1);
							}
						}
					}
				});
				sendWrk.execute();
			}
		});

		jp_buttons.add(jbtn_guess);
		jp_buttons.add(jbtn_replay);
		jp_buttons.add(jbtn_exit);
	}

	private void handleResponse(byte[] response) {
		byte opCode = response[0];
		byte[] rest = Arrays.copyOfRange(response, 1, response.length);

		LOG.debug("OP code: {}", opCode);
		LOG.debug("Rest: {}", new String(rest));

		if (opCode == OpCodes.WIN) {
			Byte scoreB = rest[0];
			Integer score = scoreB.intValue();
			jtxf_score.setText(Integer.toString(score));

			jl_win.setVisible(true);
			jbtn_guess.setVisible(false);
			jbtn_replay.setVisible(true);
		} else if (opCode == OpCodes.LOST) {
			Byte scoreB = rest[0];
			Integer score = scoreB.intValue();
			jtxf_score.setText(Integer.toString(score));
			jtxf_lifes.setText("0");

			jl_lose.setVisible(true);
			jbtn_guess.setVisible(false);
			jbtn_replay.setVisible(true);
		} else if (opCode == OpCodes.W_GUESS) {
			Byte lossB = rest[0];
			Integer loss = lossB.intValue();
			jtxf_lifes.setText(Integer.toString(loss));
			jl_w_guess.setVisible(true);
		} else if (opCode == OpCodes.G_GUESS) {
			String newPattern = new String(rest);
			jtxf_hint.setText(newPattern);
		} else if (opCode == OpCodes.PL_AGAIN) {
			String pattern = new String(rest);
			jtxf_hint.setText(pattern);
			jtxf_hint.setColumns(pattern.length());
			jtxf_guess.setColumns(pattern.length());

			jtxf_lifes.setText("10");

			jbtn_replay.setVisible(false);
			jbtn_guess.setVisible(true);

			jl_lose.setVisible(false);
			jl_win.setVisible(false);
		} else if (opCode == OpCodes.UNKNOWN) {
			LOG.info("Received unknown command from the server");
		} else {
			LOG.info("Run for your lifes!");
		}
	}

	private void resetAlphabet() {
		for (int i = 0; i < alphabet.length; i++) {
			alphabet[i].setBackground(Color.WHITE);
		}
	}

	private void createAlphabet() {
		for (int i = 0; i < alphabet.length; i++) {
			alphabet[i] = new JTextField();
			alphabet[i].setText(Alphabet.getLetter(i));
			alphabet[i].setVisible(true);
			alphabet[i].setEditable(false);
		}
	}
}
