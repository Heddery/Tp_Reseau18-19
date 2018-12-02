package fr.ensisa.hassenforder.transportation.bank.network;

import java.io.InputStream;

import fr.ensisa.hassenforder.network.BasicAbstractReader;
import fr.ensisa.hassenforder.transportation.bank.network.Protocol;

public class BankReader extends BasicAbstractReader {

	private long cardId;
	private int amount;

	public BankReader(InputStream inputStream) {
		super(inputStream);
	}

	public void receive() {
		type = readInt();
		switch (type) {

		case 0:
			break;
		case Protocol.RQ_BANK_WITHDRAW:
			this.cardId = readLong();
			this.amount = readInt();
		}
	}

	protected long getCardId() {
		return this.cardId;
	}

	protected int getAmount() {
		return this.amount;
	}

}