package fr.ensisa.hassenforder.transportation.server.network;

import java.io.IOException;
import java.io.Writer;
import java.net.Socket;
import java.util.List;

import fr.ensisa.hassenforder.transportation.server.NetworkListener;
import fr.ensisa.hassenforder.transportation.server.model.Pass;
import fr.ensisa.hassenforder.transportation.terminal.network.Protocol;

public class TerminalSession extends Thread {

	private Socket connection;
	private NetworkListener listener;

	public TerminalSession(Socket connection, NetworkListener listener) {
		this.connection = connection;
		this.listener = listener;
		if (listener == null)
			throw new RuntimeException("listener cannot be null");
	}

	public void close() {
		this.interrupt();
		try {
			if (connection != null)
				connection.close();
		} catch (IOException e) {
		}
		connection = null;
	}

	public boolean operate() {
		try {
			TerminalWriter writer = new TerminalWriter(connection.getOutputStream());
			TerminalReader reader = new TerminalReader(connection.getInputStream());
			reader.receive();

			switch (reader.getType()) {
			case 0:
				return false; // socket closed
			case Protocol.RQ_TERMINAL_CARD_DESC:
				doRequestCardDesc(writer, reader);
				break;
			case Protocol.RQ_TERMINAL_USE_TICKET:
				doRequestTerminalUse(writer, reader);
				break;
			}
			writer.send();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private void doRequestCardDesc(TerminalWriter writer, TerminalReader reader) {
		Pass pass = this.listener.terminalFetchPass(reader.getPassId());
		if (pass != null) {
			writer.createTerminalCardDescReplay(pass);
		} else {
			writer.createKoReplay();
		}
	}

	private void doRequestTerminalUse(TerminalWriter writer, TerminalReader reader) {

		if (this.listener.terminalUseTicket(reader.getPassId(), reader.getTicketId(), reader.getCount())) {
			writer.createTerminalUseReplay();
		} else {
			writer.createKoReplay();
		}
	}

	public void run() {
		while (true) {
			if (!operate())
				break;
		}
		try {
			if (connection != null)
				connection.close();
		} catch (IOException e) {
		}
	}
}
