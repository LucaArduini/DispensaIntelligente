import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
 
public class Interfaccia extends Application {
    // MODEL
    public static OggettoCacheLocale variabiliGlobali;
    
    // VIEW
    private final Label etichettaUtente                 = new Label("Utente:");
    private TextField campoUtente;
    private final Button bottoneCarica                  = new Button("CARICA");
    
    private final FlowPane flowpaneFiltro               = new FlowPane();
    private ComboBox<String> tendinaFiltro;
    private final Label etichettaFiltro                 = new Label("filtro:   ");
    
    private final Label etichettaTabellaScadenze        = new Label("Tabella Scadenze");
    private VisualeTabellaScadenze tabellaScadenze;

    private final Button bottoneElimina                 = new Button("ELIMINA");
    private final Button bottoneInserisci               = new Button("INSERISCI");
    
    private Areogramma areogramma;
    
    private final HBox HBoxSelezioneUtente              = new HBox();
    private final HBox HBoxFiltro                       = new HBox();
    private final HBox HBoxBottoni                      = new HBox();
    private final VBox VBoxCentrale                     = new VBox();
    private final VBox VBoxContenitore                  = new VBox(); 
    

    public void start(Stage stage) {
        inizilizzaStruttureApplicazione();  // (01)
        inizializzaElementiVisivi();  // (02)
        
        HBoxSelezioneUtente.getChildren().addAll(etichettaUtente, campoUtente, bottoneCarica);  // (03)
        flowpaneFiltro.getChildren().addAll(etichettaFiltro, tendinaFiltro);
        HBoxBottoni.getChildren().addAll(bottoneElimina, bottoneInserisci);
        VBoxCentrale.getChildren().addAll(tabellaScadenze, HBoxBottoni);
        VBoxContenitore.getChildren().addAll(HBoxSelezioneUtente, etichettaTabellaScadenze, VBoxCentrale, areogramma.graficoAreogramma);

        aggiungiHandlerEventi(bottoneCarica, bottoneInserisci, bottoneElimina, stage);  // (04)
        aggiungiStile();    // (05)
        mostraApplicazione(stage, (new Group(VBoxContenitore, flowpaneFiltro)));    // (06)
        
        GestoreInvioLog.inviaSocketEvento("AVVIO");
    }
    
    
    private void aggiungiStile(){
        HBoxSelezioneUtente.setAlignment(Pos.CENTER_LEFT);
        HBoxSelezioneUtente.setSpacing(10);
        etichettaUtente.setStyle("-fx-font-size: 25px");
        
        etichettaFiltro.setStyle("-fx-font-size: 13pt");
        tendinaFiltro.maxWidth(50);
        tendinaFiltro.setStyle("-fx-background-color: rgba(65,105,225, 0.8); -fx-text-fill: white;");
        flowpaneFiltro.setStyle("-fx-border-color: black; -fx-border-radius: 10px");
        flowpaneFiltro.setAlignment(Pos.CENTER);
        flowpaneFiltro.setMinHeight(38);
        flowpaneFiltro.setMaxWidth(190);
        flowpaneFiltro.relocate(660, 105);
        HBoxFiltro.setAlignment(Pos.BOTTOM_RIGHT);
        
        etichettaTabellaScadenze.setStyle("-fx-font-weight: bolder; -fx-font-size: 38px");
        tabellaScadenze.setFixedCellSize(40);
        tabellaScadenze.setMinHeight(GestoreFileConfigurazione.fileConfigurazione.maxProdottiVisibiliSchermata*40+42);
        tabellaScadenze.setMaxHeight(GestoreFileConfigurazione.fileConfigurazione.maxProdottiVisibiliSchermata*40+42);
        
        HBoxBottoni.setAlignment(Pos.BOTTOM_RIGHT);
        HBox.setMargin(bottoneElimina, new Insets(15, 0, 0, 0));
        HBoxBottoni.setSpacing(25);
        
        
        VBoxContenitore.setAlignment(Pos.CENTER);
        VBoxContenitore.setStyle("-fx-padding: 10 0 0 30;");
        VBoxContenitore.setSpacing(20);
    }

    private void inizilizzaStruttureApplicazione(){
        GestoreFileConfigurazione.leggiFileConfigurazione();
        GestoreCacheLocale.prelevaCacheLocale();
        variabiliGlobali = GestoreCacheLocale.cache;
        GestoreDatabase.inizializzaDatabase(GestoreFileConfigurazione.fileConfigurazione.infoDatabase);
    }

    private void inizializzaElementiVisivi(){
        tabellaScadenze = creaTebella();
        if(variabiliGlobali.rigaCorrente != -1) tabellaScadenze.getSelectionModel().select(variabiliGlobali.rigaCorrente);
        
        tendinaFiltro = inizializzaComboBoxFiltro();
        
        campoUtente = new TextField(variabiliGlobali.utenteCorrente);
        campoUtente.setMaxWidth(150);
        
        areogramma = new Areogramma();
    }

    private void mostraApplicazione(Stage stage, Group group){
        Scene scene = new Scene(group);
        stage.setTitle("Dispensa Intelligente");
        scene.getStylesheets().add("stile.css");
        scene.setFill(Color.web("rgba(100,149,237,0.5)"));
        stage.setWidth(900);
        stage.setHeight(580 + GestoreFileConfigurazione.fileConfigurazione.maxProdottiVisibiliSchermata*40);
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:../../myfiles/icon.jpg"));
        stage.show();
    }
    
