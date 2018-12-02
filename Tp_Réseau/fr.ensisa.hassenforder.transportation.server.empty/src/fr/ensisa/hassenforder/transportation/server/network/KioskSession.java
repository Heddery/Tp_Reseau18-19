package fr.ensisa.hassenforder.transportation.server.network;

import java.io.IOException;
import java.net.Socket;

import fr.ensisa.hassenforder.transportation.kiosk.network.Protocol;
import fr.ensisa.hassenforder.transportation.server.NetworkListener;
import fr.ensisa.hassenforder.transportation.server.model.Pass;
import fr.ensisa.hassenforder.transportation.server.model.Route;
import fr.ensisa.hassenforder.transportation.server.model.Subscription;
import fr.ensisa.hassenforder.transportation.server.model.Ticket;
import fr.ensisa.hassenforder.transportation.server.model.Transaction;
import fr.ensisa.hassenforder.transportation.server.model.Urban;

public class KioskSession extends Thread {

	private Socket connection;
	private NetworkListener listener;
	private Ticket ticket;

	public KioskSession(Socket connection, NetworkListener listener) {
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
			KioskWriter writer = new KioskWriter(connection.getOutputStream());
			KioskReader reader = new KioskReader(connection.getInputStream());
			reader.receive();
			switch (reader.getType()) {
			case 0:
				return false; // socket closed
			case Protocol.RQ_KIOSK_CARD_DESC:
				doRequestKioskCardDesc(writer, reader);
				break;
			case Protocol.RQ_CARD_CREATE:
				doRequestKioskCreateCard(writer, reader);
				break;
			case Protocol.RQ_BUY_ROUTE:
				doRequestKioskBuyRoute(writer, reader);
				break;
			case Protocol.RQ_BUY_URBAN:
				doRequestKioskBuyUrban(writer, reader);
				break;
			case Protocol.RQ_BUY_SUBSCRIPTION:
				doRequestKioskBuySubscription(writer, reader);
				break;
			case Protocol.RQ_POPUP_ANNULER:
				doRequestPopupAnnuler(writer, reader);
				break;
			case Protocol.RQ_POPUP_OK:
				doRequestPopupOk(writer, reader);
				break;
			}
			writer.send();
			return true;
		} catch (IOException e) {
			return false;
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

	private void doRequestKioskCardDesc(KioskWriter writer, KioskReader reader) {
		Pass pass = this.listener.kioskFetchPass(reader.getPassId());
		if (pass != null) {
			writer.createCardInfoReplay(pass);
		} else {
			writer.createKoReplay();
		}
	}

	private void doRequestKioskCreateCard(KioskWriter writer, KioskReader reader) {
		long passId = this.listener.kioskCreatePass();
		writer.createCardIdReplay(passId);
	}

	private void doRequestKioskBuyRoute(KioskWriter writer, KioskReader reader) {
		this.ticket = (Ticket) reader.getRoute();
		Transaction transaction = this.listener.kioskCreateTransaction(this.ticket);
		if (transaction != null) {
			this.listener.kioskAddTicket(this.ticket);
			writer.createPopupAmountReplay(transaction.getId(), transaction.getAmount());
		} else {
			writer.createKoReplay();
		}
	}

	private void doRequestKioskBuyUrban(KioskWriter writer, KioskReader reader) {
		this.ticket = (Ticket) reader.getUrban();
		Transaction transaction = this.listener.kioskCreateTransaction(this.ticket);
		if (transaction != null) {
			this.listener.kioskAddTicket(this.ticket);
			writer.createPopupAmountReplay(transaction.getId(), transaction.getAmount());
		} else {
			writer.createKoReplay();
		}
	}

	private void doRequestKioskBuySubscription(KioskWriter writer, KioskReader reader) {
		this.ticket = (Ticket) reader.getSubscription();
		Transaction transaction = this.listener.kioskCreateTransaction(this.ticket);
		if (transaction != null) {
			if (this.listener.kioskAddTicket(this.ticket))
				;
			writer.createPopupAmountReplay(transaction.getId(), transaction.getAmount());
		} else {
			writer.createKoReplay();
		}
	}

	private void doRequestPopupAnnuler(KioskWriter writer, KioskReader reader) {
		if (this.listener.kioskCancelTransaction(reader.getTransactionId())) {
			writer.createPopupAnnulerReplay();
		} else {
			writer.createKoReplay();
		}
	}

	private void doRequestPopupOk(KioskWriter writer, KioskReader reader) {
		writer.createPopupOkReplay(this.listener.kioskPayTransaction(reader.getTransactionId(), reader.getcardId()));
	}

}
