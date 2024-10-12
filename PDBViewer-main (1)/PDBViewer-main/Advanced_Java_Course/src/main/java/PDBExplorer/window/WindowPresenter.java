package PDBExplorer.window;
import PDBExplorer.model.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.SetChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.util.*;

import static PDBExplorer.model.PDBWebClient.retrievePDB;
import static PDBExplorer.model.PdbIO.*;
import static PDBExplorer.window.AAChart.aminoAcidChartProducer;
import static PDBExplorer.window.Ball.selectionModel;
import static PDBExplorer.window.MoleculeFigure.*;
import static PDBExplorer.window.PolymerChart.polymerChartProducer;
import static PDBExplorer.window.SelectionModelClass.*;

public class WindowPresenter {

    Timeline rotateTimeLine;


    SequentialTransition seq = new SequentialTransition();
    PerspectiveCamera camera = new PerspectiveCamera(true);
    double prevX;
    double prevY;

    HashMap<PdbComplex, ArrayList<Group>> complexToNormalBalls = new HashMap<PdbComplex, ArrayList<Group>>();
    HashMap<PdbComplex, ArrayList<Group>> complexToMonoBalls = new HashMap<PdbComplex, ArrayList<Group>>();
    HashMap<PdbComplex, ArrayList<Group>> complexToSecBalls = new HashMap<PdbComplex, ArrayList<Group>>();
    HashMap<PdbComplex, ArrayList<Group>> complexToPolyBalls = new HashMap<PdbComplex, ArrayList<Group>>();
    HashMap<PdbComplex, ArrayList<Group>> complexToSticks = new HashMap<PdbComplex, ArrayList<Group>>();
    HashMap<PdbComplex, ArrayList<MeshView>> complexToRibbons = new HashMap<PdbComplex, ArrayList<MeshView>>();



    RotateTransition rotateTransition = new RotateTransition();

    TranslateTransition path = new TranslateTransition();


    Point3D orthogonalAxis;

    UndoRedoManager undoManager = new UndoRedoManager();



    HashMap<Integer, ArrayList<Group>> numberToBallsGroup = new HashMap<>();

    HashMap<Integer, Group> numberToSticksGroup = new HashMap<>();

    HashMap<Integer, MeshView> numberToRibbonGroup = new HashMap<>();

    HashMap<Integer, MoleculeFigure> numberToMoleculeFigure = new HashMap<>();

    HashMap<PdbComplex, Group> complexToMono = new HashMap<>();
    HashMap<PdbComplex, Group> complexToNormal = new HashMap<>();
    HashMap<PdbComplex, Group> complexToSec = new HashMap<>();
    HashMap<PdbComplex, Group> complexToPoly = new HashMap<>();
    HashMap<PdbComplex, Group> complexToStickGroup = new HashMap<>();
    HashMap<PdbComplex, Group> complexToRibbonGroup = new HashMap<>();


    HashMap<PdbComplex, MoleculeFigure> complextoMoleculeFigure = new HashMap<>();
    Group figure = new Group();

    Rotate rotate;





    static ArrayList<PdbComplex> complexList = new ArrayList<>();
    static PdbConformations conformations = new PdbConformations(complexList);

    CheckMenuItem menu;

    static ArrayList<ArrayList<PdbBond>> conformationBonds = new ArrayList<>();

    static ArrayList<MoleculeFigure> moleculeFigures = new ArrayList<>();

    int conformationSelected = 0;


    PdbComplex c;
    static Map<PdbMonomer, Text> monomerToText = new HashMap<>();
    static Map<Text, PdbMonomer> textToMonomer = new HashMap<>();

    ArrayList<Text> aminoAcidList = new ArrayList<>();
    ArrayList<Text> secStructureList = new ArrayList<>();



    // The method conformationReader analyzes the pdb-file, extracts the different conformations (based on the MODEL-keyword) by creating a new String variable with the text for this particular conformation,
    // and applies the readAtoms-method of the PdbIO-class to each of these conformations separately.
    public static void conformationReader(String pdbFile) throws IOException {


        complexList = new ArrayList<>();

        BufferedReader bf = new BufferedReader(new StringReader(pdbFile));

        String record;
        ArrayList<String[]> textArray = new ArrayList<>();
        ArrayList<String[]> searchArray = new ArrayList<>();


        int conformationCount = 0;

        while((record = bf.readLine()) != null){

            String[] line = record.split("\\n");
            String[] pdbLine = line[0].split("");

            textArray.add(pdbLine);
            searchArray.add(line);



        }

        StringBuilder newPDB = new StringBuilder();
        StringBuilder secPDB = new StringBuilder();
        int beginIndex = 0;
        int endIndex = 0;
        int indexRange = 0;
        int count = 0;
        StringBuilder totalString = new StringBuilder();


        for(int i = 0; i < textArray.size(); i++){

            String[] strArray = textArray.get(i);


            if((strArray[0]+ strArray[1]+strArray[2]+strArray[3]+strArray[4]).contains("HELIX") | (strArray[0]+ strArray[1]+strArray[2]+strArray[3]+strArray[4]).contains("SHEET")){
                String string = Arrays.toString(searchArray.get(i)).replace(",", " ").replace("[", "").replace("]", "").trim();
                secPDB.append(string);
                secPDB.append(System.lineSeparator());
            }


            else if((strArray[0]+ strArray[1]+strArray[2]+strArray[3]+strArray[4]).contains("MODEL")){


                beginIndex = i;


            }

            else if((strArray[0]+ strArray[1]+strArray[2]+strArray[3]+strArray[4] + strArray[5]).contains("ENDMDL")){


                endIndex = i;
                indexRange = endIndex - beginIndex;
                conformationCount++;



                for(int j = beginIndex; j < indexRange + beginIndex; j++){

                    String string = Arrays.toString(searchArray.get(j)).replace(",", " ").replace("[", "").replace("]", "").trim();


                    newPDB.append(string);
                    newPDB.append(System.lineSeparator());


                }

                totalString.append(secPDB.toString());

                totalString.append(newPDB);

                PdbComplex complex = readAtoms(totalString.toString());


                newPDB = new StringBuilder();
                totalString = new StringBuilder();

                complexList.add(complex);


            }



        }

        conformations = new PdbConformations(complexList);



        if(conformationCount == 0){
            PdbComplex complex = readAtoms(pdbFile);
            complexList.add(complex);

            conformations = new PdbConformations(complexList);
        }

    }

    public Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public boolean containsNucleotides(PdbComplex c){
        boolean containsNucleotides = false;

        for(int i = 0; i < c.getPolymerList().size(); i++){

            for(int j = 0; j < c.getPolymerList().get(i).getMonomerList().size(); j++){
                PdbMonomer mono = c.getPolymerList().get(i).getMonomerList().get(j);

                if(mono.getLabel().equals("DA") ){
                    containsNucleotides = true;

                }
                else if(mono.getLabel().equals("DC") ){
                    containsNucleotides = true;


                }
                else if( mono.getLabel().equals("DG") ){
                    containsNucleotides = true;

                }
                else if(mono.getLabel().equals("DT")){
                    containsNucleotides = true;

                }

            }
        }
        return containsNucleotides;
    }

    Alert containsNucleotidesAlert = new Alert(Alert.AlertType.INFORMATION);



    // The setTextFlow-method constructs the textflow for the amino-acid/nucleotide sequence and the secondary structure sequence.
    public void setTextFlow(){


        Text monomerText = new Text("Sequence: \n");
        aminoAcidList.add(monomerText);


        Text secStrucureText = new Text("Secondary Structures: \n");
        secStructureList.add(secStrucureText);



        for (int i = 0; i < c.getPolymerList().size(); i++) {

            for (int j = 0; j < c.getPolymerList().get(i).getMonomerList().size(); j++) {
                TextStructure aminoAcid = new TextStructure(new Text(c.getPolymerList().get(i).getMonomerList().get(j).getLabel()));
                aminoAcidList.add(aminoAcid.getText());
                monomerToText.put(c.getPolymerList().get(i).getMonomerList().get(j), aminoAcid.getText());
                textToMonomer.put(aminoAcid.getText(), c.getPolymerList().get(i).getMonomerList().get(j));

                if (c.getPolymerList().get(i).getMonomerList().get(j).getType().equals("null")) {

                    Text secStructure = new Text("-");
                    secStructureList.add(secStructure);

                } else {
                    if (c.getPolymerList().get(i).getMonomerList().get(j).getType().equals("Helix")) {
                        Text secStructure = new Text("H");
                        secStructureList.add(secStructure);

                    }
                    if (c.getPolymerList().get(i).getMonomerList().get(j).getType().equals("Sheet")) {
                        Text secStructure = new Text("S");
                        secStructureList.add(secStructure);

                    }
                    if (c.getPolymerList().get(i).getMonomerList().get(j).getType().equals("Turn")) {
                        Text secStructure = new Text("T");
                        secStructureList.add(secStructure);

                    }


                }


            }
        }


    }
    //static MoleculeFigure mf;