    private void aggiungiHandlerEventi(Button carica, Button inserisci, Button elimina, Stage stage){
        bottoneCarica.setOnAction((ActionEvent ev) -> {
            GestoreInvioLog.inviaSocketEvento("CARICA");
            variabiliGlobali.nuovaRigaInCostruzione();
            variabiliGlobali.utenteCorrente = campoUtente.getText();
            tabellaScadenze.aggiornaListaProdotti();
            Areogramma.aggiornaAreogramma();
        });

        bottoneInserisci.setOnAction((ActionEvent ev) -> {
            GestoreInvioLog.inviaSocketEvento("INSERISCI");
            variabiliGlobali.nuovaRigaInCostruzione();
            GestoreDatabase.inserisciInDatabase(tabellaScadenze.prendiRigaEditabile());
            tabellaScadenze.aggiornaListaProdotti();
            Areogramma.aggiornaAreogramma();
        });
        
        bottoneElimina.setOnAction((ActionEvent ev) -> {
            GestoreInvioLog.inviaSocketEvento("ELIMINA");
            GestoreDatabase.eliminaInDatabase(tabellaScadenze.getSelectionModel().getSelectedItems().get(0));
            tabellaScadenze.aggiornaListaProdotti();
            Areogramma.aggiornaAreogramma();
        });
        
        stage.setOnCloseRequest((WindowEvent we) ->{
            GestoreInvioLog.inviaSocketEvento("TERMINA");
            variabiliGlobali.rigaCorrente = tabellaScadenze.getSelectionModel().getSelectedIndex();
            variabiliGlobali.rigaInCostruzione.categoria = tabellaScadenze.prendiRigaEditabile().getCategoria();
            GestoreCacheLocale.conservaCacheLocale();
        });
    }
    
    private VisualeTabellaScadenze creaTebella(){
        Intervalli intervalli = GestoreFileConfigurazione.fileConfigurazione.intervalli;
        VisualeTabellaScadenze tabella = new VisualeTabellaScadenze();
        tabella.setRowFactory(row -> new TableRow<Prodotto>() {
            @Override
            public void updateItem(Prodotto item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null) setStyle("");
                else if(item.getGiorniRimanenti() == 0) setStyle("-fx-background-color: #B0B0B0");
                else if(item.getGiorniRimanenti() <= intervalli.intervallo1) setStyle("-fx-background-color: #FF0000");
                else if(item.getGiorniRimanenti() <= intervalli.intervallo2) setStyle("-fx-background-color: #FFA500");
                else if(item.getGiorniRimanenti() <= intervalli.intervallo3) setStyle("-fx-background-color: #32CD32");
                else setStyle("-fx-background-color: #ADFF2F");
            }
        });
                
        tabella.aggiornaListaProdotti();
        tabella.setEditable(true);
        return tabella;
    }
    
    private ComboBox<String> inizializzaComboBoxFiltro(){
        ComboBox<String> tendinaFiltro = new ComboBox();
        tendinaFiltro.getItems().addAll(GestoreFileConfigurazione.fileConfigurazione.intervalliTempo);
        tendinaFiltro.setOnAction((event) -> {
            switch(tendinaFiltro.getSelectionModel().getSelectedIndex()){
                case 0: 
                    variabiliGlobali.filtroCorrenteInt = 7; 
                    variabiliGlobali.filtroCorrenteString = GestoreFileConfigurazione.fileConfigurazione.intervalliTempo[0];
                break;
                case 1: 
                    variabiliGlobali.filtroCorrenteInt = 14; 
                    variabiliGlobali.filtroCorrenteString = GestoreFileConfigurazione.fileConfigurazione.intervalliTempo[1];
                break;
                case 2: 
                    variabiliGlobali.filtroCorrenteInt = 30; 
                    variabiliGlobali.filtroCorrenteString = GestoreFileConfigurazione.fileConfigurazione.intervalliTempo[2];
                break;
                case 3: 
                    variabiliGlobali.filtroCorrenteInt = 180; 
                    variabiliGlobali.filtroCorrenteString = GestoreFileConfigurazione.fileConfigurazione.intervalliTempo[3];
                break;
                case 4: 
                    variabiliGlobali.filtroCorrenteInt = 365; 
                    variabiliGlobali.filtroCorrenteString = GestoreFileConfigurazione.fileConfigurazione.intervalliTempo[4];
                break;
                case 5: 
                    variabiliGlobali.filtroCorrenteInt = 1000; 
                    variabiliGlobali.filtroCorrenteString = GestoreFileConfigurazione.fileConfigurazione.intervalliTempo[5];
                break;
            }
            tabellaScadenze.aggiornaListaProdotti();
            if(variabiliGlobali.rigaCorrente != -1 && variabiliGlobali.rigaCorrente < tabellaScadenze.numRigheTabella())
                tabellaScadenze.getSelectionModel().select(variabiliGlobali.rigaCorrente);
        });
        
        tendinaFiltro.getSelectionModel().select(variabiliGlobali.filtroCorrenteString); //valore iniziale preso dalla cache
        //▲▲ATTENZIONE▲▲ questa select triggera la tendina e quindi fa aggiornare la tabella
        return tendinaFiltro;
    }
}

/*
(01)
Vengono inizializzate tutte le strutture dati necessarie al corretto funzoinamento dell'applicazione: vengono letti i file
di cache e di configurazione, inoltre viene stabilita la connesione al database

(02)
Vengono creati gli elementi visivi dell'applicazione, come la tabella, l'areogramma e la comboBox per selezionare l'utente

(03)
in queste 5 righe di codice vengono organizzati gli elementi visivi all'interno di appositi "contenitori" per far si che
essi vengano posizionati come prestabilito

(04)
vengono aggiunti gli Handler ai bottoni che controllano l'interazione con l'utente

(05)
viene aggiunto lo stile agli elementi grafici

(06)
viene caricato lo stage che l'applicazione mostrerà
*/