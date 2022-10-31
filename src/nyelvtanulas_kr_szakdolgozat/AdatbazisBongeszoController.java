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
    @FXML private Button           btnVegrehajt;
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
    void sortValtoztat() {

    }

    @FXML
    void sortorles() {

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
        btnVegrehajt.setText(FoablakController.adatbazisBongeszoFelirat[9]);
        oNevelo.setText(FoablakController.adatbazisBongeszoFelirat[10]);
        oSzo.setText(FoablakController.adatbazisBongeszoFelirat[11]);
        oForditas.setText(FoablakController.adatbazisBongeszoFelirat[12]);
        oMondat.setText(FoablakController.adatbazisBongeszoFelirat[13]);
        lblNevelo.setText(FoablakController.adatbazisBongeszoFelirat[14]);
        lblSzo.setText(FoablakController.adatbazisBongeszoFelirat[15]);
        lblForditas.setText(FoablakController.adatbazisBongeszoFelirat[16]);
        lblMondat.setText(FoablakController.adatbazisBongeszoFelirat[17]);
        btnValtoztat.setText(FoablakController.adatbazisBongeszoFelirat[18]);
        btnSorTorlese.setText(FoablakController.adatbazisBongeszoFelirat[19]);
        
        
        
        cbxStatus.setValue(FoablakController.adatbazisBongeszoFelirat[2]);
        
        listener = (v, regi, uj) -> {
            
            if (uj == null) txfNevelo.setText(uj.getNevelo());
            if (uj.getSzo()!= null) txfSzo.setText(uj.getSzo());
            if (uj.getForditas()!= null) txfForditas.setText(uj.getForditas());
            if (uj.getMondat()!= null) txaMondat.setText(uj.getMondat());
            
        };
        
    }    
    
}
