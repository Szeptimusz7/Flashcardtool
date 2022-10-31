package nyelvtanulas_kr_szakdolgozat;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author kremm
 */
public class AdatbazisBongeszoController implements Initializable {

    @FXML private Label            lblStatus;
    @FXML private ComboBox<String> cbxStatus;
    @FXML private Label            lblNyelv;
    @FXML private ComboBox<String> cbxNyelv;
    @FXML private Label            lblSzuro;
    @FXML private TextField        txfSzoSzuro;
    @FXML private TextField        txfForditasSzuro;
    @FXML private TextField        txfMondatSzuro;
    @FXML private TextField        txfNeveloSzuro;
    @FXML private Button           btnSzurokTorlese;
    @FXML private TableColumn<Sor, String> oSzo;
    @FXML private TableColumn<Sor, String> oForditas;
    @FXML private TableColumn<Sor, String> oMondat;
    @FXML private TableColumn<Sor, String> oNevelo;
    @FXML private Label           lblSzo;
    @FXML private TextField       txfSzo;
    @FXML private Label           lblForditas;
    @FXML private TextField       txfForditas;
    @FXML private Label           lblMondat;
    @FXML private TextArea        txaMondat;
    @FXML private Label           lblNevelo;
    @FXML private TextField       txfNevelo;
    @FXML private Button          btnValtoztat;
    @FXML private TableView<Sor>  tblTablazat;
    @FXML private Button          btnSorTorlese;
    
    private ObservableList<Sor> ismertSzavak    = FXCollections.observableArrayList();
    private ObservableList<Sor> tanulandoSzavak = FXCollections.observableArrayList();
    private ChangeListener<Sor> listener;
    
    @FXML
    void lekerdez() {
        
        if (cbxNyelv.getValue() == null || cbxStatus.getValue() == null) return;

        tblTablazat.getSelectionModel().selectedItemProperty().removeListener(listener);
        
        txfNeveloSzuro.setText("");
        txfSzoSzuro.setText("");
        txfForditasSzuro.setText("");
        txfMondatSzuro.setText("");
        txfNevelo.setText("");
        txfSzo.setText("");
        txfForditas.setText("");
        txaMondat.setText("");
        
        String nyelvkod = FoablakController.nyelvekKodja.get(cbxNyelv.getValue());
        tblTablazat.getItems().clear();
        ismertSzavak.clear();
        tanulandoSzavak.clear();
        
        if (cbxStatus.getSelectionModel().getSelectedIndex() == 0) {
            ismertSzavak = FXCollections.observableArrayList(DB.mindenIsmertetLekerdez(nyelvkod));
            ismertSzavak.sort((s1, s2) -> s1.getSzo().compareTo(s2.getSzo()));
            tblTablazat.getItems().addAll(ismertSzavak);
            
        } else {
            tanulandoSzavak = FXCollections.observableArrayList(DB.mindenTanulandotLekerdez(nyelvkod));
            tanulandoSzavak.sort((s1, s2) -> s1.getSzo().compareTo(s2.getSzo()));
            tblTablazat.getItems().addAll(tanulandoSzavak);
        }

        tblTablazat.getSelectionModel().selectedItemProperty().addListener(listener);
    }


    @FXML
    void szurestVegrehajt() {

       tblTablazat.getSelectionModel().selectedItemProperty().removeListener(listener);
        
       List<Sor> filteredList;
       String NeveloSzuro   = txfNeveloSzuro.getText();
       String SzoSzuro      = txfSzoSzuro.getText();
       String ForditasSzuro = txfForditasSzuro.getText();
       String MondatSzuro   = txfMondatSzuro.getText();
       
       if (cbxStatus.getSelectionModel().getSelectedIndex() == 0) {
           filteredList = ismertSzavak.stream()
                    .filter(s -> s.getNevelo().contains(NeveloSzuro))
                    .filter(s -> s.getSzo().contains(SzoSzuro))
                    .filter(s -> s.getForditas().contains(ForditasSzuro))
                    .filter(s -> s.getMondat().contains(MondatSzuro))
                    .collect(Collectors.toList());
       } else {
           filteredList = tanulandoSzavak.stream()
                    .filter(s -> s.getNevelo().contains(NeveloSzuro))
                    .filter(s -> s.getSzo().contains(SzoSzuro))
                    .filter(s -> s.getForditas().contains(ForditasSzuro))
                    .filter(s -> s.getMondat().contains(MondatSzuro))
                    .collect(Collectors.toList());
       }
       
       tblTablazat.getItems().clear();
       tblTablazat.getItems().addAll(filteredList);
       
       tblTablazat.getSelectionModel().selectedItemProperty().addListener(listener);
        
    }
    
