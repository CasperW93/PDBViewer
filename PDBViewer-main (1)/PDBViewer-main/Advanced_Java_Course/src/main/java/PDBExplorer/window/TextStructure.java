package PDBExplorer.window;

import PDBExplorer.model.PdbMonomer;
import javafx.scene.control.SelectionModel;
import javafx.scene.text.Text;

import static PDBExplorer.window.MoleculeFigure.*;
import static PDBExplorer.window.WindowPresenter.textToMonomer;

public class TextStructure extends BallGroup{

    Text text;


    // TextStructure is basically a text-node with an actionlistener, which is adapted from the lecture
    public TextStructure(Text text){

        this.text = text;

        text.setOnMouseClicked(e -> {

            if(!e.isShiftDown()){



                if(selectionModel.isSelected(textToMonomer.get(text)) & selectionModel.set.size() == 1){
                    selectionModel.clearSelection();



                }else{
                    selectionModel.clearSelection();

                    boolean isSelected = selectionModel.isSelected(textToMonomer.get(text));


                    selectionModel.setSelected(textToMonomer.get(text), !isSelected);



                }
            }else{
                boolean isSelected = selectionModel.isSelected(textToMonomer.get(text));


                selectionModel.setSelected(textToMonomer.get(text), !isSelected);



            }





        });


    }


    public Text getText() {
        return text;
    }


}
