import java.text.*;
import java.util.*;
import javafx.beans.property.*;

public class Prodotto{
    private SimpleStringProperty nome;
    private SimpleStringProperty pezzi;
    private SimpleStringProperty categoria;
    private SimpleIntegerProperty giorniRimanenti;
    private SimpleObjectProperty<Date> dataScadenza;

    
    public Prodotto(){
        nome = new SimpleStringProperty("");
        pezzi = new SimpleStringProperty("");
        categoria = new SimpleStringProperty("");
        giorniRimanenti = new SimpleIntegerProperty(0);
        dataScadenza = new SimpleObjectProperty("2022-01-01");
    }
    public Prodotto(String n, String p, String c, int gr, Date ds) {
        nome = new SimpleStringProperty(n);
        pezzi = new SimpleStringProperty(p);
        categoria = new SimpleStringProperty(c);
        giorniRimanenti = new SimpleIntegerProperty(gr);
        dataScadenza = new SimpleObjectProperty(ds);
    }
    
    public String getNome() { return nome.get(); }
    public String getPezzi() { return pezzi.get(); }
    public String getCategoria() { return categoria.get(); }
    public StringProperty getcategoriaStringProperty() { return categoria; }
    public int getGiorniRimanenti() { return giorniRimanenti.get(); }
    public Date getDataScadenza() { return (Date) dataScadenza.get(); }
    public String getDataScadenzaAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(dataScadenza.get());

        return strDate;
    }

    public void setNome(String nuovoNome) { nome.set(nuovoNome); }
    public void setPezzi(String nuovoNumPezzi) { pezzi.set(nuovoNumPezzi); }
    public void setCategoria(String nuovaCategoria) { categoria.set(nuovaCategoria); }
    public void setCategoria(StringProperty nuovaCategoria) {categoria.set(nuovaCategoria.get()); }
    public void setDate(Date data) {dataScadenza.set(data);}
}