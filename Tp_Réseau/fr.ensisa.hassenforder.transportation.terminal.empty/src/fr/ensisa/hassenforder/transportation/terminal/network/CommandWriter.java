package fr.ensisa.hassenforder.transportation.terminal.network;

import java.io.OutputStream;

import fr.ensisa.hassenforder.network.BasicAbstractWriter;
import fr.ensisa.hassenforder.transportation.terminal.network.Protocol;

public class CommandWriter extends BasicAbstractWriter {

	public CommandWriter(OutputStream outputStream) {
		super(outputStream);
	}

	protected void createTerminalCardDescRequest(Long passId) {
		writeInt(Protocol.RQ_TERMINAL_CARD_DESC);
		writeLong(passId);
	}

	protected void createTerminalUseRequest(long passId, String ticketId, int count) {
		writeInt(Protocol.RQ_TERMINAL_USE_TICKET);
		writeLong(passId);
		writeString(ticketId);
		writeInt(count);
	}

}
