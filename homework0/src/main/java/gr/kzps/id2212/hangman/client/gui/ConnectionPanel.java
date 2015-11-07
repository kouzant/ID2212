package gr.kzps.id2212.hangman.client.gui;

import gr.kzps.id2212.hangman.client.Connection;
import gr.kzps.id2212.hangman.client.RecvWorker;
import gr.kzps.id2212.hangman.client.SendWorker;
import gr.kzps.id2212.hangman.general.OpCodes;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class ConnectionPanel extends JPanel {

	private static final long serialVersionUID = -7068265294171409209L;

	private JTextField jtxf_ipAddress;
	private JTextField jtxf_port;
	private JTextField jtxf_username;
	private JButton jbtn_connect;
	private JButton jbtn_reset;
	private Connection connection;
	private JTextField jtxt_notifications;

	public ConnectionPanel(Connection connection, JTextField jtxt_notifications) {
		this.connection = connection;
		this.jtxt_notifications = jtxt_notifications;

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		create();
	}

	private void create() {
		JPanel jp_ipaddress = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel jp_hostPort = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel jp_username = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel jp_buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));

		add(jp_ipaddress);
		add(jp_hostPort);
		add(jp_username);
		add(jp_buttons);

		JLabel jlb_ipAddress = new JLabel("IP address");

		jtxf_ipAddress = new JTextField();
		jtxf_ipAddress.setEditable(true);
		jtxf_ipAddress.setColumns(10);
		jtxf_ipAddress.setText("localhost");

		jp_ipaddress.add(jlb_ipAddress);
		jp_ipaddress.add(jtxf_ipAddress);

		JLabel jlb_port = new JLabel("Port");
		jtxf_port = new JTextField();
		jtxf_port.setEditable(true);
		jtxf_port.setColumns(4);
		;
		jtxf_port.setText("8080");

		jp_hostPort.add(jlb_port);
		jp_hostPort.add(jtxf_port);

		JLabel jlb_username = new JLabel("Username");
		jtxf_username = new JTextField();
		jtxf_username.setEditable(true);
		jtxf_username.setColumns(10);
		jtxf_username.setText("username");

		jtxf_username.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				jtxf_username.setText("");
			}
		});

		jp_username.add(jlb_username);
		jp_username.add(jtxf_username);

		jbtn_connect = new JButton();
		jbtn_connect.setEnabled(true);
		jbtn_connect.setText("Start");
		jbtn_connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Connect and go to other window
				String ipAddress = jtxf_ipAddress.getText();
				Integer port = Integer.parseInt(jtxf_port.getText());

				jtxt_notifications.setText("Connecting...");

				connection.connect(ipAddress, port);

				// Send message to server to begin

				byte[] message = new byte[jtxf_username.getText().length() + 1];
				// Op code to create a new game
				message[0] = OpCodes.CREATE;

				System.arraycopy(jtxf_username.getText().getBytes(), 0,
						message, 1, jtxf_username.getText().getBytes().length);
				
				SendWorker sendwrk = new SendWorker(connection, message,
						jtxt_notifications);
				
				sendwrk.execute();
				
				RecvWorker recvWorker = new RecvWorker(connection, jtxt_notifications);
				recvWorker.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals("state")) {
							if ((SwingWorker.StateValue) evt.getNewValue() == SwingWorker.StateValue.DONE) {
								try {
									byte[] response = recvWorker.get();
									
									System.out.println("Response: " + new String(response));
									jp_ipaddress.setVisible(false);
									jp_hostPort.setVisible(false);
									jp_username.setVisible(false);
									jp_buttons.setVisible(false);

									add(new PlayPanel(connection, jtxt_notifications, response));
									
								} catch (InterruptedException e) {
									e.printStackTrace();
								} catch (ExecutionException e) {
									e.printStackTrace();
								}
							}
						}
						
					}
				});
				
				recvWorker.execute();
			}
		});

		jp_buttons.add(jbtn_connect);

		jbtn_reset = new JButton();
		jbtn_reset.setEnabled(true);
		jbtn_reset.setText("Reset");
		jbtn_reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jtxf_ipAddress.setText("localhost");
				jtxf_port.setText("8080");
				jtxf_username.setText("username");
			}
		});

		jp_buttons.add(jbtn_reset);
	}

	public JTextField getJtxf_ipAddress() {
		return jtxf_ipAddress;
	}

	public JTextField getJtxf_port() {
		return jtxf_port;
	}

	public JTextField getJtxf_username() {
		return jtxf_username;
	}

	public JButton getJbtn_connect() {
		return jbtn_connect;
	}

	public JButton getJbtn_reset() {
		return jbtn_reset;
	}
}