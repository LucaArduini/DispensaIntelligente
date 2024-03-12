import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

public class ServerRicezioneLog {

    public static void main(String[] args) {
        RigaDiLog rigaLog = null;
        try (ServerSocket servs = new ServerSocket(8081, 5) ){
            while(true) {
                try(Socket s = servs.accept();
                    DataInputStream din = new DataInputStream(s.getInputStream());){
                    String nuovaRigaLogXML = din.readUTF();
                   
                    if(validazioneXML(nuovaRigaLogXML)){
                        XStream xs = new XStream();                        
                        rigaLog = (RigaDiLog) xs.fromXML(nuovaRigaLogXML);
                        System.out.println(rigaLog.nomeApplicazione +" "+ rigaLog.dataEora +"    evento: " + rigaLog.evento);
                        
                        nuovaRigaLogXML = nuovaRigaLogXML + "\n\n";
                        Files.write(Paths.get("./myfiles/archivioLogEventi.xml"), nuovaRigaLogXML.getBytes(), StandardOpenOption.APPEND);
                    }
                    else
                        System.out.println("ERRORE valdiazione XML");
                }
            }
        } catch (Exception e) {e.printStackTrace();}
    }
    
    public static boolean validazioneXML(String stringaXML) {
        try {
            Files.write(Paths.get("./myfiles/logInArrivoXML.xml"), stringaXML.getBytes());
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Document d = db.parse(new File("./myfiles/logInArrivoXML.xml"));
            Schema s = sf.newSchema(new StreamSource(new File("./myfiles/schemaRigheDiLog.xsd")));
            s.newValidator().validate(new DOMSource(d));
            return true;
        } catch (Exception e) {
            if (e instanceof SAXException) 
              System.out.println("Errore di validazione: " + e.getMessage());
            else
              System.out.println(e.getMessage());
            return false;
        }
  }
}
