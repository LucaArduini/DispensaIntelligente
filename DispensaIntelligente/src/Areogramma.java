import java.util.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.chart.*;

public class Areogramma{
    public static final ObservableList<PieChart.Data> listaOsservabileCategorie  = FXCollections.observableArrayList();
    public static final PieChart graficoAreogramma = new PieChart(listaOsservabileCategorie);
    
    
    public Areogramma(){
        graficoAreogramma.setTitle("Distribuzione Categorie");
        graficoAreogramma.setLabelsVisible(false);
        graficoAreogramma.setLegendSide(Side.LEFT);
        graficoAreogramma.setMaxHeight(250);
        graficoAreogramma.setMaxWidth(385);
        
        aggiornaAreogramma();
    }
    
    public static void aggiornaAreogramma(){    // (01)
        listaOsservabileCategorie.clear();
        List<PieChart.Data> listaPerControllo = GestoreDatabase.caricaFrequenzaCategorie(Interfaccia.variabiliGlobali.utenteCorrente, Interfaccia.variabiliGlobali.filtroCorrenteInt);
        if (listaPerControllo.size() == 0)
            listaOsservabileCategorie.add(new PieChart.Data("DISPENSA VUOTA", 1));
        else
            listaOsservabileCategorie.addAll(listaPerControllo);
    }
}

/*
    (01)
    Viene eseguita una query che restituisce i dati aggiornati che dovranno essere visualizzati dall'areogramma
    e aggiorna quest'ultimo
*/