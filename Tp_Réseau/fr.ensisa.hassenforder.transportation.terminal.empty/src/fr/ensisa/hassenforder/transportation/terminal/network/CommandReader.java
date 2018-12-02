package fr.ensisa.hassenforder.transportation.terminal.network;

import java.io.InputStream;
import java.util.List;

import javax.management.loading.PrivateClassLoader;

import fr.ensisa.hassenforder.network.BasicAbstractReader;

import fr.ensisa.hassenforder.transportation.terminal.network.Protocol;
import fr.ensisa.hassenforder.transportation.terminal.model.Pass;
import fr.ensisa.hassenforder.transportation.terminal.model.Ticket;
import fr.ensisa.hassenforder.transportation.terminal.model.Ticket.Month;
import fr.ensisa.hassenforder.transportation.terminal.model.Ticket.Type;

public class CommandReader extends BasicAbstractReader {
	private Pass pass = null;

	public CommandReader(InputStream inputStream) {
		super(inputStream);
	}

	public void receive() {
		type = readInt();
		switch (type) {
		case 0:
			break;

		case Protocol.RP_CARD_INFO:
			this.readCardInfo();
			break;

		case Protocol.RP_OK:
			break;

		case Protocol.RP_KO:
			break;
		}
	}

	protected Pass getPass() {
		return this.pass;
	}

	protected void readCardInfo() {
		long passId = readLong();
		String description = readString();
		int size = readInt();
		this.pass = new Pass(passId, description);
		if (size != 0) {
			for (int i = 0; i < size; i++) {
				int ticketType = readInt();
				if (Type.values()[ticketType] == Type.ROUTE) {
					readRoute();
				}

				if (Type.values()[ticketType] == Type.URBAN) {
					readUrban();
				}
				if (Type.values()[ticketType] == Type.SUBSCRIPTION) {
					readSubscription();
				}
			}
		}
	}

	protected void readRoute() {
		String ticketId = readString();
		String from = readString();
		String to = readString();
		int count = readInt();
		int use = readInt();
		this.pass.addTicket(new Ticket(ticketId, from, to, count, use));
	}

	protected void readUrban() {
		String ticketId = readString();
		int count = readInt();
		int use = readInt();
		this.pass.addTicket(new Ticket(ticketId, count, use));
	}

	protected void readSubscription() {
		String ticketId = readString();
		int subscriptionType = readInt();
		Month month = Month.values()[subscriptionType];
		int use = readInt();
		this.pass.addTicket(new Ticket(ticketId, month, use));
	}

}