    @FXML
    void szurokTorlese() {
        txfNeveloSzuro.setText("");
        txfSzoSzuro.setText("");
        txfForditasSzuro.setText("");
        txfMondatSzuro.setText("");
        
        txfNevelo.setText("");
        txfSzo.setText("");
        txfForditas.setText("");
        txaMondat.setText("");
       
       szurestVegrehajt();
    }
    
    @FXML
    void sortValtoztat() {

        boolean sikeresModositas = true;
        
        // listener törlése
        tblTablazat.getSelectionModel().selectedItemProperty().removeListener(listener);
        
        // szűrő állapot mentése
        String NeveloSzuro   = txfNeveloSzuro.getText();
        String SzoSzuro      = txfSzoSzuro.getText();
        String ForditasSzuro = txfForditasSzuro.getText();
        String MondatSzuro   = txfMondatSzuro.getText();
        
        // kiválasztott sor adatai alapján DB-ben módosítani a rekordot
        String regiSzo = tblTablazat.getSelectionModel().getSelectedItem().getSzo();
        String nevelo = txfNevelo.getText();
        String ujSzo = txfSzo.getText();
        String forditas = txfForditas.getText();
        String mondat = txaMondat.getText();
        if (cbxStatus.getSelectionModel().getSelectedIndex() == 0) {
            if (!DB.ismertetModosit(FoablakController.nyelvekKodja.get(cbxNyelv.getValue()) + "_szavak", ujSzo, regiSzo)) sikeresModositas = false;
            
            for (int i = 0; i < ismertSzavak.size(); i++) {
                if (ismertSzavak.get(i).getSzo().equals(regiSzo)) {
                    ismertSzavak.set(i, new Sor("", ujSzo, "", ""));
                    break;
                }
            }
            
        } else {
            if (!DB.tanulandotModosit(FoablakController.nyelvekKodja.get(cbxNyelv.getValue()) + "_tanulando", nevelo, regiSzo, ujSzo, mondat, forditas, 0)) sikeresModositas = false;
            
            for (int i = 0; i < tanulandoSzavak.size(); i++) {
                if (tanulandoSzavak.get(i).getSzo().equals(regiSzo)) {
                    tanulandoSzavak.set(i, new Sor(nevelo, ujSzo, mondat, forditas));
                    break;
                }
            }
            
        } 
        
        // szűrő alapján újra filtered listet csinálni és betölteni a táblázatba
        List<Sor> filteredList;
        if (cbxStatus.getSelectionModel().getSelectedIndex() == 0) {
           filteredList = ismertSzavak.stream()
                    .filter(s -> s.getNevelo().contains(NeveloSzuro))
                    .filter(s -> s.getSzo().contains(SzoSzuro))
                    .filter(s -> s.getForditas().contains(ForditasSzuro))
                    .filter(s -> s.getMondat().contains(MondatSzuro))
                    .collect(Collectors.toList());
        } else {
           filteredList = tanulandoSzavak.stream()
                    .filter(s -> s.getNevelo().contains(NeveloSzuro))
                    .filter(s -> s.getSzo().contains(SzoSzuro))
                    .filter(s -> s.getForditas().contains(ForditasSzuro))
                    .filter(s -> s.getMondat().contains(MondatSzuro))
                    .collect(Collectors.toList());
        }
       
       tblTablazat.getItems().clear();
       tblTablazat.getItems().addAll(filteredList);
        
        // alsó szövegmezőkből adatok törlése
        txfNevelo.setText("");
        txfSzo.setText("");
        txfForditas.setText("");
        txaMondat.setText("");
        
        // listener hozzáadása
        tblTablazat.getSelectionModel().selectedItemProperty().addListener(listener);
        
        // Visszajelzés ablakban, hogy sikeres volt a változtatás?
        if (!sikeresModositas) Panel.hiba("Hiba", "Nem sikerült a módosítás!");
        else Panel.tajekoztat("Info", "Sikerült a módosítás!");
        
    }

