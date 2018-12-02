package fr.ensisa.hassenforder.transportation.bank.network;

import java.io.OutputStream;

import fr.ensisa.hassenforder.network.BasicAbstractWriter;
import fr.ensisa.hassenforder.transportation.bank.network.Protocol;

public class BankWriter extends BasicAbstractWriter {

	public BankWriter(OutputStream outputStream) {
		super(outputStream);
	}

	protected void createBankWithdrawOkReplay() {
		writeInt(Protocol.RP_OK);
	}

	protected void createKoReplay() {
		writeInt(Protocol.RP_KO);
	}
}
