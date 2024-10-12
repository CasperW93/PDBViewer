package Assignment_5.model;

import javafx.scene.paint.Color;

public class AtomicSpecies {

    // An atomic species consists of a symbol (string), a radius, and a color. There are 3 species in total: Carbon, Hydrogen, and Oxygen

    private String symbol;
    private double radiusPM;
    private Color col;

    public AtomicSpecies(String symbol, double radiusPM, Color col){
        this.symbol = symbol;
        this. radiusPM = radiusPM;
        this.col = col;


    }

    public static AtomicSpecies createCarbon(){

        return new AtomicSpecies("C", 0.3, Color.BLACK);

    }

    public static AtomicSpecies createHydrogen(){

        return new AtomicSpecies("H", 0.1, Color.WHITE);

    }

    public static AtomicSpecies createOxygen(){

        return new AtomicSpecies("O", 0.2 , Color.RED);

    }



    public String getSymbol() {
        return symbol;
    }

    public double getRadiusPM() {
        return radiusPM;
    }

    public Color getCol() {
        return col;
    }
}
