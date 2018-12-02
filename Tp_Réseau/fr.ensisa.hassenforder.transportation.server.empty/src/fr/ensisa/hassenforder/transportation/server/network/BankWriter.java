package fr.ensisa.hassenforder.transportation.server.network;

import java.io.OutputStream;

import fr.ensisa.hassenforder.network.BasicAbstractWriter;
import fr.ensisa.hassenforder.transportation.bank.network.Protocol;

public class BankWriter extends BasicAbstractWriter {

	public BankWriter(OutputStream outputStream) {
		super(outputStream);
	}

	protected void createBankWithdraw(long cardId, int amount) {
		writeInt(Protocol.RQ_BANK_WITHDRAW);
		writeLong(cardId);
		writeInt(amount);
	}
}