    @FXML
    void sortorles() {

        boolean sikeresTorles = true;
        
        // listener törlése
        tblTablazat.getSelectionModel().selectedItemProperty().removeListener(listener);
        
        // szűrő állapot mentése
        String NeveloSzuro   = txfNeveloSzuro.getText();
        String SzoSzuro      = txfSzoSzuro.getText();
        String ForditasSzuro = txfForditasSzuro.getText();
        String MondatSzuro   = txfMondatSzuro.getText();
        
        // kiválasztott sor adatai alapján DB-ben törölni a rekordot
        String szo = tblTablazat.getSelectionModel().getSelectedItem().getSzo();
        if (cbxStatus.getSelectionModel().getSelectedIndex() == 0) {
            if (!DB.szotTorolAdatbazisbol(FoablakController.nyelvekKodja.get(cbxNyelv.getValue()) + "_szavak", szo)) sikeresTorles = false;
            
            for (int i = 0; i < ismertSzavak.size(); i++) {
                if (ismertSzavak.get(i).getSzo().equals(szo)) {
                    ismertSzavak.remove(i);
                    break;
                }
            }
            
        } else {
            if (!DB.szotTorolAdatbazisbol(FoablakController.nyelvekKodja.get(cbxNyelv.getValue()) + "_tanulando", szo)) sikeresTorles = false;
            
            for (int i = 0; i < tanulandoSzavak.size(); i++) {
                if (tanulandoSzavak.get(i).getSzo().equals(szo)) {
                    tanulandoSzavak.remove(i);
                    break;
                }
            }
            
        } 
        
        // szűrő alapján újra filtered listet csinálni és betölteni a táblázatba
        List<Sor> filteredList;
        if (cbxStatus.getSelectionModel().getSelectedIndex() == 0) {
           filteredList = ismertSzavak.stream()
                    .filter(s -> s.getNevelo().contains(NeveloSzuro))
                    .filter(s -> s.getSzo().contains(SzoSzuro))
                    .filter(s -> s.getForditas().contains(ForditasSzuro))
                    .filter(s -> s.getMondat().contains(MondatSzuro))
                    .collect(Collectors.toList());
        } else {
           filteredList = tanulandoSzavak.stream()
                    .filter(s -> s.getNevelo().contains(NeveloSzuro))
                    .filter(s -> s.getSzo().contains(SzoSzuro))
                    .filter(s -> s.getForditas().contains(ForditasSzuro))
                    .filter(s -> s.getMondat().contains(MondatSzuro))
                    .collect(Collectors.toList());
        }
       
       tblTablazat.getItems().clear();
       tblTablazat.getItems().addAll(filteredList);
        
        // alsó szövegmezőkből adatok törlése
        txfNevelo.setText("");
        txfSzo.setText("");
        txfForditas.setText("");
        txaMondat.setText("");
        
        // listener hozzáadása
        tblTablazat.getSelectionModel().selectedItemProperty().addListener(listener);
        
        // Visszajelzés ablakban, hogy sikeres volt a változtatás?
        if (!sikeresTorles) Panel.hiba("Hiba", "Nem sikerült a törlés!");
        else Panel.tajekoztat("Info", "Sikerült a törlés!");
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oSzo.setCellValueFactory(new PropertyValueFactory<>("szo"));
        oForditas.setCellValueFactory(new PropertyValueFactory<>("forditas"));
        oMondat.setCellValueFactory(new PropertyValueFactory<>("mondat"));
        oNevelo.setCellValueFactory(new PropertyValueFactory<>("nevelo"));
        
        cbxNyelv.getItems().clear();
        cbxNyelv.getItems().addAll(FoablakController.nyelvek);
        
        lblStatus.setText(FoablakController.adatbazisBongeszoFelirat[0]);
        cbxStatus.getItems().add(FoablakController.adatbazisBongeszoFelirat[1]);
        cbxStatus.getItems().add(FoablakController.adatbazisBongeszoFelirat[2]);
        lblNyelv.setText(FoablakController.adatbazisBongeszoFelirat[3]);
        lblSzuro.setText(FoablakController.adatbazisBongeszoFelirat[4]);
        txfNeveloSzuro.setPromptText(FoablakController.adatbazisBongeszoFelirat[5]);
        txfSzoSzuro.setPromptText(FoablakController.adatbazisBongeszoFelirat[6]);
        txfForditasSzuro.setPromptText(FoablakController.adatbazisBongeszoFelirat[7]);
        txfMondatSzuro.setPromptText(FoablakController.adatbazisBongeszoFelirat[8]);
        oNevelo.setText(FoablakController.adatbazisBongeszoFelirat[9]);
        oSzo.setText(FoablakController.adatbazisBongeszoFelirat[10]);
        oForditas.setText(FoablakController.adatbazisBongeszoFelirat[11]);
        oMondat.setText(FoablakController.adatbazisBongeszoFelirat[12]);
        lblNevelo.setText(FoablakController.adatbazisBongeszoFelirat[13]);
        lblSzo.setText(FoablakController.adatbazisBongeszoFelirat[14]);
        lblForditas.setText(FoablakController.adatbazisBongeszoFelirat[15]);
        lblMondat.setText(FoablakController.adatbazisBongeszoFelirat[16]);
        btnValtoztat.setText(FoablakController.adatbazisBongeszoFelirat[17]);
        btnSorTorlese.setText(FoablakController.adatbazisBongeszoFelirat[18]);
        btnSzurokTorlese.setText(FoablakController.adatbazisBongeszoFelirat[19]);

        cbxStatus.setValue(FoablakController.adatbazisBongeszoFelirat[2]);
        
        listener = (v, regi, uj) -> {
            
            txfNevelo.setText(uj.getNevelo());
            txfSzo.setText(uj.getSzo());
            txfForditas.setText(uj.getForditas());
            txaMondat.setText(uj.getMondat());
            
        };
        
    }    
    
}
