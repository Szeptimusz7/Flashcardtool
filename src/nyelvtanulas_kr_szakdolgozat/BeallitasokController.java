package nyelvtanulas_kr_szakdolgozat;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import static nyelvtanulas_kr_szakdolgozat.FoablakController.uzenetek;
import static nyelvtanulas_kr_szakdolgozat.Panel.hiba;

/**
 *
 * @author Kremmer Róbert
 */
public class BeallitasokController implements Initializable {
    
    @FXML
    private Label            lblForrasNyelv;
    @FXML
    private Label            lblCelnyelv;
    @FXML
    private Label            lblBeallitasok;
    @FXML
    private Label            lblSorokSzama;
    @FXML
    private Button           btnMentes;
    @FXML
    private Button           btnMegse;
    @FXML
    private TextField        txtSorokSzama;
    @FXML
    private ComboBox<String> cbxForrasNyelv;
    @FXML
    private ComboBox<String> cbxCelnyelv;
    @FXML
    private Label            lblFeluletNyelve;
    @FXML
    private ComboBox<String> cbxFeluletNyelve;
    
    
    @FXML
    void ment() {
        String feluletNyelve  = cbxFeluletNyelve.getValue();
        String forrasNyelvKod = FoablakController.nyelvekKodja.get(cbxForrasNyelv.getValue());
        String celNyelvKod    = FoablakController.nyelvekKodja.get(cbxCelnyelv.getValue());
        
        int sorokSzama;
        try {
            sorokSzama = Integer.parseInt(txtSorokSzama.getText());
        } catch (NumberFormatException e) {
            hiba(uzenetek.get("hiba"),uzenetek.get("nemszam"));
            return;
        }
        
        if (feluletNyelve == null || celNyelvKod == null || forrasNyelvKod == null) {
            hiba(uzenetek.get("hiba"),uzenetek.get("adjonmegmindenadatot"));
            return;
        }
        
        FoablakController.feluletNyelveKod = FoablakController.nyelvekKodja.get(feluletNyelve);
        FoablakController.forrasNyelvKod   = forrasNyelvKod;
        FoablakController.celNyelvKod      = celNyelvKod;
        FoablakController.beolvasottSorokSzama = sorokSzama;
        
        DB.beallitastModosit("feluletNyelveSetting", FoablakController.nyelvekKodja.get(feluletNyelve));
        DB.beallitastModosit("forrasNyelvSetting", forrasNyelvKod);
        DB.beallitastModosit("celNyelvSetting", celNyelvKod);
        DB.beallitastModosit("sorokSzamaSetting", (sorokSzama + ""));
        
        Window ablak = cbxCelnyelv.getScene().getWindow();
        ablak.hide();
    }
    
    @FXML
    void megse() {
        Window ablak = cbxCelnyelv.getScene().getWindow();
        ablak.hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        lblBeallitasok.setText(FoablakController.beallitasokFelirat[0]);
        lblFeluletNyelve.setText(FoablakController.beallitasokFelirat[1]);
        lblCelnyelv.setText(FoablakController.beallitasokFelirat[2]);
        lblSorokSzama.setText(FoablakController.beallitasokFelirat[3]);
        btnMentes.setText(FoablakController.beallitasokFelirat[4]);
        btnMegse.setText(FoablakController.beallitasokFelirat[5]);
        lblForrasNyelv.setText(FoablakController.beallitasokFelirat[6]);
        
        cbxFeluletNyelve.getItems().addAll(FoablakController.nyelvek);
        cbxForrasNyelv.getItems().addAll(FoablakController.nyelvek);
        cbxCelnyelv.getItems().addAll(FoablakController.nyelvek);
        
        
        cbxFeluletNyelve.setValue(FoablakController.feluletNyelvenekNeveAdottNyelven);
        cbxForrasNyelv.setValue(FoablakController.kodhozNyelv.get(FoablakController.forrasNyelvKod));

        cbxCelnyelv.setValue(FoablakController.kodhozNyelv.get(FoablakController.celNyelvKod));
        txtSorokSzama.setText(FoablakController.beolvasottSorokSzama + "");
    }
    
}
