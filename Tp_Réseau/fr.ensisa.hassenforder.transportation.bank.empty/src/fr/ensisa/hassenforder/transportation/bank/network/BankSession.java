package fr.ensisa.hassenforder.transportation.bank.network;

import java.io.IOException;
import java.net.Socket;

import fr.ensisa.hassenforder.transportation.bank.BankNetworkListener;
import fr.ensisa.hassenforder.transportation.bank.network.Protocol;

public class BankSession extends Thread {

	private Socket connection;
	private BankNetworkListener listener;

	public BankSession(Socket connection, BankNetworkListener listener) {
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
			BankWriter writer = new BankWriter(connection.getOutputStream());
			BankReader reader = new BankReader(connection.getInputStream());
			reader.receive();
			switch (reader.getType()) {
			case 0:
				return false; // socket closed
			case Protocol.RQ_BANK_WITHDRAW:
				doRequesBanktWithdraw(writer, reader);
				break;
			}
			writer.send();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private void doRequesBanktWithdraw(BankWriter writer, BankReader reader) {
		if (this.listener.withdrawByCardId(reader.getCardId(), reader.getAmount())) {
			writer.createBankWithdrawOkReplay();
		} else {
			writer.createKoReplay();
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

}