    ArrayList<CheckMenuItem> menuList;
    IntegerBinding listSize;
    BooleanBinding moreThanOneConformation;
    BooleanBinding smallerThan21;
    BooleanBinding largerThanOne;
    BooleanBinding inBetween;
    IntegerBinding conformationListSize;
    int conformationNumber = 0;


    // The setPolymers-method fills the Polymer-menu and adds listeners to each menu-item.
    public void setPolymers(){
        numberToBallsGroup = new HashMap<>();
        numberToSticksGroup = new HashMap<>();
        menuList = new ArrayList<>();

        for(int i = 0; i < c.getPolymerList().size(); i++){
            CheckMenuItem menu1 = new CheckMenuItem();
            String menuText = "Polymer " + (i + 1);
            String[] array = menuText.split("\\s");
            menu1.setSelected(true);

            menu1.setText(menuText);
            ArrayList<Group> groupList = new ArrayList<>();



            menu1.selectedProperty().addListener((v, o, n) -> {


                int menuNumber = 0;
                String[] menuArray = menu1.getText().split("\\s");

                menuNumber = Integer.parseInt(menuArray[1]) - 1;



                if(menu1.isSelected()){


                    ((Group) balls.getBalls().getChildren().get(conformationNumber)).getChildren().get(menuNumber).setVisible(true);

                    ((Group) monomerBalls.getChildren().get(conformationNumber)).getChildren().get(menuNumber).setVisible(true);


                    ((Group) secBalls.getChildren().get(conformationNumber)).getChildren().get(menuNumber).setVisible(true);


                    ((Group) polymerBalls.getChildren().get(conformationNumber)).getChildren().get(menuNumber).setVisible(true);


                    ((Group) sticks.getChildren().get(conformationNumber)).getChildren().get(menuNumber).setVisible(true);

                    if(menuNumber < ((Group) ribbonGroup.getChildren().get(conformationNumber)).getChildren().size()){
                        ((Group) ribbonGroup.getChildren().get(conformationNumber)).getChildren().get(menuNumber).setVisible(true);
                    }



                }
                if(!menu1.isSelected()){


                    ((Group) balls.getBalls().getChildren().get(conformationNumber)).getChildren().get(menuNumber).setVisible(false);

                    ((Group) monomerBalls.getChildren().get(conformationNumber)).getChildren().get(menuNumber).setVisible(false);


                    ((Group) secBalls.getChildren().get(conformationNumber)).getChildren().get(menuNumber).setVisible(false);


                    ((Group) polymerBalls.getChildren().get(conformationNumber)).getChildren().get(menuNumber).setVisible(false);


                    ((Group) sticks.getChildren().get(conformationNumber)).getChildren().get(menuNumber).setVisible(false);


                    if(menuNumber < ((Group) ribbonGroup.getChildren().get(conformationNumber)).getChildren().size()){
                        ((Group) ribbonGroup.getChildren().get(conformationNumber)).getChildren().get(menuNumber).setVisible(false);

                    }



                }
                undoManager.add(new PropertyCommand<>("polymer", (BooleanProperty) v, o, n));

            });


            menuList.add(menu1);



        }


    }

    BallGroup balls = new BallGroup();
    Group sticks = new Group();
    Group polymerBalls = new Group();
    Group monomerBalls = new Group();
    Group secBalls = new Group();
    Group ribbonGroup = new Group();

    Alert notAProteinAlert = new Alert(Alert.AlertType.WARNING);

    // isProtein() checks if a certain pdb-file actually contains a protein. A file is considered to contain a protein when at least 1 amino-acid is present
    public boolean isProtein(PdbComplex complex){
        boolean isProtein = false;

        for(int i = 0; i < complex.getPolymerList().size(); i++){

            for(int j = 0; j < complex.getPolymerList().get(i).getMonomerList().size(); j++){

                String label = complex.getPolymerList().get(i).getMonomerList().get(j).getLabel();

                if(label.equals("A") | label.equals("R")| label.equals("N") | label.equals("D") | label.equals("C")| label.equals("E") | label.equals("Q") | label.equals("G")| label.equals("H") | label.equals("I") | label.equals("L")| label.equals("K") | label.equals("M") | label.equals("F")| label.equals("P") | label.equals("S") | label.equals("T")| label.equals("W") | label.equals("Y") | label.equals("V")){
                    isProtein = true;
                    break;
                }



            }
            if(isProtein){
                break;
            }
        }

        return isProtein;

    }
    boolean isProtein;


    public WindowPresenter(Stage stage, WindowView wv) {

        var controller = wv.getController();
        SubScene subScene = new SubScene(figure, controller.getStructurePane().getWidth(), controller.getStructurePane().getHeight(), true, SceneAntialiasing.BALANCED);

        var root = wv.getRoot();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);


        // Listener for close-menuitem
        controller.getCloseMenuItem().setOnAction(e -> {
            Platform.exit();

        });

        // Listener for fullscreen-menuitem
        controller.getFullscreenMenuItem().setOnAction(e -> {
            stage.setFullScreen(true);
        });

        // Listener for copy-menuitem
        controller.getCopyMenuItem().setOnAction(e -> {
            var content = new ClipboardContent();
            content.putImage(controller.getTabPane().snapshot(null, null));
            Clipboard.getSystemClipboard().setContent(content);

        });
        // Listener for About-menuitem
        controller.getAboutMenuItem().setOnAction(e -> {
            alert.setContentText("This is a program for viewing proteins");
            alert.show();


        });

        // Cancel-button and progress bar are first set to disabled and invisible, respectively
        controller.getCancelButton().disableProperty().set(true);
        controller.getProgressBar().visibleProperty().set(false);


