package fr.ensisa.hassenforder.transportation.kiosk.network;

import java.io.OutputStream;

import fr.ensisa.hassenforder.network.BasicAbstractWriter;
import fr.ensisa.hassenforder.transportation.kiosk.network.Protocol;

public class KioskWriter extends BasicAbstractWriter {
	/* kiosk en tant que client pour le serveur */

	public KioskWriter(OutputStream outputStream) {
		super(outputStream);
	}

	protected void createCardCreateRequest() {
		writeInt(Protocol.RQ_CARD_CREATE);
	}

	protected void createKioskCardDescRequest(Long passId) {
		writeInt(Protocol.RQ_KIOSK_CARD_DESC);
		writeLong(passId);
	}

	protected void createBuyRouteRequest(long passId, String from, String to, int count) {
		writeInt(Protocol.RQ_BUY_ROUTE);
		writeLong(passId);
		writeString(from);
		writeString(to);
		writeInt(count);
	}

	protected void createBuyUrbainRequest(Long passId, int count) {
		writeInt(Protocol.RQ_BUY_URBAN);
		writeLong(passId);
		writeInt(count);
	}

	protected void createBuySubscriptionRequest(Long passId, int month) {
		writeInt(Protocol.RQ_BUY_SUBSCRIPTION);
		writeLong(passId);
		writeInt(month);
	}

	protected void createPopupAnnulerRequest(long id) {
		writeInt(Protocol.RQ_POPUP_ANNULER);
		writeLong(id);
	}

	protected void createPopupOkRequest(long transactionId, long cardId) {
		writeInt(Protocol.RQ_POPUP_OK);
		writeLong(transactionId);
		writeLong(cardId);
	}

}
