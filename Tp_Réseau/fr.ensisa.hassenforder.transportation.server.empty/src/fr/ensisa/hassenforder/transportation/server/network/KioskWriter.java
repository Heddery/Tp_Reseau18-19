package fr.ensisa.hassenforder.transportation.server.network;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.ensisa.hassenforder.network.BasicAbstractWriter;
import fr.ensisa.hassenforder.transportation.kiosk.network.Protocol;
import fr.ensisa.hassenforder.transportation.server.model.Pass;
import fr.ensisa.hassenforder.transportation.server.model.Route;
import fr.ensisa.hassenforder.transportation.server.model.Subscription;
import fr.ensisa.hassenforder.transportation.server.model.Ticket;
import fr.ensisa.hassenforder.transportation.server.model.Transaction;
import fr.ensisa.hassenforder.transportation.server.model.Urban;
import fr.ensisa.hassenforder.transportation.server.model.Ticket.Type;

public class KioskWriter extends BasicAbstractWriter {

	// pas de données membres !!
	public KioskWriter(OutputStream outputStream) {
		super(outputStream);
	}

	protected void createCardInfoReplay(Pass pass) {

		writeInt(Protocol.RP_CARD_INFO);
		writeLong(pass.getPassId());
		writeString(pass.getDescription());

		final List<Ticket> tickets = pass.getTickets();
		this.writeInt(tickets.size());

		for (Ticket t : tickets) {
			if (t instanceof Route) {
				writeRoute(t);
			}
			if (t instanceof Urban) {
				writeUrban(t);
			}
			if (t instanceof Subscription) {
				writeSubscription(t);
			}
		}
	}

	protected void createKoReplay() {
		writeInt(Protocol.RP_KO);
	}

	protected void createCardIdReplay(long passId) {
		writeInt(Protocol.RP_CARD_ID);
		writeLong(passId);
	}

	protected void createPopupAmountReplay(long transactionId, int amount) {
		writeInt(Protocol.RP_POPUP);
		writeLong(transactionId);
		writeInt(amount);
	}

	protected void createPopupOkReplay(long ok) {
		writeInt(Protocol.RP_POPUP_OK);
		writeLong(ok);
	}

	protected void createPopupAnnulerReplay() {
		writeInt(Protocol.RP_OK);
	}

	protected void writeRoute(Ticket t) {
		writeInt(Type.ROUTE.ordinal());
		writeString(t.getTicketId());
		writeString(((Route) t).getFrom());
		writeString(((Route) t).getTo());
		writeInt(((Route) t).getCount());
		writeInt(((Route) t).getUsed());
	}

	protected void writeUrban(Ticket t) {
		writeInt(Type.URBAN.ordinal());
		writeString(t.getTicketId());
		writeInt(((Urban) t).getCount());
		writeInt(((Urban) t).getUsed());
	}

	protected void writeSubscription(Ticket t) {
		writeInt(Type.SUBSCRIPTION.ordinal());
		writeString(t.getTicketId());
		writeInt(((Subscription) t).getMonth().ordinal());
		writeInt(((Subscription) t).getUsed());
	}

}
