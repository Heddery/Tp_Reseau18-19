package fr.ensisa.hassenforder.transportation.server.network;

import java.io.InputStream;

import fr.ensisa.hassenforder.network.BasicAbstractReader;
import fr.ensisa.hassenforder.transportation.kiosk.network.Protocol;
import fr.ensisa.hassenforder.transportation.server.model.Route;
import fr.ensisa.hassenforder.transportation.server.model.Subscription;
import fr.ensisa.hassenforder.transportation.server.model.Subscription.Month;
import fr.ensisa.hassenforder.transportation.server.model.Ticket;
import fr.ensisa.hassenforder.transportation.server.model.Urban;

public class KioskReader extends BasicAbstractReader {
	/* Server */

	private long passId;
	private String From;
	private String To;
	private int month;
	private int count;
	private long transactionId;
	private long cardId;

	public KioskReader(InputStream inputStream) {
		super(inputStream);
	}

	public void receive() {
		type = readInt();
		switch (type) {
		case 0:
			break;
		case Protocol.RQ_CARD_CREATE:
			break;
		case Protocol.RQ_KIOSK_CARD_DESC:
			this.passId = readLong();
			break;
		case Protocol.RQ_BUY_ROUTE:
			this.passId = readLong();
			this.From = readString();
			this.To = readString();
			this.count = readInt();
			break;
		case Protocol.RQ_BUY_URBAN:
			this.passId = readLong();
			this.count = readInt();
			break;
		case Protocol.RQ_BUY_SUBSCRIPTION:
			this.passId = readLong();
			this.month = readInt();
			break;
		case Protocol.RQ_POPUP_ANNULER:
			this.transactionId = readLong();
			break;
		case Protocol.RQ_POPUP_OK:
			this.transactionId = readLong();
			this.cardId = readLong();
			break;

		}

	}

	protected long getcardId() {
		return this.cardId;
	}

	protected long getTransactionId() {
		return this.transactionId;
	}

	protected Route getRoute() {
		Route route = new Route(this.passId, this.From, this.To, this.count);
		return route;
	}

	protected Urban getUrban() {
		Urban urban = new Urban(this.passId, this.count);
		return urban;
	}

	protected Subscription getSubscription() {
		Subscription subscription = new Subscription(this.passId, getMonth());
		return subscription;
	}

	protected long getPassId() {
		return this.passId;
	}

	protected String getTo() {
		return this.To;
	}

	protected String getFrom() {
		return this.From;
	}

	protected Month getMonth() {
		return Subscription.Month.values()[this.month];
	}

	protected int getCount() {
		return this.count;
	}
}
