package PDBExplorer.window;

import PDBExplorer.model.PdbComplex;
import javafx.scene.chart.PieChart;


// PolymerChart creates a chart that shows the amount of monomers per polymer molecule
public class PolymerChart {


    public static PieChart polymerChartProducer(PdbComplex complex){
        PieChart chart = new PieChart();

        int polymerSize = 0;



        for(int i =0; i < complex.getPolymerList().size(); i++){

            polymerSize = complex.getPolymerList().get(i).getMonomerList().size();

            chart.getData().add(new PieChart.Data(polymerSize + " " + complex.getPolymerList().get(i).getLabel(), polymerSize));



        }

        chart.setTitle("Amount of Amino Acids per Polymer");



        return chart;
    }
}
