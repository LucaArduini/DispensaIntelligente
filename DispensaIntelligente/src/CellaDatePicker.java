import java.text.*;
import java.time.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;

public class CellaDatePicker<S, T> extends TableCell<Prodotto, Date> {
    private DatePicker datePicker;
    private ObservableList<Prodotto> prodottiData;
    

    public CellaDatePicker(ObservableList<Prodotto> listaOsservabileProdotti) {
        super();
        this.prodottiData = listaOsservabileProdotti;

        if (datePicker == null) {
            creaDatepicker();
        }
        setGraphic(datePicker);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                datePicker.requestFocus();
            }
        });
    }

    @Override
    public void updateItem(Date data, boolean vuoto) {
        super.updateItem(data, vuoto);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if (null == this.datePicker) {
            System.out.println("datePicker ha valore NULL");
        }
        if (vuoto) {
            setText(null);
            setGraphic(null);
        }
        else {
            if (isEditing()) {
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
            else {
                setDataDatepicker(sdf.format(data));
                setText(sdf.format(data));
                setGraphic(this.datePicker);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }
        }
    }

    private void setDataDatepicker(String dateAsStr) {
        LocalDate ld = null;
        int giorni, mesi, anni;

        giorni = mesi = anni = 0;
        try {
            giorni = Integer.parseInt(dateAsStr.substring(0, 2));
            mesi = Integer.parseInt(dateAsStr.substring(3, 5));
            anni = Integer.parseInt(dateAsStr.substring(6, dateAsStr.length()));
        } catch (NumberFormatException e) { System.out.println("setDataDatepicker / unexpected error " + e); }

        ld = LocalDate.of(anni, mesi, giorni);
        datePicker.setValue(ld);
    }

    private void creaDatepicker() {
        this.datePicker = new DatePicker();
        datePicker.setPromptText("jj/mm/aaaa");
        datePicker.setEditable(true);

        datePicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                LocalDate data = datePicker.getValue();
                int index = getIndex();

                SimpleDateFormat smp = new SimpleDateFormat("dd/MM/yyyy");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, data.getDayOfMonth());
                cal.set(Calendar.MONTH, data.getMonthValue() - 1);
                cal.set(Calendar.YEAR, data.getYear());

                setText(smp.format(cal.getTime()));
                commitEdit(cal.getTime());

                if (null != getProdottiData()) {
                    getProdottiData().get(index).setDate(cal.getTime());
                }
            }
        });
        setAlignment(Pos.CENTER);
    }

    public void startEdit() {
        super.startEdit();
    }

    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }
   
    public ObservableList<Prodotto> getProdottiData() {
        return prodottiData;
    }

    public void setProdottiData(ObservableList<Prodotto> prodottiData) {
        this.prodottiData = prodottiData;
    }

    public DatePicker getDatepicker() {
        return datePicker;
    }

    public void setDatepicker(DatePicker datepicker) {
        this.datePicker = datepicker;
    }

}