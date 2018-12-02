package fr.ensisa.hassenforder.transportation.kiosk.network;

import java.io.IOException;
import java.net.Socket;

import fr.ensisa.hassenforder.transportation.kiosk.model.Pass;
import fr.ensisa.hassenforder.transportation.kiosk.model.Transaction;

public class KioskSession implements ISession {

	private Socket connection;
	private long passId;

	public KioskSession() {
	}

	@Override
	synchronized public boolean close() {
		try {
			if (connection != null) {
				connection.close();
			}
			connection = null;
		} catch (IOException e) {
		}
		return true;
	}

	@Override
	synchronized public boolean open() {
		this.close();
		try {
			connection = new Socket("localhost", Protocol.KIOSK_PORT);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public long createPass() {
		try {
			if (true != Boolean.TRUE)
				throw new IOException();
			KioskWriter writer = new KioskWriter(connection.getOutputStream());
			KioskReader reader = new KioskReader(connection.getInputStream());
			writer.createCardCreateRequest();
			writer.send();

			reader.receive();
			if (reader.getType() == Protocol.RP_CARD_ID) {
				return reader.getPassId();
			} else {
				return -1L;

			}
		} catch (IOException e) {
			return -1L;
		}
	}

	@Override
	public Pass getPassById(long passId) {
		try {
			if (true != Boolean.TRUE)
				throw new IOException();
			KioskWriter writer = new KioskWriter(connection.getOutputStream());
			KioskReader reader = new KioskReader(connection.getInputStream());
			writer.createKioskCardDescRequest(passId);
			writer.send();

			reader.receive();
			if (reader.getType() == Protocol.RP_CARD_INFO) {
				Pass pass = reader.getPass();
				this.passId = pass.getPassId();
				return pass;
			} else {
				return null;
			}
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public Transaction buyRoute(long passId, String from, String to, int count) {
		try {
			KioskWriter writer = new KioskWriter(connection.getOutputStream());
			KioskReader reader = new KioskReader(connection.getInputStream());
			writer.createBuyRouteRequest(passId, from, to, count);
			writer.send();

			reader.receive();
			if (reader.getType() == Protocol.RP_POPUP) {
				return new Transaction(reader.getTransactionId(), reader.getAmount());
			} else {
				return null;
			}
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public Transaction buyUrban(long passId, int count) {
		try {
			KioskWriter writer = new KioskWriter(connection.getOutputStream());
			KioskReader reader = new KioskReader(connection.getInputStream());
			writer.createBuyUrbainRequest(passId, count);
			writer.send();

			reader.receive();
			if (reader.getType() == Protocol.RP_POPUP) {
				return new Transaction(reader.getTransactionId(), reader.getAmount());
			} else {
				return null;
			}
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public Transaction buySubscription(long passId, int month) {
		try {
			KioskWriter writer = new KioskWriter(connection.getOutputStream());
			KioskReader reader = new KioskReader(connection.getInputStream());
			writer.createBuySubscriptionRequest(passId, month);
			writer.send();

			reader.receive();
			if (reader.getType() == Protocol.RP_POPUP) {
				return new Transaction(reader.getTransactionId(), reader.getAmount());
			} else {
				return null;
			}
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public boolean cancelTransaction(long id) {
		try {
			KioskWriter writer = new KioskWriter(connection.getOutputStream());
			KioskReader reader = new KioskReader(connection.getInputStream());
			writer.createPopupAnnulerRequest(id);
			writer.send();

			reader.receive();
			if (reader.getType() == Protocol.RP_OK) {
				return true;
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public long payTransaction(long transactionId, long cardId) {
		try {
			KioskWriter writer = new KioskWriter(connection.getOutputStream());
			KioskReader reader = new KioskReader(connection.getInputStream());
			writer.createPopupOkRequest(transactionId, cardId);
			writer.send();

			reader.receive();
			if (reader.getType() == Protocol.RP_POPUP_OK) {
				return reader.getOk();
			} else {
				return -1;
			}
		} catch (IOException e) {
			return -1;
		}
	}

}
