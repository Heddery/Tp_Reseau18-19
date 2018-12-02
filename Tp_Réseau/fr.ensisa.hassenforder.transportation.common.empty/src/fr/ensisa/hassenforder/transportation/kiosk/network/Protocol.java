package fr.ensisa.hassenforder.transportation.kiosk.network;

public class Protocol {

    public static final int KIOSK_PORT = 7777;
    
    public static final int RQ_CARD_CREATE =          0x2001;
    public static final int RQ_KIOSK_CARD_DESC=       0x2002;
    public static final int RQ_BUY_ROUTE =            0x2003;
    public static final int RQ_BUY_URBAN =            0x2004;
    public static final int RQ_BUY_SUBSCRIPTION =     0x2005;
    public static final int RQ_POPUP_ANNULER =        0x1006;
    public static final int RQ_POPUP_OK =             0x1007;
    
    public static final int RP_CARD_INFO =            0x1001;
    public static final int RP_KO =                   0x1002;
    public static final int RP_OK =                   0x1003;
    public static final int RP_POPUP_OK =             0x1004;
    public static final int RP_CARD_ID =              0x1005;
    public static final int RP_POPUP =                0x1006;

    
}
