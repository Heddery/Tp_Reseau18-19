package fr.ensisa.hassenforder.transportation.server.network;

import java.io.OutputStream;
import java.util.List;

import fr.ensisa.hassenforder.network.BasicAbstractWriter;
import fr.ensisa.hassenforder.transportation.terminal.network.Protocol;
import fr.ensisa.hassenforder.transportation.server.model.Model;
import fr.ensisa.hassenforder.transportation.server.model.Pass;
import fr.ensisa.hassenforder.transportation.server.model.Route;
import fr.ensisa.hassenforder.transportation.server.model.Subscription;
import fr.ensisa.hassenforder.transportation.server.model.Ticket;
import fr.ensisa.hassenforder.transportation.server.model.Ticket.Type;
import fr.ensisa.hassenforder.transportation.server.model.Urban;

public class TerminalWriter extends BasicAbstractWriter {
	/* terminal en tant que client pour le serveur */
	public TerminalWriter(OutputStream outputStream) {
		super(outputStream);
	}

	protected void createTerminalCardDescReplay(Pass pass) {
		this.writeInt(Protocol.RP_CARD_INFO);
		this.writeLong(pass.getPassId());
		this.writeString(pass.getDescription());

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

	protected void createTerminalUseReplay() {
		writeInt(Protocol.RP_OK);
	}

	protected void createKoReplay() {
		writeInt(Protocol.RP_KO);
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
