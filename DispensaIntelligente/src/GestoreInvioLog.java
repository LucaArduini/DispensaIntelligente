import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.converters.basic.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class GestoreInvioLog{
    private static InfoServerLog infoServer;

    
    static{
        infoServer = GestoreFileConfigurazione.fileConfigurazione.infoServerLog;
    }
    
    public static void inviaSocketEvento(String nomeEvento) {  // (01)
        XStream xs = new XStream();
        xs.registerConverter(new DateConverter("yyyy-MM-dd", null));
        
        RigaDiLog log = new RigaDiLog(infoServer.indirizzoIP, nomeEvento);
        String XMLRigaDiLog ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+ xs.toXML(log);
        
        try ( DataOutputStream dout = new DataOutputStream((new Socket("localhost",8081) ).getOutputStream())) {
            dout.writeUTF(XMLRigaDiLog);
            System.out.println("inviato evento: " + nomeEvento);
        } catch (Exception e) {e.printStackTrace();}
    }
}

class RigaDiLog implements Serializable{
    public String nomeApplicazione = "DispensaIntelligente";
    public String indirizzoIP;
    public String dataEora;
    public String evento;
    
    
    public RigaDiLog(String indirizzoIP, String nomeEvento){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());
        
        this.indirizzoIP = indirizzoIP;
        this.dataEora = strDate;
        this.evento = nomeEvento;
    }
}

/*
(01)
metodo che invia i record di log degli "eventi dell'applicazione" ad un apposito server che li riceverà e li memorizzerà
*/