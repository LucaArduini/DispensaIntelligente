import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.converters.basic.*;
import java.io.*;
import java.nio.file.*;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.xml.sax.*;
import org.w3c.dom.*;

public class GestoreFileConfigurazione {
    public static OggettoFileConfigurazione fileConfigurazione;
    
    
    public static void leggiFileConfigurazione(){  // (01)
        XStream xs = new XStream();
        xs.registerConverter(new DateConverter("yyyy-MM-dd", null));
        
        try {
            if(validazioneXML())
                fileConfigurazione = (OggettoFileConfigurazione) xs.fromXML(new String(Files.readAllBytes(Paths.get("./myfiles/fileConfigurazioneXML.xml"))));
            else
                System.out.println("ERRORE validazione XML");
        }   catch (Exception ex) { System.err.println(ex.getMessage()); }
    }
    
    private static boolean validazioneXML() {  // (02)
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Document d = db.parse(new File("./myfiles/fileConfigurazioneXML.xml"));
            Schema s = sf.newSchema(new StreamSource(new File("./myfiles/schemaFileConfigurazione.xsd")));
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

/*
(01)
viene letto il fileConfigurazione.xml e se questo supera la validazione, i suoi dati verranno memorizzati in 
un apposita struttura dati

(02)
metodo che effettua la validazione del fle XML
*/