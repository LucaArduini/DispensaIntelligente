import java.io.*;

public class GestoreCacheLocale {
    public static OggettoCacheLocale cache;
    
    
    public static void conservaCacheLocale(){  // (01)
        GestoreCacheLocale.cache = Interfaccia.variabiliGlobali;
        
        try (FileOutputStream fout = new FileOutputStream("./myfiles/cacheLocale.bin");
             ObjectOutputStream oout = new ObjectOutputStream(fout);){
            
            oout.writeObject(cache);
            System.out.println("cache salvata");
            
        }   catch(IOException ex){
                System.out.println("Errore: impossibile conservare la cache locale");
                System.err.println(ex.getMessage());
            }
    }
    
    public static void prelevaCacheLocale(){  // (02)
        
        try (FileInputStream fin = new FileInputStream("./myfiles/cacheLocale.bin");
             ObjectInputStream oin = new ObjectInputStream(fin);){

            cache = (OggettoCacheLocale) oin.readObject();
            System.out.println("cache prelevata");

        }   catch(IOException | ClassNotFoundException ex){
                System.out.println("Errore: impossibile prelevare la cache locale");
                System.err.println(ex.getMessage());
            }
    }
}

/*
(01)
metodo che scrive un file di cache per memorizzare alcuni parametri attualmente utilizzati nell'applicazione

(02)
metodo che legge il file di cache e ne memorizza i dati in un apposita struttura dati
*/