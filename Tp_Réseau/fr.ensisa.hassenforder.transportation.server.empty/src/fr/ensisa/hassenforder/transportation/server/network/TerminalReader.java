package fr.ensisa.hassenforder.transportation.server.network;

import java.io.InputStream;

import fr.ensisa.hassenforder.network.BasicAbstractReader;
import fr.ensisa.hassenforder.transportation.terminal.network.Protocol;

public class TerminalReader extends BasicAbstractReader {
	private long passId;
	private String ticketId;
	private int count;

	public TerminalReader(InputStream inputStream) {
		super(inputStream);
	}

	public void receive() {
		type = readInt();
		switch (type) {
		case 0:
			break;
		case Protocol.RQ_TERMINAL_CARD_DESC:
			passId = readLong();
			break;
		case Protocol.RQ_TERMINAL_USE_TICKET:
			this.passId = readLong();
			this.ticketId = readString();
			this.count = readInt();
			break;
		}
	}

	protected long getPassId() {
		return this.passId;
	}

	protected String getTicketId() {
		return this.ticketId;
	}

	protected int getCount() {
		return this.count;
	}
}
