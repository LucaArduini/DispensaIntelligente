import java.sql.*;
import java.util.*;
import javafx.scene.chart.*;

public class GestoreDatabase {
    private static Connection connessioneADatabase; 
    private static PreparedStatement statementProdottiUtenteFiltrati; 
    private static PreparedStatement statementFrequenzaCategorie;
    private static PreparedStatement statementInserisciInDatabase;
    private static PreparedStatement statementEliminaInDatabase;
    

    public static void inizializzaDatabase(InfoDatabase info){  // (01)
        try {
            connessioneADatabase = DriverManager.getConnection("jdbc:mysql://"+info.indirizzoIP+":"+info.porta+"/dispensaintelligente", info.username, info.password);   
            statementProdottiUtenteFiltrati = connessioneADatabase.prepareStatement("SELECT nome, pezzi, categoria, DATEDIFF(DataScadenza, CURRENT_DATE) AS giorniRimanenti, dataScadenza FROM prodotti WHERE utente = ? AND DATEDIFF(DataScadenza, CURRENT_DATE) <= ? ORDER BY dataScadenza");
            statementFrequenzaCategorie = connessioneADatabase.prepareStatement("SELECT categoria, COUNT(*) AS frequenza FROM prodotti WHERE utente = ? AND DATEDIFF(DataScadenza, CURRENT_DATE) <= ? GROUP BY Categoria");
            statementInserisciInDatabase = connessioneADatabase.prepareStatement("INSERT INTO prodotti VALUES (?, ?, ?, ?, ?)");
            statementEliminaInDatabase = connessioneADatabase.prepareStatement("DELETE FROM prodotti WHERE utente=? AND nome=? AND dataScadenza=?");
        } catch (SQLException e) {System.err.println(e.getMessage()); System.out.println("errore costruttore DB");} 
    }
    
    public static List<Prodotto> caricaProdottiUtenteFiltrati(String utente, int filtro) {  // (02)
        List<Prodotto> listaProdotti = new ArrayList<>();
        try { 
            statementProdottiUtenteFiltrati.setString(1, utente);
            statementProdottiUtenteFiltrati.setInt(2, filtro);
            ResultSet rs = statementProdottiUtenteFiltrati.executeQuery();
            while (rs.next())
                listaProdotti.add(new Prodotto(rs.getString("nome"), rs.getString("pezzi"), rs.getString("categoria"), rs.getInt("giorniRimanenti"), rs.getDate("dataScadenza")));
        } catch (SQLException e) {System.err.println(e.getMessage());} 
        
        Prodotto prodottoEditabile = new Prodotto(Interfaccia.variabiliGlobali.rigaInCostruzione.nome, Interfaccia.variabiliGlobali.rigaInCostruzione.pezzi, Interfaccia.variabiliGlobali.rigaInCostruzione.categoria, 0, java.util.Calendar.getInstance().getTime() );
        listaProdotti.add(prodottoEditabile); //aggiungo la riga editabile
        return listaProdotti;
    }

    public static List<PieChart.Data> caricaFrequenzaCategorie(String utente, int filtro) {  // (03)
        List<PieChart.Data> listaCategorie = new ArrayList<>();
        try { 
            statementFrequenzaCategorie.setString(1, utente);
            statementFrequenzaCategorie.setInt(2, filtro);
            ResultSet rs = statementFrequenzaCategorie.executeQuery();
            while (rs.next())
                listaCategorie.add(new PieChart.Data(rs.getString("categoria"), rs.getInt("frequenza")));
        } catch (SQLException e) {System.err.println(e.getMessage());} 
        return listaCategorie;
    }
    
    public static void inserisciInDatabase(Prodotto prodotto){  // (04)
        try {
            statementInserisciInDatabase.setString(1, Interfaccia.variabiliGlobali.utenteCorrente);
            statementInserisciInDatabase.setString(2, prodotto.getNome());
            statementInserisciInDatabase.setString(3, prodotto.getPezzi());
            statementInserisciInDatabase.setString(4, prodotto.getCategoria());
            statementInserisciInDatabase.setString(5, prodotto.getDataScadenzaAsString());

            System.out.println("rows affected: " + statementInserisciInDatabase.executeUpdate());
        } catch (SQLException e) {System.err.println(e.getMessage());}
    }

    public static void eliminaInDatabase(Prodotto prodotto){  // (05)
        try {
            statementEliminaInDatabase.setString(1, Interfaccia.variabiliGlobali.utenteCorrente);
            statementEliminaInDatabase.setString(2, prodotto.getNome());
            statementEliminaInDatabase.setString(3, prodotto.getDataScadenzaAsString());

            System.out.println("rows affected: " + statementEliminaInDatabase.executeUpdate());
        } catch (SQLException e) {System.err.println(e.getMessage());}
    }
}

/*
(01)
Viene stabilita la conessione al database e vengono preparate le query che verrano poi eseguite dagli altri metodi
della classe

(02)
è la query più importante: restituisce tutti i prodotti presenti nel DB relativi all'utente selezionato

(03)
restituisce i dati che verranno poi visulizzati nell'areogramma

(04)
query che inserisce nel DB un nuovo prodotto

(05)
query che elimina un prodotto dal DB
*/