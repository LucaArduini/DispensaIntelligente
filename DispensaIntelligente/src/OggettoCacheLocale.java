import java.io.*;

public class OggettoCacheLocale implements Serializable{
    public String utenteCorrente;
    public int filtroCorrenteInt;
    public String filtroCorrenteString;
    public int rigaCorrente;
    public RigaInCostruzione rigaInCostruzione;

    
    public OggettoCacheLocale(){
        utenteCorrente = "";
	    filtroCorrenteInt = 365;
        filtroCorrenteString = "1 anno";
        rigaCorrente = -1;
        rigaInCostruzione = new RigaInCostruzione();
    }
    
    public void nuovaRigaInCostruzione(){
        this.rigaInCostruzione = new RigaInCostruzione();
    }
}

class RigaInCostruzione implements Serializable{
    public String nome = "NUOVO PRODOTTO";
    public String pezzi = "";
    public String categoria = "SELEZIONA";
}