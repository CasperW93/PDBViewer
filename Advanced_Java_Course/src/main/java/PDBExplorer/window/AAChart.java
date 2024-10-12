package PDBExplorer.window;

import PDBExplorer.model.PdbComplex;
import javafx.scene.chart.PieChart;


public class AAChart {


    // Creates a chart that shows the amount of each amino acid
    public static PieChart aminoAcidChartProducer(PdbComplex complex){
        PieChart chart = new PieChart();

        double Ala = 0;
        double Arg = 0;
        double Asn = 0;
        double Asp = 0;
        double Cys = 0;
        double Glu = 0;
        double Gln = 0;
        double Gly = 0;
        double His = 0;
        double Ile = 0;
        double Leu = 0;
        double Lys = 0;
        double Met = 0;
        double Phe = 0;
        double Pro = 0;
        double Ser = 0;
        double Thr = 0;
        double Trp = 0;
        double Tyr = 0;
        double Val = 0;

        for(int i =0; i < complex.getPolymerList().size(); i++){
            for(int j = 0; j < complex.getPolymerList().get(i).getMonomerList().size(); j++){

                String label = complex.getPolymerList().get(i).getMonomerList().get(j).getLabel();


                if (label.equals("A")) {
                    Ala++;
                }
                if (label.equals("R")) {
                    Arg++;
                }
                if (label.equals("N")) {
                    Asn++;
                }
                if (label.equals("D")) {
                    Asp++;
                }
                if (label.equals("C")) {
                    Cys++;
                }
                if (label.equals("E")) {
                    Glu++;
                }
                if (label.equals("Q")) {
                    Gln++;
                }
                if (label.equals("G")) {
                    Gly++;
                }
                if (label.equals("H")) {
                    His++;
                }
                if (label.equals("I")) {
                    Ile++;
                }
                if (label.equals("L")) {
                    Leu++;
                }
                if (label.equals("K")) {
                    Lys++;
                }
                if (label.equals("M")) {
                    Met++;
                }
                if (label.equals("F")) {
                    Phe++;
                }
                if (label.equals("P")) {
                    Pro++;
                }
                if (label.equals("S")) {
                    Ser++;
                }
                if (label.equals("T")) {
                    Thr++;
                }
                if (label.equals("W")) {
                    Trp++;
                }
                if (label.equals("Y")) {
                    Tyr++;
                }
                if (label.equals("V")) {
                    Val++;
                }
            }

        }

        chart.getData().add(new PieChart.Data((int) Ala + " Ala", Ala));
        chart.getData().add(new PieChart.Data((int) Arg + " Arg", Arg));
        chart.getData().add(new PieChart.Data((int) Asn + " Asn", Asn));
        chart.getData().add(new PieChart.Data((int) Asp + " Asp", Asp));
        chart.getData().add(new PieChart.Data((int) Cys + " Cys", Cys));
        chart.getData().add(new PieChart.Data((int) Glu + " Glu", Glu));
        chart.getData().add(new PieChart.Data((int) Gln + " Gln", Gln));
        chart.getData().add(new PieChart.Data((int) Gly + " Gly", Gly));
        chart.getData().add(new PieChart.Data((int) His + " His", His));
        chart.getData().add(new PieChart.Data((int) Ile + " Ile", Ile));
        chart.getData().add(new PieChart.Data((int) Leu + " Leu", Leu));
        chart.getData().add(new PieChart.Data((int) Lys + " Lys", Lys));
        chart.getData().add(new PieChart.Data((int) Met + " Met", Met));
        chart.getData().add(new PieChart.Data((int) Phe + " Phe", Phe));
        chart.getData().add(new PieChart.Data((int) Pro + " Pro", Pro));
        chart.getData().add(new PieChart.Data((int) Ser + " Ser", Ser));
        chart.getData().add(new PieChart.Data((int) Thr + " Thr", Thr));
        chart.getData().add(new PieChart.Data((int) Trp + " Trp", Trp));
        chart.getData().add(new PieChart.Data((int) Tyr + " Tyr", Tyr));
        chart.getData().add(new PieChart.Data((int) Val + " Val", Val));

        chart.setTitle("Amino Acid Counts");



        return chart;
    }
}
