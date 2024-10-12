package PDBExplorer.window;

import PDBExplorer.model.PDBReadService;
import PDBExplorer.model.PDBWebClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class WindowController implements Initializable {

    ObservableList<String> colorSchemeList = FXCollections.observableArrayList("By atom species", "By amino acid", "By secondary structure", "By polymer");

    ObservableList<String> polymers = FXCollections.observableArrayList();

    ObservableList<String> conformationList = FXCollections.observableArrayList();
    @FXML
    private MenuButton polymerMenuButton;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private CheckBox ribbonCheckBox;

    @FXML
    private CheckBox ballsBox;


    @FXML
    private Slider ballsSlider;

    @FXML
    private Button cancelButton;

    @FXML
    private AnchorPane chartPane;

    @FXML
    private Tab chartTab;

    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private ComboBox<String> conformationComboBox;

    @FXML
    private Button connectButton;

    @FXML
    private MenuItem copyMenuItem;

    @FXML
    private CheckMenuItem darkmodeMenuItem;

    @FXML
    private Button explodeButton;

    @FXML
    private MenuItem fullscreenMenuItem;

    @FXML
    private MenuItem openMenuItem;

    @FXML
    private ListView<String> pdbList;

    @FXML
    private Pane pdbTextPane;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Pane chartPaneContent;

    @FXML
    private MenuItem redoMenuItem;

    @FXML
    private Button ribbonButton;


    @FXML
    private MenuItem saveMenuItem;

    @FXML
    private ScrollPane scrollPaneCharts;


    @FXML
    private TextField searchField;

    @FXML
    private CheckBox sticksBox;


    @FXML
    private Slider sticksSlider;

    @FXML
    private Pane structurePane;

    @FXML
    private TabPane tabPane;

    @FXML
    private TextArea textAreaStructure;
    @FXML
    private ScrollPane textScrollPane;



    @FXML
    private MenuItem undoMenuItem;
    @FXML
    private TextArea pdbTextArea;
    ObservableList<String> pdbs;
    FilteredList<String> filteredList;




    public MenuItem getAboutMenuItem() {
        return aboutMenuItem;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public CheckBox getBallsBox() {
        return ballsBox;
    }

    public ObservableList<String> getPolymers() {
        return polymers;
    }

    public Slider getBallsSlider() {
        return ballsSlider;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public ObservableList<String> getConformationList() {
        return conformationList;
    }

    public MenuButton getPolymerMenuButton() {
        return polymerMenuButton;
    }

    public AnchorPane getChartPane() {
        return chartPane;
    }

    public Tab getChartTab() {
        return chartTab;
    }

    public MenuItem getCloseMenuItem() {
        return closeMenuItem;
    }

    public Button getConnectButton() {
        return connectButton;
    }


    public MenuItem getCopyMenuItem() {
        return copyMenuItem;
    }

    public CheckMenuItem getDarkmodeMenuItem() {
        return darkmodeMenuItem;
    }

    public Button getExplodeButton() {
        return explodeButton;
    }

    public MenuItem getFullscreenMenuItem() {
        return fullscreenMenuItem;
    }

    public MenuItem getOpenMenuItem() {
        return openMenuItem;
    }

    public ListView<?> getPdbList() {
        return pdbList;
    }

    public Pane getPdbTextPane() {
        return pdbTextPane;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public MenuItem getRedoMenuItem() {
        return redoMenuItem;
    }

    public Button getRibbonButton() {
        return ribbonButton;
    }


    public MenuItem getSaveMenuItem() {
        return saveMenuItem;
    }

    public ScrollPane getScrollPaneCharts() {
        return scrollPaneCharts;
    }

    public Pane getChartPaneContent() {
        return chartPaneContent;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public CheckBox getSticksBox() {
        return sticksBox;
    }


    public Slider getSticksSlider() {
        return sticksSlider;
    }

    public Pane getStructurePane() {
        return structurePane;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public TextArea getTextAreaStructure() {
        return textAreaStructure;
    }

    public MenuItem getUndoMenuItem() {
        return undoMenuItem;
    }

    public ScrollPane getTextScrollPane() {
        return textScrollPane;
    }

    public TextArea getPdbTextArea() {
        return pdbTextArea;
    }

    public ObservableList<String> getPdbs() {
        return pdbs;
    }

    public FilteredList<String> getFilteredList() {
        return filteredList;
    }

    public CheckBox getRibbonCheckBox() {
        return ribbonCheckBox;
    }

    @FXML
    private TextFlow textFlowPane = new TextFlow();

    public ComboBox<String> getConformationComboBox() {
        return conformationComboBox;
    }

    public TextFlow getTextFlowPane() {
        return textFlowPane;
    }

    public ComboBox<String> getComboBox() {
        return comboBox;
    }


    // Upon initialization, the combobox-menu for the color schemes is being filled and the PDBWebClient is being employed, in order to load
    // the observable array with all pdb files, which is then packed in a filtered list. This filtered list is finally packed into the listview.
    // The filtered list also enables the search function.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        comboBox.setItems(colorSchemeList);
        comboBox.getSelectionModel().selectFirst();


        PDBWebClient pdb = new PDBWebClient();

        try {
            pdbs = FXCollections.observableArrayList(pdb.call());
            filteredList = new FilteredList<>(pdbs, s -> true);


            pdbList.setItems(filteredList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

