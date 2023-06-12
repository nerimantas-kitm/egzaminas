package lt.kitm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lt.kitm.dto.FilmasDTO;
import lt.kitm.model.Kategorija;
import lt.kitm.model.Filmas;
import lt.kitm.model.FilmasDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RedaguotiFilmaController {
    @FXML
    private TextField isbn;
    @FXML
    private TextField pavadinimas;
    @FXML
    private TextArea santrauka;
    @FXML
    private Spinner<Integer> puslapiu_skaicius;
    @FXML
    private ComboBox<String> kategorija;
    @FXML
    private Label zinute;
    private FilmasDTO filmas;
    private ArrayList<Kategorija> kategorijos;
    private boolean atnaujinta = false;

    public void uzkrautiDuomenis(FilmasDTO knyga, ArrayList<Kategorija> kategorijos) {
        this.filmas = knyga;
        // Sudeda kategorijas į ComboBox
        this.kategorijos = kategorijos;
        kategorija.getItems().addAll(
                kategorijos.stream()
                        .map(Kategorija::getPavadinimas)
                        .collect(Collectors.toList()
                ));
        this.isbn.setText(knyga.getIsbn());
        this.pavadinimas.setText(knyga.getPavadinimas());
        this.santrauka.setText(knyga.getSantrauka());
        this.puslapiu_skaicius.getValueFactory().setValue(knyga.getPuslapiuSkaicius());
        this.kategorija.setValue(knyga.getKategorijosPavadinimas());
    }

    public boolean isAtnaujinta() {
        return this.atnaujinta;
    }

    public void onActionAtsaukti(ActionEvent actionEvent) {
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    public void onActionAtnaujinti(ActionEvent actionEvent) {
        //TODO: reikia patikrinti ar laukeliai tušti
        try {
            Filmas filmas = new Filmas(this.filmas.getId(), this.isbn.getText(), this.pavadinimas.getText(), this.santrauka.getText(), "", this.puslapiu_skaicius.getValue());
            if (!this.kategorija.getSelectionModel().isEmpty()) {
                int pasirinktaKategorija = kategorijos.stream()
                        .filter(k -> k.getPavadinimas().equals(this.kategorija.getValue()))
                        .findFirst().get().getId();
                filmas.setKategorijosId(pasirinktaKategorija);
            }
            FilmasDAO.update(filmas);
            this.atnaujinta = true;
            this.onActionAtsaukti(actionEvent);
        } catch (SQLException e) {
            //TODO: žinutė jei klaida
            e.printStackTrace();
        }
    }
}
