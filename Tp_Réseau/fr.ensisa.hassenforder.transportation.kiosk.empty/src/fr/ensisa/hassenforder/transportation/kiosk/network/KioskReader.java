package fr.ensisa.hassenforder.transportation.kiosk.network;

import java.io.InputStream;
import java.net.Proxy.Type;

import fr.ensisa.hassenforder.network.BasicAbstractReader;

import fr.ensisa.hassenforder.transportation.kiosk.network.Protocol;
import fr.ensisa.hassenforder.transportation.kiosk.model.Pass;
import fr.ensisa.hassenforder.transportation.kiosk.model.Ticket;
import fr.ensisa.hassenforder.transportation.kiosk.model.Ticket.Month;
import fr.ensisa.hassenforder.transportation.kiosk.model.Transaction;

public class KioskReader extends BasicAbstractReader {
	private Pass pass = null;
	private long passId;
	private int amount = 0;
	private long id = 0;
	private long transactionId = 0;
	private long ok;

	public KioskReader(InputStream inputStream) {
		super(inputStream);
	}

	public void receive() {
		type = readInt();
		switch (type) {
		case 0:
			break;
		case Protocol.RP_CARD_ID:
			this.passId = readLong();
			break;
		case Protocol.RP_CARD_INFO:
			this.readCardInfo();
			break;
		case Protocol.RP_POPUP:
			this.transactionId = readLong();
			this.amount = readInt();
			break;
		case Protocol.RP_KO:
			break;
		case Protocol.RP_OK:
			break;
		case Protocol.RP_POPUP_OK:
			this.ok = readLong();
			break;
		}
	}

	protected long getOk() {
		return this.ok;
	}

	protected long getTransactionId() {
		return this.transactionId;
	}

	protected Pass getPass() {
		return this.pass;

	}

	protected long getPassId() {
		return this.passId;
	}

	protected long getId() {
		return this.id;
	}

	protected int getAmount() {
		return this.amount;
	}

	protected void readCardInfo() {
		long passId = readLong();
		String description = readString();
		int size = readInt();
		this.pass = new Pass(passId, description);
		if (size != 0) {
			for (int i = 0; i < size; i++) {
				int ticketType = readInt();
				if (Ticket.Type.values()[ticketType] == Ticket.Type.ROUTE) {
					readRoute();
				}
				if (Ticket.Type.values()[ticketType] == Ticket.Type.URBAN) {
					readUrban();
				}
				if (Ticket.Type.values()[ticketType] == Ticket.Type.SUBSCRIPTION) {
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
		Month month = Ticket.Month.values()[subscriptionType];
		int use = readInt();
		this.pass.addTicket(new Ticket(ticketId, month, use));
	}
}