        // After double-clicking on an entry in the pdb listview, a molecule is loaded
        controller.getPdbList().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (mouseEvent.getClickCount() == 2) {
                    isProtein = true;

                    // All windows, groups, etc. are cleared when loading a new protein
                    controller.getComboBox().getSelectionModel().selectFirst();
                    controller.getStructurePane().getChildren().clear();
                    figure.getChildren().clear();
                    controller.getConformationList().clear();
                    conformations.getComplexList().clear();
                    complexList.clear();
                    moleculeFigures.clear();
                    controller.getSticksSlider().setValue(0);
                    controller.getBallsSlider().setValue(0);

                    balls.getBalls().getChildren().clear();
                    sticks.getChildren().clear();
                    ribbonGroup.getChildren().clear();
                    polymerBalls.getChildren().clear();
                    monomerBalls.getChildren().clear();
                    secBalls.getChildren().clear();
                    controller.getTextFlowPane().getChildren().clear();
                    controller.getPdbTextArea().clear();
                    String item = (String) controller.getPdbList().getSelectionModel().getSelectedItem();
                    controller.getChartPaneContent().getChildren().clear();

                    // figureService retrieves the text in the pdb-file, reads the different conformations and creates a
                    // MoleculeFigure for each conformation. This is computationally quite intensive, which is why it is done in a separate thread.
                    Service<Void> figureService = new Service<Void>() {
                        @Override
                        protected Task<Void> createTask() {

                            return new Task<Void>() {
                                @Override
                                protected Void call() throws Exception {

                                    String text = retrievePDB(item);
                                    controller.getPdbTextArea().setText(text);

                                    conformationReader(text);



                                    for(int i = 0; i < conformations.getComplexList().size(); i++){

                                        MoleculeFigure mf = new MoleculeFigure();



                                        moleculeFigures.add(mf.compute(conformations.getComplexList().get(i)));
                                        complextoMoleculeFigure.put(conformations.getComplexList().get(i), moleculeFigures.get(i));

                                    }

                                    // All elements of each MoleculeFigure (normal balls, monomer balls, polymer balls, secondary structure balls, sticks, and ribbons) are
                                    // added to the WindowPresenter groups. There is a separate ball set for each color scheme.
                                    for(int i = 0; i < conformations.getComplexList().size(); i++){


                                        monomerBalls.getChildren().add(complextoMoleculeFigure.get(conformations.getComplexList().get(i)).monomerBallsFigure);
                                        secBalls.getChildren().add(complextoMoleculeFigure.get(conformations.getComplexList().get(i)).secBallsFigure);
                                        polymerBalls.getChildren().add(complextoMoleculeFigure.get(conformations.getComplexList().get(i)).polymerBallsFigure);
                                        balls.getBalls().getChildren().add(complextoMoleculeFigure.get(conformations.getComplexList().get(i)).ballsFigure);

                                        sticks.getChildren().add(complextoMoleculeFigure.get(conformations.getComplexList().get(i)).sticksFigure);
                                        ribbonGroup.getChildren().add(complextoMoleculeFigure.get(conformations.getComplexList().get(i)).ribbonGroupFigure);

                                    }


                                    controller.getConformationComboBox().setItems(controller.getConformationList());

                                    numberToMoleculeFigure = new HashMap<>();

                                    // Conformation menu-items are set
                                    for(int i = 0; i < moleculeFigures.size(); i++){

                                        String boxItem = "Conformation " + (i+1);

                                        controller.getConformationList().add(boxItem);

                                        numberToMoleculeFigure.put(i+1, moleculeFigures.get(i));
                                    }



                                    return null;
                                }
                            };
                        }
                    };


                    figureService.start();


                    // Set functionality of cancel-button
                    controller.getCancelButton().setOnAction(action -> {
                        figureService.cancel();

                    });
                    // Properties of cancel-button and progress bar are bound
                    controller.getCancelButton().disableProperty().bind(figureService.runningProperty().not());
                    controller.getProgressBar().visibleProperty().bind(figureService.runningProperty());
                    controller.getProgressBar().progressProperty().bind(figureService.progressProperty());
                    controller.getPdbList().disableProperty().bind(figureService.runningProperty());
                    controller.getSaveMenuItem().disableProperty().bind(figureService.runningProperty());
                    controller.getOpenMenuItem().disableProperty().bind(figureService.runningProperty());



                    figureService.setOnSucceeded(b -> {

                        // If a file does not contain a protein (if the first conformation does not contain an amino-acid, it is assumed that all further conformations also do not contain amino-acids),
                        // then a warning is shown and the molecule is not displayed (only the text of the pdb-file)
                        if(!isProtein(conformations.getComplexList().get(0))){
                            notAProteinAlert.setContentText("This file does not contain a protein structure and its structure can therefore not be displayed");
                            notAProteinAlert.show();

                            controller.getComboBox().getSelectionModel().selectFirst();
                            controller.getStructurePane().getChildren().clear();
                            figure.getChildren().clear();
                            controller.getConformationList().clear();
                            conformations.getComplexList().clear();
                            complexList.clear();
                            moleculeFigures.clear();
                            controller.getSticksSlider().setValue(0);
                            controller.getBallsSlider().setValue(0);

                            balls.getBalls().getChildren().clear();
                            sticks.getChildren().clear();
                            ribbonGroup.getChildren().clear();
                            polymerBalls.getChildren().clear();
                            monomerBalls.getChildren().clear();
                            secBalls.getChildren().clear();
                            controller.getTextFlowPane().getChildren().clear();
                            //controller.getPdbTextArea().clear();
                            controller.getChartPaneContent().getChildren().clear();
                            isProtein = false;
                        }



                        controller.getPolymerMenuButton().getItems().clear();
                        controller.getPolymers().clear();

                        // If the molecule is actually a protein, the first of its conformations is selected and will be displayed.
                        if(isProtein){
                            controller.getConformationComboBox().getSelectionModel().select(0);

                            if(containsNucleotides(conformations.getComplexList().get(0))){
                                containsNucleotidesAlert.setContentText("This molecule contains nucleotides. Ribbons will not be shown for nucleotide sections of the molecule");
                                containsNucleotidesAlert.show();
                            }

                            // The polymer menubutton is loaded with the polymers for this particular conformation
                            for(CheckMenuItem menu : menuList){

                                controller.getPolymerMenuButton().getItems().add(menu);

                                controller.getPolymers().add(menu.getText());


                            }
                            // Bindings for polymer-menubutton and explode-button
                            listSize = Bindings.size(controller.getPolymers());
                            largerThanOne = listSize.greaterThan(1);
                            smallerThan21 = listSize.greaterThan(20).not();
                            inBetween = largerThanOne.and(smallerThan21);

                            controller.getPolymerMenuButton().disableProperty().bind(largerThanOne.not());
                            controller.getExplodeButton().disableProperty().bind(inBetween.not());


                            // Add all groups to the figure and set some to invisible
                            figure.getChildren().add(balls.getBalls());
                            figure.getChildren().add(sticks);
                            figure.getChildren().add(ribbonGroup);
                            ribbonGroup.setVisible(false);
                            figure.getChildren().add(monomerBalls);
                            monomerBalls.setVisible(false);
                            figure.getChildren().add(secBalls);
                            secBalls.setVisible(false);
                            figure.getChildren().add(polymerBalls);
                            polymerBalls.setVisible(false);

                            // Produce the charts for this particular molecule
                            controller.getChartPaneContent().getChildren().add(aminoAcidChartProducer(c));
                            controller.getChartPaneContent().getChildren().add(polymerChartProducer(c));


                            controller.getTextScrollPane().setFitToWidth(true);




                            camera = new PerspectiveCamera(true);


                            camera.setNearClip(0.1);
                            camera.setFarClip(10000);
                            camera.setTranslateZ(-10);


                            subScene.setCamera(camera);

                            controller.getStructurePane().getChildren().add(subScene);
                            subScene.heightProperty().bind(controller.getStructurePane().heightProperty());
                            subScene.widthProperty().bind(controller.getStructurePane().widthProperty());

                            controller.getSticksBox().setSelected(true);
                            controller.getBallsBox().setSelected(true);
                            controller.getRibbonCheckBox().setSelected(false);
                        }



                    });


                }

            }

        });



        // Set actionlisteners for undo and redo buttons
        controller.getUndoMenuItem().setOnAction(e -> undoManager.undo());
        controller.getUndoMenuItem().textProperty().bind(undoManager.undoLabelProperty());
        controller.getUndoMenuItem().disableProperty().bind(undoManager.canUndoProperty().not());

        controller.getRedoMenuItem().setOnAction(e -> undoManager.redo());
        controller.getRedoMenuItem().textProperty().bind(undoManager.redoLabelProperty());
        controller.getRedoMenuItem().disableProperty().bind(undoManager.canRedoProperty().not());


        // Zoom-in and zoom-out functionality, taken from lecture
        controller.getStructurePane().setOnScroll(e -> {
            var dy = e.getDeltaY();
            if (dy > 0) {
                camera.setTranslateZ(1.1 * camera.getTranslateZ());
            }
            if (dy < 0) {
                camera.setTranslateZ(1 / 1.1 * camera.getTranslateZ());
            }

        });
        

        // Explode-button functionality: if the animation is not running, a list of different xyz-directions is generated.
        // Each ball/stick/ribbon group of each polymer is assigned a direction and the animation is executed in a sequential form with a pause in the middle
        // If the polymer gets too large (more than 20 molecules), the explode-button is disabled, as exploding very large molecules becomes quite chaotic and
        // an index out of bounds would be generated for the directions list.
        controller.getExplodeButton().setOnAction(e -> {
            MeshView ribbons = null;

            if(seq.getStatus() != Animation.Status.RUNNING) {

                ArrayList<Integer> list1 = new ArrayList<>();
                list1.add(30);
                list1.add(30);
                list1.add(30);

                ArrayList<Integer> list2 = new ArrayList<>();
                list2.add(-30);
                list2.add(-30);
                list2.add(-30);

                ArrayList<Integer> list3 = new ArrayList<>();
                list3.add(30);
                list3.add(30);
                list3.add(-30);

                ArrayList<Integer> list4 = new ArrayList<>();
                list4.add(-30);
                list4.add(-30);
                list4.add(30);

                ArrayList<Integer> list5 = new ArrayList<>();
                list5.add(0);
                list5.add(30);
                list5.add(0);

                ArrayList<Integer> list6 = new ArrayList<>();
                list6.add(0);
                list6.add(-30);
                list6.add(0);

                ArrayList<Integer> list7 = new ArrayList<>();
                list7.add(-30);
                list7.add(0);
                list7.add(0);

                ArrayList<Integer> list8 = new ArrayList<>();
                list8.add(30);
                list8.add(0);
                list8.add(0);

                ArrayList<Integer> list9 = new ArrayList<>();
                list9.add(0);
                list9.add(0);
                list9.add(30);

                ArrayList<Integer> list10 = new ArrayList<>();
                list10.add(0);
                list10.add(0);
                list10.add(-30);


                ArrayList<Integer> list11 = new ArrayList<>();
                list11.add(60);
                list11.add(60);
                list11.add(60);

                ArrayList<Integer> list12 = new ArrayList<>();
                list12.add(-60);
                list12.add(-60);
                list12.add(-60);

                ArrayList<Integer> list13 = new ArrayList<>();
                list13.add(60);
                list13.add(60);
                list13.add(-60);

                ArrayList<Integer> list14 = new ArrayList<>();
                list14.add(-60);
                list14.add(-60);
                list14.add(60);

                ArrayList<Integer> list15 = new ArrayList<>();
                list15.add(0);
                list15.add(60);
                list15.add(0);

                ArrayList<Integer> list16 = new ArrayList<>();
                list16.add(0);
                list16.add(-60);
                list16.add(0);

                ArrayList<Integer> list17 = new ArrayList<>();
                list17.add(-60);
                list17.add(0);
                list17.add(0);

                ArrayList<Integer> list18 = new ArrayList<>();
                list18.add(60);
                list18.add(0);
                list18.add(0);

                ArrayList<Integer> list19 = new ArrayList<>();
                list19.add(0);
                list19.add(0);
                list19.add(60);

                ArrayList<Integer> list20 = new ArrayList<>();
                list20.add(0);
                list20.add(0);
                list20.add(-60);

                ArrayList<ArrayList<Integer>> directionList = new ArrayList<>();
                directionList.add(list1);
                directionList.add(list2);
                directionList.add(list3);
                directionList.add(list4);
                directionList.add(list5);
                directionList.add(list6);
                directionList.add(list7);
                directionList.add(list8);
                directionList.add(list9);
                directionList.add(list10);
                directionList.add(list11);
                directionList.add(list12);
                directionList.add(list13);
                directionList.add(list14);
                directionList.add(list15);
                directionList.add(list16);
                directionList.add(list17);
                directionList.add(list18);
                directionList.add(list19);
                directionList.add(list20);




                for (int i = 0; i < c.getPolymerList().size(); i++) {

                    path = new TranslateTransition(Duration.seconds(3));
                    TranslateTransition path1 = new TranslateTransition(Duration.seconds(3));
                    TranslateTransition path2 = new TranslateTransition(Duration.seconds(3));
                    TranslateTransition path3 = new TranslateTransition(Duration.seconds(3));
                    TranslateTransition path4 = new TranslateTransition(Duration.seconds(3));
                    TranslateTransition path5 = new TranslateTransition(Duration.seconds(3));

                    TranslateTransition pathMinus = new TranslateTransition(Duration.seconds(3));
                    TranslateTransition pathMinus1 = new TranslateTransition(Duration.seconds(3));
                    TranslateTransition pathMinus2 = new TranslateTransition(Duration.seconds(3));
                    TranslateTransition pathMinus3 = new TranslateTransition(Duration.seconds(3));
                    TranslateTransition pathMinus4 = new TranslateTransition(Duration.seconds(3));
                    TranslateTransition pathMinus5 = new TranslateTransition(Duration.seconds(3));
                    PauseTransition pause = new PauseTransition();
                    pause.setDuration(Duration.seconds(1));


                    seq = new SequentialTransition(path, pause, pathMinus);
                    SequentialTransition seq1 = new SequentialTransition(path1, pause, pathMinus1);
                    SequentialTransition seq2 = new SequentialTransition(path2, pause, pathMinus2);
                    SequentialTransition seq3 = new SequentialTransition(path3, pause, pathMinus3);
                    SequentialTransition seq4 = new SequentialTransition(path4, pause, pathMinus4);
                    SequentialTransition seq5 = new SequentialTransition(path5, pause, pathMinus5);





                    Group normalBall = (Group) ((Group) balls.getBalls().getChildren().get(conformationNumber)).getChildren().get(i);
                    Group monoBall = (Group) ((Group) monomerBalls.getChildren().get(conformationNumber)).getChildren().get(i);
                    Group secBall = (Group) ((Group) secBalls.getChildren().get(conformationNumber)).getChildren().get(i);
                    Group polyBall = (Group) ((Group) polymerBalls.getChildren().get(conformationNumber)).getChildren().get(i);
                    Group stickGroup = (Group) ((Group) sticks.getChildren().get(conformationNumber)).getChildren().get(i);
                    if(i < ((Group) ribbonGroup.getChildren().get(conformationNumber)).getChildren().size()){
                        ribbons = (MeshView) ((Group) ribbonGroup.getChildren().get(conformationNumber)).getChildren().get(i);
                        path5.setNode(ribbons);
                        pathMinus5.setNode(ribbons);
                        path5.setFromX(ribbons.getTranslateX());
                        path5.setFromY(ribbons.getTranslateY());
                        path5.setFromZ(ribbons.getTranslateZ());

                        path5.setToX(ribbons.getTranslateX() + directionList.get(i).get(0));
                        path5.setToY(ribbons.getTranslateY() + directionList.get(i).get(1));
                        path5.setToZ(ribbons.getTranslateZ() + directionList.get(i).get(2));

                        pathMinus5.setFromX(ribbons.getTranslateX() + directionList.get(i).get(0));
                        pathMinus5.setFromY(ribbons.getTranslateY() + directionList.get(i).get(1));
                        pathMinus5.setFromZ(ribbons.getTranslateZ() + directionList.get(i).get(2));

                        pathMinus5.setToX(ribbons.getTranslateX());
                        pathMinus5.setToY(ribbons.getTranslateY() );
                        pathMinus5.setToZ(ribbons.getTranslateZ() );



                    }

                    path.setNode(normalBall);
                    path1.setNode(monoBall);
                    path2.setNode(secBall);
                    path3.setNode(polyBall);
                    path4.setNode(stickGroup);

                    pathMinus.setNode(normalBall);
                    pathMinus1.setNode(monoBall);
                    pathMinus2.setNode(secBall);
                    pathMinus3.setNode(polyBall);
                    pathMinus4.setNode(stickGroup);

                    path.setAutoReverse(false);
                    path.setCycleCount(1);
                    path1.setAutoReverse(false);
                    path1.setCycleCount(1);
                    path2.setAutoReverse(false);
                    path2.setCycleCount(1);
                    path3.setAutoReverse(false);
                    path3.setCycleCount(1);
                    path4.setAutoReverse(false);
                    path4.setCycleCount(1);
                    path5.setAutoReverse(false);
                    path5.setCycleCount(1);


                    double normalX = normalBall.getTranslateX();
                    double normalY = normalBall.getTranslateY();
                    double normalZ = normalBall.getTranslateY();



                    path.setFromX(normalBall.getTranslateX());
                    path.setFromY(normalBall.getTranslateY());
                    path.setFromZ(normalBall.getTranslateZ());

                    path.setToX(normalBall.getTranslateX() + directionList.get(i).get(0));
                    path.setToY(normalBall.getTranslateY() + directionList.get(i).get(1));
                    path.setToZ(normalBall.getTranslateZ() + directionList.get(i).get(2));

                    pathMinus.setFromX(normalBall.getTranslateX() + directionList.get(i).get(0));
                    pathMinus.setFromY(normalBall.getTranslateY() + directionList.get(i).get(1));
                    pathMinus.setFromZ(normalBall.getTranslateZ() + directionList.get(i).get(2));

                    pathMinus.setToX(normalBall.getTranslateX() );
                    pathMinus.setToY(normalBall.getTranslateY() );
                    pathMinus.setToZ(normalBall.getTranslateZ() );



                    path1.setFromX(monoBall.getTranslateX());
                    path1.setFromY(monoBall.getTranslateY());
                    path1.setFromZ(monoBall.getTranslateZ());

                    path1.setToX(monoBall.getTranslateX() + directionList.get(i).get(0));
                    path1.setToY(monoBall.getTranslateY() + directionList.get(i).get(1));
                    path1.setToZ(monoBall.getTranslateZ() + directionList.get(i).get(2));

                    pathMinus1.setFromX(monoBall.getTranslateX() + directionList.get(i).get(0));
                    pathMinus1.setFromY(monoBall.getTranslateY() + directionList.get(i).get(1));
                    pathMinus1.setFromZ(monoBall.getTranslateZ() + directionList.get(i).get(2));

                    pathMinus1.setToX(monoBall.getTranslateX() );
                    pathMinus1.setToY(monoBall.getTranslateY() );
                    pathMinus1.setToZ(monoBall.getTranslateZ() );


                    path2.setFromX(secBall.getTranslateX());
                    path2.setFromY(secBall.getTranslateY());
                    path2.setFromZ(secBall.getTranslateZ());

                    path2.setToX(secBall.getTranslateX() + directionList.get(i).get(0));
                    path2.setToY(secBall.getTranslateY() + directionList.get(i).get(1));
                    path2.setToZ(secBall.getTranslateZ() + directionList.get(i).get(2));

                    pathMinus2.setFromX(secBall.getTranslateX() + directionList.get(i).get(0));
                    pathMinus2.setFromY(secBall.getTranslateY() + directionList.get(i).get(1));
                    pathMinus2.setFromZ(secBall.getTranslateZ() + directionList.get(i).get(2));

                    pathMinus2.setToX(secBall.getTranslateX() );
                    pathMinus2.setToY(secBall.getTranslateY() );
                    pathMinus2.setToZ(secBall.getTranslateZ() );




                    path3.setFromX(polyBall.getTranslateX());
                    path3.setFromY(polyBall.getTranslateY());
                    path3.setFromZ(polyBall.getTranslateZ());

                    path3.setToX(polyBall.getTranslateX() + directionList.get(i).get(0));
                    path3.setToY(polyBall.getTranslateY() + directionList.get(i).get(1));
                    path3.setToZ(polyBall.getTranslateZ() + directionList.get(i).get(2));

                    pathMinus3.setFromX(polyBall.getTranslateX() + directionList.get(i).get(0));
                    pathMinus3.setFromY(polyBall.getTranslateY() + directionList.get(i).get(1));
                    pathMinus3.setFromZ(polyBall.getTranslateZ() + directionList.get(i).get(2));

                    pathMinus3.setToX(polyBall.getTranslateX() );
                    pathMinus3.setToY(polyBall.getTranslateY() );
                    pathMinus3.setToZ(polyBall.getTranslateZ() );



                    path4.setFromX(stickGroup.getTranslateX());
                    path4.setFromY(stickGroup.getTranslateY());
                    path4.setFromZ(stickGroup.getTranslateZ());

                    path4.setToX(stickGroup.getTranslateX() + directionList.get(i).get(0));
                    path4.setToY(stickGroup.getTranslateY() + directionList.get(i).get(1));
                    path4.setToZ(stickGroup.getTranslateZ() + directionList.get(i).get(2));

                    pathMinus4.setFromZ(stickGroup.getTranslateX() + directionList.get(i).get(0));
                    pathMinus4.setFromZ(stickGroup.getTranslateY() + directionList.get(i).get(1));
                    pathMinus4.setFromZ(stickGroup.getTranslateZ() + directionList.get(i).get(2));

                    pathMinus4.setToX(stickGroup.getTranslateX() );
                    pathMinus4.setToY(stickGroup.getTranslateY() );
                    pathMinus4.setToZ(stickGroup.getTranslateZ() );

                    path.setInterpolator(Interpolator.EASE_BOTH);
                    path1.setInterpolator(Interpolator.EASE_BOTH);
                    path2.setInterpolator(Interpolator.EASE_BOTH);
                    path3.setInterpolator(Interpolator.EASE_BOTH);
                    path4.setInterpolator(Interpolator.EASE_BOTH);
                    path5.setInterpolator(Interpolator.EASE_BOTH);

                    pathMinus.setInterpolator(Interpolator.EASE_BOTH);
                    pathMinus1.setInterpolator(Interpolator.EASE_BOTH);
                    pathMinus2.setInterpolator(Interpolator.EASE_BOTH);
                    pathMinus3.setInterpolator(Interpolator.EASE_BOTH);
                    pathMinus4.setInterpolator(Interpolator.EASE_BOTH);
                    pathMinus5.setInterpolator(Interpolator.EASE_BOTH);



                    seq.play();
                    seq1.play();
                    seq2.play();
                    seq3.play();
                    seq4.play();
                    seq5.play();


                }
            }




        });


        // Functionality of rotate-function and rotation-animation
        controller.getStructurePane().setOnMousePressed(e -> {
            prevX = e.getSceneX();
            prevY = e.getSceneY();

            if(rotateTimeLine != null){
                    rotateTimeLine.stop();
                    rotateTimeLine = null;

                }



        });
        figure.getTransforms().add(new Rotate());

        controller.getStructurePane().setOnMouseDragged(e -> {
            var dx = e.getSceneX() - prevX;
            var dy = e.getSceneY() - prevY;


            orthogonalAxis = new Point3D(dy, -dx, 0);
            rotate=new Rotate(0.25 * orthogonalAxis.magnitude(), orthogonalAxis);
            final Transform[] transformOld = {figure.getTransforms().get(0)};
            figure.getTransforms().remove(0);
            figure.getTransforms().forEach(transform -> transformOld[0] = transformOld[0].createConcatenation(transform));
            Transform transformNew= rotate.createConcatenation(transformOld[0]);

            figure.getTransforms().clear();
            figure.getTransforms().add(transformNew);
            prevX = e.getSceneX();
            prevY = e.getSceneY();

            if (e.isShiftDown()) {

                rotate = new Rotate(0, orthogonalAxis);
                Transform oldTransform = figure.getTransforms().get(0);
                figure.getTransforms().clear();
                figure.getTransforms().addAll(rotate, oldTransform);

                KeyFrame key1 = new KeyFrame(Duration.seconds(0), new KeyValue(rotate.angleProperty(), 0));
                KeyFrame key2 = new KeyFrame(Duration.seconds(7), new KeyValue(rotate.angleProperty(), 360));


                rotateTimeLine = new Timeline(key1, key2);

                rotateTimeLine.setCycleCount(Animation.INDEFINITE);

                rotateTimeLine.play();

            }

        });






        // Set balls and sticks sliders to initial values
        controller.getBallsSlider().setMax(1);
        controller.getBallsSlider().setMin(-1);
        controller.getSticksSlider().setMax(1);
        controller.getSticksSlider().setMin(-1);
        controller.getSticksSlider().setValue(0);
        controller.getBallsSlider().setValue(0);


        // Balls slider functionality
        controller.getBallsSlider().valueProperty().addListener((obervable, oldValue, newValue) -> {

            undoManager.add(new PropertyCommand<>("radius", (DoubleProperty) obervable, oldValue, newValue));


            double difference = (oldValue.doubleValue() - newValue.doubleValue()) * -0.1;


                for (Node node : balls.getBalls().getChildren()) {

                    Group group = (Group) node;
                    for(Node n: group.getChildren()){

                        Group group2 = (Group) n;

                        for(Node n2 : group2.getChildren()) {
                            Sphere sphere = (Sphere) n2;
                            sphere.setRadius(sphere.getRadius() + difference);
                        }


                    }


                }
                for (Node node : monomerBalls.getChildren()) {

                    Group group = (Group) node;

                    for(Node n : group.getChildren()) {
                        Group group2 = (Group) n;

                        for(Node n2 : group2.getChildren()) {
                            Sphere sphere = (Sphere) n2;
                            sphere.setRadius(sphere.getRadius() + difference);
                        }
                    }
                }
                for (Node node : secBalls.getChildren()) {

                    Group group = (Group) node;

                    for(Node n : group.getChildren()) {
                        Group group2 = (Group) n;

                        for(Node n2 : group2.getChildren()) {
                            Sphere sphere = (Sphere) n2;
                            sphere.setRadius(sphere.getRadius() + difference);
                        }
                    }
                }
                for (Node node : polymerBalls.getChildren()) {

                    Group group = (Group) node;

                    for(Node n : group.getChildren()) {
                        Group group2 = (Group) n;

                        for(Node n2 : group2.getChildren()) {
                            Sphere sphere = (Sphere) n2;
                            sphere.setRadius(sphere.getRadius() + difference);
                        }
                    }
                }


        });


        // Dark-mode functionality
        var stylesURL = WindowPresenter.class.getResource("modena_dark.css");
        controller.getDarkmodeMenuItem().setOnAction(e -> {

            if(controller.getDarkmodeMenuItem().isSelected()){
                stage.getScene().getStylesheets().add(stylesURL.toExternalForm());

            }else{
                stage.getScene().getStylesheets().remove(stylesURL.toExternalForm());

            }
        });




        // Stick-slider functionality
        controller.getSticksSlider().valueProperty().addListener(((observableValue, oldValue, newValue) -> {
            undoManager.add(new PropertyCommand<>("width", (DoubleProperty) observableValue, oldValue, newValue));

            double dif = (oldValue.doubleValue() - newValue.doubleValue()) * -0.05;

            for (int i = 0; i < sticks.getChildren().size(); i++) {
                Group group = (Group) sticks.getChildren().get(i);

                for(int j = 0; j < group.getChildren().size(); j++ ){

                    Group group2 = (Group) group.getChildren().get(j);

                    for(Node n : group2.getChildren()) {


                        Cylinder s = (Cylinder) n;

                        s.setRadius(s.getRadius() + dif);
                    }

                }



            }


        }));




        // Set functionality of balls box
        controller.getBallsBox().selectedProperty().addListener((v,o,n) -> {
            undoManager.add(new PropertyCommand<>("balls", (BooleanProperty) v, o, n));

            if (controller.getBallsBox().isSelected() & controller.getComboBox().getSelectionModel().isSelected(0)) {
                // clear figure of all ball groups first?
                balls.getBalls().setVisible(false);
                polymerBalls.setVisible(false);
                monomerBalls.setVisible(false);
                secBalls.setVisible(false);
                balls.getBalls().setVisible(true);


            }
            if (controller.getBallsBox().isSelected() & controller.getComboBox().getSelectionModel().isSelected(2)) {

                balls.getBalls().setVisible(false);
                polymerBalls.setVisible(false);
                monomerBalls.setVisible(false);
                secBalls.setVisible(false);
                secBalls.setVisible(true);


            }
            if (controller.getBallsBox().isSelected() & controller.getComboBox().getSelectionModel().isSelected(1)) {
                balls.getBalls().setVisible(false);
                polymerBalls.setVisible(false);
                monomerBalls.setVisible(false);
                secBalls.setVisible(false);
                monomerBalls.setVisible(true);


            }
            if (controller.getBallsBox().isSelected() & controller.getComboBox().getSelectionModel().isSelected(3)) {

                balls.getBalls().setVisible(false);
                polymerBalls.setVisible(false);
                monomerBalls.setVisible(false);
                secBalls.setVisible(false);
                polymerBalls.setVisible(true);


            }

            if (!controller.getBallsBox().isSelected()) {

                balls.getBalls().setVisible(false);
                polymerBalls.setVisible(false);
                monomerBalls.setVisible(false);
                secBalls.setVisible(false);



            }


        });

        // Set functionality of sticks-box
        controller.getSticksBox().selectedProperty().addListener((v,o,n) -> {

            if (controller.getSticksBox().isSelected()) {
                sticks.setVisible(true);

                controller.getRibbonCheckBox().setSelected(false);
            }

            if (!controller.getSticksBox().isSelected()) {
                sticks.setVisible(false);



            }
            undoManager.add(new PropertyCommand<>("sticks", (BooleanProperty) v, o, n));

        });

        // Set functionality of ribbon-checkbox
        controller.getRibbonCheckBox().selectedProperty().addListener((v,o,n) -> {

            if(controller.getRibbonCheckBox().isSelected()){
                ribbonGroup.setVisible(true);
                sticks.setVisible(false);
                controller.getSticksBox().setSelected(false);
            }

            if(!controller.getRibbonCheckBox().isSelected()){
                ribbonGroup.setVisible(false);


            }
            undoManager.add(new PropertyCommand<>("ribbons", (BooleanProperty) v, o, n));

        });

        // Property bindings
        // Balls-slider and balls-checkbox are deactivated when no molecule has been loaded
        controller.getBallsSlider().disableProperty().bind(Bindings.isEmpty(balls.getBalls().getChildren()));
        controller.getBallsBox().disableProperty().bind(Bindings.isEmpty(balls.getBalls().getChildren()));

        controller.getBallsSlider().disableProperty().bind(Bindings.isEmpty(controller.getStructurePane().getChildren()));
        controller.getBallsBox().disableProperty().bind(Bindings.isEmpty(controller.getStructurePane().getChildren()));
        // Sticks-slider and sticks-checkbox are deactivated when no molecule has been loaded

        controller.getSticksSlider().disableProperty().bind(Bindings.isEmpty(sticks.getChildren()));
        controller.getSticksBox().disableProperty().bind(Bindings.isEmpty(sticks.getChildren()));

        controller.getSticksSlider().disableProperty().bind(Bindings.isEmpty(controller.getStructurePane().getChildren()));
        controller.getSticksBox().disableProperty().bind(Bindings.isEmpty(controller.getStructurePane().getChildren()));

        controller.getPolymerMenuButton().disableProperty().bind(Bindings.isEmpty(controller.getStructurePane().getChildren()));
        controller.getPolymerMenuButton().disableProperty().bind(Bindings.isEmpty(figure.getChildren()));



        // Balls-slider and stick-slider are both deactivated when the respective checkbox is unchecked
        controller.getBallsSlider().disableProperty().bind(controller.getBallsBox().selectedProperty().not());
        controller.getSticksSlider().disableProperty().bind(controller.getSticksBox().selectedProperty().not());

        controller.getExplodeButton().disableProperty().bind(Bindings.isEmpty(controller.getStructurePane().getChildren()));

        controller.getExplodeButton().disableProperty().bind(Bindings.isEmpty(figure.getChildren()));
        controller.getRibbonCheckBox().disableProperty().bind(Bindings.isEmpty(ribbonGroup.getChildren()));


        // Property bindings of conformation combobox
        controller.getComboBox().disableProperty().bind(Bindings.isEmpty(figure.getChildren()));
        controller.getConformationComboBox().disableProperty().bind(Bindings.isEmpty(figure.getChildren()));
        conformationListSize = Bindings.size(controller.getConformationList());
        moreThanOneConformation = conformationListSize.greaterThan(1);
        controller.getConformationComboBox().disableProperty().bind(moreThanOneConformation.not());

        // Functionality of Search-Field
        controller.getSearchField().textProperty().addListener(e -> {
            String filter = controller.getSearchField().getText();
            if (filter == null || filter.length() == 0) {
                controller.getFilteredList().setPredicate(s -> true);

            } else {
                controller.getFilteredList().setPredicate(s -> s.contains(filter));
            }
        });

        // When a new conformation is selected, this part of the code is executed.
        controller.getConformationComboBox().valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                conformationNumber = 0;

                if(t1 != null){
                    String[] strArray = t1.split("\\s");


                    conformationNumber = Integer.parseInt(strArray[1]) - 1;


                    conformationSelected = conformationNumber;


                    c = conformations.getComplexList().get(conformationNumber);

                    // All balls/sticks/ribbons are invisible, except for the conformation that is selected

                    for(int i = 0; i < balls.getBalls().getChildren().size(); i++){

                        if(i == conformationNumber){


                            balls.getBalls().getChildren().get(i).setVisible(true);
                            monomerBalls.getChildren().get(i).setVisible(true);
                            polymerBalls.getChildren().get(i).setVisible(true);
                            secBalls.getChildren().get(i).setVisible(true);
                            sticks.getChildren().get(i).setVisible(true);
                            ribbonGroup.getChildren().get(i).setVisible(true);

                            // Polymers are determined for the selected conformation
                            setPolymers();
                            aminoAcidList = new ArrayList<>();
                            secStructureList = new ArrayList<>();
                            monomerToText = new HashMap<>();
                            textToMonomer = new HashMap<>();
                            selectionModel = new SelectionModelClass();

                            MoleculeFigure mf = moleculeFigures.get(i);

                            // Textflow is set for chosen conformation
                            setTextFlow();

                            controller.getTextFlowPane().setPadding(new Insets(10, 10, 0, 10));
                            controller.getTextFlowPane().getChildren().clear();


                            controller.getTextFlowPane().getChildren().addAll(aminoAcidList);

                            controller.getTextFlowPane().getChildren().add(new Text("\n"));
                            controller.getTextFlowPane().getChildren().add(new Text("\n"));
                            controller.getTextFlowPane().getChildren().addAll(secStructureList);

                            // Actionlistener for selected text or balls, taken from lecture
                            selectionModel.getSelectedItems().addListener((SetChangeListener<? super PdbMonomer>) change -> {
                                if (change.wasAdded()) {



                                    Platform.runLater(() -> {
                                        if (selectionModel.set.size() == 1) {
                                            updateOpacity(flat(mf.item2shapes), 0.2);


                                        }

                                        updateOpacity(mf.item2shapes.get(change.getElementAdded()), 1.0);


                                        int j = aminoAcidList.indexOf(monomerToText.get(change.getElementAdded()));
                                        aminoAcidList.get(j).setFill(Color.BLUE);
                                        controller.getTextFlowPane().getChildren().clear();

                                        controller.getTextFlowPane().getChildren().addAll(aminoAcidList);


                                        controller.getTextFlowPane().getChildren().add(new Text("\n"));
                                        controller.getTextFlowPane().getChildren().add(new Text("\n"));
                                        controller.getTextFlowPane().getChildren().addAll(secStructureList);

                                    });
                                } else if (change.wasRemoved()) {
                                    Platform.runLater(() -> {
                                        if (selectionModel.set.size() > 0) {
                                            updateOpacity(mf.item2shapes.get(change.getElementRemoved()), 0.2);

                                            int j = aminoAcidList.indexOf(monomerToText.get(change.getElementRemoved()));
                                            aminoAcidList.get(j).setFill(Color.BLACK);
                                            controller.getTextFlowPane().getChildren().clear();
                                            controller.getTextFlowPane().getChildren().addAll(aminoAcidList);


                                            controller.getTextFlowPane().getChildren().add(new Text("\n"));
                                            controller.getTextFlowPane().getChildren().add(new Text("\n"));
                                            controller.getTextFlowPane().getChildren().addAll(secStructureList);
                                        } else {
                                            updateOpacity(flat(mf.item2shapes), 1.0);

                                            int j = aminoAcidList.indexOf(monomerToText.get(change.getElementRemoved()));
                                            aminoAcidList.get(j).setFill(Color.BLACK);
                                            controller.getTextFlowPane().getChildren().clear();


                                            controller.getTextFlowPane().getChildren().addAll(aminoAcidList);


                                            controller.getTextFlowPane().getChildren().add(new Text("\n"));
                                            controller.getTextFlowPane().getChildren().add(new Text("\n"));
                                            controller.getTextFlowPane().getChildren().addAll(secStructureList);

                                        }
                                    });
                                }
                            });

                            continue;
                        }

                        balls.getBalls().getChildren().get(i).setVisible(false);
                        monomerBalls.getChildren().get(i).setVisible(false);
                        polymerBalls.getChildren().get(i).setVisible(false);
                        secBalls.getChildren().get(i).setVisible(false);
                        sticks.getChildren().get(i).setVisible(false);
                        ribbonGroup.getChildren().get(i).setVisible(false);




                    }

                }

                undoManager.add(new PropertyCommand<>("conformation", (SimpleObjectProperty<String>) observableValue, s, t1));

            }
        });


        // Set functionality of color scheme menu
        controller.getComboBox().valueProperty().addListener((v, o, n) -> {

            if(controller.getSticksBox().isSelected()){
                if (controller.getBallsBox().isSelected() & controller.getComboBox().getValue().toString().equals("By atom species")) {

                    monomerBalls.setVisible(false);
                    polymerBalls.setVisible(false);
                    balls.getBalls().setVisible(false);
                    sticks.setVisible(false);
                    secBalls.setVisible(false);
                    ribbonGroup.setVisible(false);

                    balls.getBalls().setVisible(true);
                    sticks.setVisible(true);
                }
                if (controller.getBallsBox().isSelected() & controller.getComboBox().getValue().toString().equals("By amino acid")) {

                    monomerBalls.setVisible(false);
                    polymerBalls.setVisible(false);
                    balls.getBalls().setVisible(false);
                    sticks.setVisible(false);
                    secBalls.setVisible(false);
                    ribbonGroup.setVisible(false);

                    monomerBalls.setVisible(true);
                    sticks.setVisible(true);
                }
                if (controller.getBallsBox().isSelected() & controller.getComboBox().getValue().toString().equals("By secondary structure")) {
                    monomerBalls.setVisible(false);
                    polymerBalls.setVisible(false);
                    balls.getBalls().setVisible(false);
                    sticks.setVisible(false);
                    secBalls.setVisible(false);
                    ribbonGroup.setVisible(false);

                    secBalls.setVisible(true);
                    sticks.setVisible(true);
                }
                if (controller.getBallsBox().isSelected() & controller.getComboBox().getValue().toString().equals("By polymer")) {
                    monomerBalls.setVisible(false);
                    polymerBalls.setVisible(false);
                    balls.getBalls().setVisible(false);
                    sticks.setVisible(false);
                    secBalls.setVisible(false);
                    ribbonGroup.setVisible(false);

                    polymerBalls.setVisible(true);
                    sticks.setVisible(true);
                }
            }
            else if(controller.getRibbonCheckBox().isSelected()){

                if (controller.getBallsBox().isSelected() & controller.getComboBox().getValue().toString().equals("By atom species")) {
                    monomerBalls.setVisible(false);
                    polymerBalls.setVisible(false);
                    balls.getBalls().setVisible(false);
                    sticks.setVisible(false);
                    secBalls.setVisible(false);
                    ribbonGroup.setVisible(false);

                    balls.getBalls().setVisible(true);
                    ribbonGroup.setVisible(true);
                }
                if (controller.getBallsBox().isSelected() & controller.getComboBox().getValue().toString().equals("By amino acid")) {

                    monomerBalls.setVisible(false);
                    polymerBalls.setVisible(false);
                    balls.getBalls().setVisible(false);
                    sticks.setVisible(false);
                    secBalls.setVisible(false);
                    ribbonGroup.setVisible(false);

                    monomerBalls.setVisible(true);
                    ribbonGroup.setVisible(true);
                }
                if (controller.getBallsBox().isSelected() & controller.getComboBox().getValue().toString().equals("By secondary structure")) {

                    monomerBalls.setVisible(false);
                    polymerBalls.setVisible(false);
                    balls.getBalls().setVisible(false);
                    sticks.setVisible(false);
                    secBalls.setVisible(false);
                    ribbonGroup.setVisible(false);

                    secBalls.setVisible(true);
                    ribbonGroup.setVisible(true);
                }
                if (controller.getBallsBox().isSelected() & controller.getComboBox().getValue().toString().equals("By polymer")) {
                    monomerBalls.setVisible(false);
                    polymerBalls.setVisible(false);
                    balls.getBalls().setVisible(false);
                    sticks.setVisible(false);
                    secBalls.setVisible(false);
                    ribbonGroup.setVisible(false);

                    polymerBalls.setVisible(true);
                    ribbonGroup.setVisible(true);
                }

            }else{
                if (controller.getBallsBox().isSelected() & controller.getComboBox().getValue().toString().equals("By atom species")) {
                    monomerBalls.setVisible(false);
                    polymerBalls.setVisible(false);
                    balls.getBalls().setVisible(false);
                    sticks.setVisible(false);
                    secBalls.setVisible(false);
                    ribbonGroup.setVisible(false);

                    balls.getBalls().setVisible(true);
                }
                if (controller.getBallsBox().isSelected() & controller.getComboBox().getValue().toString().equals("By amino acid")) {

                    monomerBalls.setVisible(false);
                    polymerBalls.setVisible(false);
                    balls.getBalls().setVisible(false);
                    sticks.setVisible(false);
                    secBalls.setVisible(false);
                    ribbonGroup.setVisible(false);

                    monomerBalls.setVisible(true);
                }
                if (controller.getBallsBox().isSelected() & controller.getComboBox().getValue().toString().equals("By secondary structure")) {
                    monomerBalls.setVisible(false);
                    polymerBalls.setVisible(false);
                    balls.getBalls().setVisible(false);
                    sticks.setVisible(false);
                    secBalls.setVisible(false);
                    ribbonGroup.setVisible(false);

                    secBalls.setVisible(true);
                }
                if (controller.getBallsBox().isSelected() & controller.getComboBox().getValue().toString().equals("By polymer")) {
                    monomerBalls.setVisible(false);
                    polymerBalls.setVisible(false);
                    balls.getBalls().setVisible(false);
                    sticks.setVisible(false);
                    secBalls.setVisible(false);
                    ribbonGroup.setVisible(false);

                    polymerBalls.setVisible(true);
                }


            }

         undoManager.add(new PropertyCommand<>("color", (SimpleObjectProperty<String>) v, o, n));


        });

        controller.getRibbonCheckBox().setSelected(false);

        Alert noPDB = new Alert(Alert.AlertType.WARNING);



        // Open menubutton functionality, pretty much the same as the functionality of the listview selection above

        controller.getOpenMenuItem().setOnAction(e -> {
            var fc = new FileChooser();
            var file = fc.showOpenDialog(stage);
            controller.getBallsBox().setSelected(true);
            controller.getSticksBox().setSelected(true);
            controller.getRibbonCheckBox().setSelected(false);



            if (file != null) {


                isProtein = true;


                controller.getComboBox().getSelectionModel().selectFirst();
                controller.getStructurePane().getChildren().clear();
                figure.getChildren().clear();
                controller.getConformationList().clear();
                conformations.getComplexList().clear();
                complexList.clear();
                moleculeFigures.clear();
                controller.getSticksSlider().setValue(0);
                controller.getBallsSlider().setValue(0);

                balls.getBalls().getChildren().clear();
                sticks.getChildren().clear();
                ribbonGroup.getChildren().clear();
                polymerBalls.getChildren().clear();
                monomerBalls.getChildren().clear();
                secBalls.getChildren().clear();
                controller.getTextFlowPane().getChildren().clear();
                controller.getPdbTextArea().clear();
                String item = (String) controller.getPdbList().getSelectionModel().getSelectedItem();
                controller.getChartPaneContent().getChildren().clear();

                System.out.println(getExtension(file.getName()).toString());




                    Service<Void> figureService = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {

                        return new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {

                                Path filePath = Path.of(file.getPath());
                                controller.getPdbTextArea().setText(Files.readString(filePath));

                                conformationReader(Files.readString(filePath));

                                for (int i = 0; i < conformations.getComplexList().size(); i++) {

                                    MoleculeFigure mf = new MoleculeFigure();


                                    moleculeFigures.add(mf.compute(conformations.getComplexList().get(i)));
                                    complextoMoleculeFigure.put(conformations.getComplexList().get(i), moleculeFigures.get(i));

                                }


                                for (int i = 0; i < conformations.getComplexList().size(); i++) {


                                    monomerBalls.getChildren().add(complextoMoleculeFigure.get(conformations.getComplexList().get(i)).monomerBallsFigure);
                                    secBalls.getChildren().add(complextoMoleculeFigure.get(conformations.getComplexList().get(i)).secBallsFigure);
                                    polymerBalls.getChildren().add(complextoMoleculeFigure.get(conformations.getComplexList().get(i)).polymerBallsFigure);
                                    balls.getBalls().getChildren().add(complextoMoleculeFigure.get(conformations.getComplexList().get(i)).ballsFigure);

                                    sticks.getChildren().add(complextoMoleculeFigure.get(conformations.getComplexList().get(i)).sticksFigure);
                                    ribbonGroup.getChildren().add(complextoMoleculeFigure.get(conformations.getComplexList().get(i)).ribbonGroupFigure);

                                }


                                controller.getConformationComboBox().setItems(controller.getConformationList());

                                numberToMoleculeFigure = new HashMap<>();


                                for (int i = 0; i < moleculeFigures.size(); i++) {

                                    String boxItem = "Conformation " + (i + 1);

                                    controller.getConformationList().add(boxItem);

                                    numberToMoleculeFigure.put(i + 1, moleculeFigures.get(i));
                                }


                                return null;
                            }
                        };
                    }
                };

                if(!getExtension(file.getName()).toString().equals("Optional[pdb]")  ){
                    noPDB.setContentText("This is not a pdb-file");
                    noPDB.show();

                }else {

                    figureService.start();
                }


                controller.getCancelButton().setOnAction(action -> {
                    figureService.cancel();

                });
                controller.getCancelButton().disableProperty().bind(figureService.runningProperty().not());
                controller.getProgressBar().visibleProperty().bind(figureService.runningProperty());
                controller.getProgressBar().progressProperty().bind(figureService.progressProperty());
                controller.getPdbList().disableProperty().bind(figureService.runningProperty());
                controller.getSaveMenuItem().disableProperty().bind(figureService.runningProperty());
                controller.getOpenMenuItem().disableProperty().bind(figureService.runningProperty());


                figureService.setOnSucceeded(b -> {

                    System.out.println("Succeeded");

                    if (!isProtein(conformations.getComplexList().get(0))) {
                        notAProteinAlert.setContentText("This file does not contain a protein structure and its structure can therefore not be displayed");
                        notAProteinAlert.show();

                        controller.getComboBox().getSelectionModel().selectFirst();
                        controller.getStructurePane().getChildren().clear();
                        figure.getChildren().clear();
                        controller.getConformationList().clear();
                        conformations.getComplexList().clear();
                        complexList.clear();
                        moleculeFigures.clear();
                        controller.getSticksSlider().setValue(0);
                        controller.getBallsSlider().setValue(0);

                        balls.getBalls().getChildren().clear();
                        sticks.getChildren().clear();
                        ribbonGroup.getChildren().clear();
                        polymerBalls.getChildren().clear();
                        monomerBalls.getChildren().clear();
                        secBalls.getChildren().clear();
                        controller.getTextFlowPane().getChildren().clear();
                        controller.getChartPaneContent().getChildren().clear();
                        isProtein = false;


                    }

                    controller.getPolymerMenuButton().getItems().clear();
                    controller.getPolymers().clear();


                    if (isProtein) {
                        controller.getConformationComboBox().getSelectionModel().select(0);

                        if(containsNucleotides(conformations.getComplexList().get(0))){
                            containsNucleotidesAlert.setContentText("This molecule contains nucleotides. Ribbons will not be shown for nucleotide sections of the molecule");
                            containsNucleotidesAlert.show();
                        }


                        for (CheckMenuItem menu : menuList) {

                            controller.getPolymerMenuButton().getItems().add(menu);

                            controller.getPolymers().add(menu.getText());


                        }
                        listSize = Bindings.size(controller.getPolymers());
                        largerThanOne = listSize.greaterThan(1);
                        smallerThan21 = listSize.greaterThan(20).not();
                        inBetween = largerThanOne.and(smallerThan21);
                        controller.getPolymerMenuButton().disableProperty().bind(largerThanOne.not());
                        controller.getExplodeButton().disableProperty().bind(inBetween.not());
                        controller.getExplodeButton().disableProperty().bind(Bindings.isEmpty(controller.getStructurePane().getChildren()));
                        controller.getPolymerMenuButton().disableProperty().bind(Bindings.isEmpty(controller.getStructurePane().getChildren()));



                        figure.getChildren().add(balls.getBalls());
                        figure.getChildren().add(sticks);
                        figure.getChildren().add(ribbonGroup);
                        ribbonGroup.setVisible(false);
                        figure.getChildren().add(monomerBalls);
                        monomerBalls.setVisible(false);
                        figure.getChildren().add(secBalls);
                        secBalls.setVisible(false);
                        figure.getChildren().add(polymerBalls);
                        polymerBalls.setVisible(false);

                        controller.getChartPaneContent().getChildren().add(aminoAcidChartProducer(c));
                        controller.getChartPaneContent().getChildren().add(polymerChartProducer(c));


                        Service<Void> textFlowService = new Service<Void>() {
                            @Override
                            protected Task<Void> createTask() {
                                return new Task<Void>() {
                                    @Override
                                    protected Void call() throws Exception {


                                        return null;
                                    }
                                };
                            }
                        };

                        textFlowService.start();

                        controller.getCancelButton().setOnAction(action -> {
                            textFlowService.cancel();

                        });
                        controller.getCancelButton().disableProperty().bind(textFlowService.runningProperty().not());
                        controller.getProgressBar().visibleProperty().bind(textFlowService.runningProperty());
                        controller.getProgressBar().progressProperty().bind(textFlowService.progressProperty());
                        controller.getPdbList().disableProperty().bind(textFlowService.runningProperty());

                        controller.getSaveMenuItem().disableProperty().bind(textFlowService.runningProperty());
                        controller.getOpenMenuItem().disableProperty().bind(textFlowService.runningProperty());


                        controller.getTextScrollPane().setFitToWidth(true);


                        camera = new PerspectiveCamera(true);


                        camera.setNearClip(0.1);
                        camera.setFarClip(10000);
                        camera.setTranslateZ(-10);


                        subScene.setCamera(camera);

                        controller.getStructurePane().getChildren().add(subScene);
                        subScene.heightProperty().bind(controller.getStructurePane().heightProperty());
                        subScene.widthProperty().bind(controller.getStructurePane().widthProperty());

                        controller.getBallsBox().setSelected(true);
                        controller.getSticksBox().setSelected(true);
                        controller.getRibbonCheckBox().setSelected(false);
                    }


                });


            }


        });



        // Save-button functionality
        controller.getSaveMenuItem().setOnAction(e -> {

            String text = controller.getPdbTextArea().getText();

            var fc = new FileChooser();
            var file = fc.showSaveDialog(stage);

            BufferedWriter bw = null;

            if(file != null){
                try {
                    bw = new BufferedWriter(new FileWriter(file.getPath()));
                    bw.write(text);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }




        });



    }
}

