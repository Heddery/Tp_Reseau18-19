package fr.ensisa.hassenforder.transportation.bank.network;

public interface Protocol {

    public static final int BANK_PORT = 6666;
    
    public static final int RQ_BANK_WITHDRAW =           0x2010;
    
    public static final int RP_OK =                      0x10010;
    public static final int RP_KO =                      0x10011;

}
