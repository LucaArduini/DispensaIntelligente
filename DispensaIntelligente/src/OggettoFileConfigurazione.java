public class OggettoFileConfigurazione {
    public int maxProdottiVisibiliSchermata;
    public String[] categorie;
    public String[] intervalliTempo;
    public Intervalli intervalli = new Intervalli();
    public InfoServerLog infoServerLog = new InfoServerLog();
    public InfoDatabase infoDatabase = new InfoDatabase();
}

class Intervalli{
    public int intervallo1;
    public int intervallo2;
    public int intervallo3;
}
class InfoServerLog{
    public String indirizzoIP;
    public String porta;
}
class InfoDatabase{
    public String username;
    public String password;
    public String indirizzoIP;
    public String porta;
}