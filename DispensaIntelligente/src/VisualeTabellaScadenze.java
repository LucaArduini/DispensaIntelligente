import java.util.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.*;
import javafx.scene.control.cell.*;
import javafx.util.*;

public class VisualeTabellaScadenze extends TableView<Prodotto> {
    private final ObservableList<Prodotto> listaOsservabileProdotti;
    

    public VisualeTabellaScadenze() {
        listaOsservabileProdotti = FXCollections.observableArrayList();
        setItems(listaOsservabileProdotti);
        getColumns().addAll(creaColonnaNome(), creaColonnaPezzi(), creaColonnaCategoria(), creaColonnaGiorniRimanenti(), creaColonnaDataScadenza());
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    public void aggiornaListaProdotti() {
        List<Prodotto> prodotti = GestoreDatabase.caricaProdottiUtenteFiltrati(Interfaccia.variabiliGlobali.utenteCorrente, Interfaccia.variabiliGlobali.filtroCorrenteInt);
        listaOsservabileProdotti.clear();
        listaOsservabileProdotti.addAll(prodotti);
    }
    
    public int numRigheTabella(){
        return listaOsservabileProdotti.size();
    }
    
    public Prodotto prendiRigaEditabile(){
        return listaOsservabileProdotti.get( this.numRigheTabella() -1);
    }
    
    private TableColumn creaColonnaNome(){
        TableColumn colonnaNome = new TableColumn("Nome");
        colonnaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        colonnaNome.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaNome.setOnEditCommit(new EventHandler<CellEditEvent<Prodotto, String>>(){
            public void handle(CellEditEvent<Prodotto, String> event){
                Prodotto prodotto = event.getRowValue();
                prodotto.setNome(event.getNewValue());
                Interfaccia.variabiliGlobali.rigaInCostruzione.nome = event.getNewValue();
            }
        });
        
        colonnaNome.setMinWidth(210);
        colonnaNome.setMaxWidth(210);
        return colonnaNome;
    }

    private TableColumn creaColonnaPezzi(){
        TableColumn colonnaPezzi = new TableColumn("Pezzi");
        colonnaPezzi.setCellValueFactory(new PropertyValueFactory<>("pezzi"));

        colonnaPezzi.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaPezzi.setOnEditCommit(new EventHandler<CellEditEvent<Prodotto, String>>(){
            public void handle(CellEditEvent<Prodotto, String> event){
                Prodotto prodotto = event.getRowValue();
                prodotto.setPezzi(event.getNewValue());
                Interfaccia.variabiliGlobali.rigaInCostruzione.pezzi = event.getNewValue();
            }
        });
        
        colonnaPezzi.setMinWidth(120);
        colonnaPezzi.setMaxWidth(120);
        return colonnaPezzi;
    }

    private TableColumn<Prodotto, StringProperty> creaColonnaCategoria(){
        TableColumn<Prodotto, StringProperty> colonnaCategoria = new TableColumn("Categoria");
        ObservableList<String> opzioni = FXCollections.observableArrayList(GestoreFileConfigurazione.fileConfigurazione.categorie);
        
        colonnaCategoria.setCellValueFactory(i -> {
            final StringProperty value = i.getValue().getcategoriaStringProperty();
            return Bindings.createObjectBinding(() -> value);       // binding to constant value
        });
        
        colonnaCategoria.setCellFactory(col -> {
            TableCell<Prodotto, StringProperty> tc = new TableCell<>();
            final ComboBox<String> comboBox = new ComboBox<>(opzioni);
            tc.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue != null)
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                if (newValue != null)
                    comboBox.valueProperty().bindBidirectional(newValue);
            });
            tc.graphicProperty().bind(Bindings.when(tc.emptyProperty()).then((Node) null).otherwise(comboBox));
            return tc;
        });
        
        colonnaCategoria.setStyle("-fx-alignment: CENTER;");
        colonnaCategoria.setMinWidth(150);
        colonnaCategoria.setMaxWidth(150);
        return colonnaCategoria;
    }

    private TableColumn creaColonnaGiorniRimanenti(){
        TableColumn colonnaGiorniRimanenti = new TableColumn("Giorni Rimanenti");
        colonnaGiorniRimanenti.setCellValueFactory(new PropertyValueFactory<>("giorniRimanenti"));
        
        colonnaGiorniRimanenti.setStyle("-fx-alignment: CENTER;");
        colonnaGiorniRimanenti.setMinWidth(180);
        colonnaGiorniRimanenti.setMaxWidth(180);
        return colonnaGiorniRimanenti;
    }

    private TableColumn creaColonnaDataScadenza(){
        TableColumn colonnaDataScadenza = new TableColumn("Data Scadenza");
        colonnaDataScadenza.setCellValueFactory(new PropertyValueFactory<>("dataScadenza"));
        
        colonnaDataScadenza.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                CellaDatePicker datePick = new CellaDatePicker(listaOsservabileProdotti);
                return datePick;
            }
        });

        colonnaDataScadenza.setStyle("-fx-alignment: CENTER;");
        colonnaDataScadenza.setMinWidth(160);
        colonnaDataScadenza.setMaxWidth(160);
        return colonnaDataScadenza;
    }
}