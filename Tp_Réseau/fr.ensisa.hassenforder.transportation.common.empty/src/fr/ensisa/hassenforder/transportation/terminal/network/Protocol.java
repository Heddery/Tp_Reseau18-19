package fr.ensisa.hassenforder.transportation.terminal.network;

public class Protocol {

    public static final int TERMINAL_PORT = 8888;
    

    public static final int RQ_TERMINAL_CARD_DESC =              0x2008;
    public static final int RQ_TERMINAL_USE_TICKET =             0x2009;

    public static final int RP_CARD_INFO =                       0x1007;
    public static final int RP_OK =                              0x1008;
    public static final int RP_KO =                              0x1009;

}
