package PDBExplorer.model;

import javafx.concurrent.Task;

import javax.json.Json;
import javax.json.JsonString;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class PDBWebClient extends Task<ArrayList<String>> {

    ArrayList<String> overviewFiles;

    public PDBWebClient(){
        overviewFiles = new ArrayList<>();


    }

    static final String fullPDB_URL = "https://data.rcsb.org/rest/v1/holdings/current/entry_ids";
    static final String prefixPDB_URL = "https://files.rcsb.org/download/";


    // getFromURL takes a URL as input, connects to it and retrieves the inputstream
    private static InputStream getFromURL(URL url) throws IOException{
        var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection.getInputStream();

    }


    // retreivePDB turns the retrieved input string into a string
    public static String retrievePDB(String pdb) throws IOException {
        URL url = new URL(prefixPDB_URL + pdb + ".pdb");

        String output = new String(getFromURL(url).readAllBytes());

        return output;
    }

    @Override
    public ArrayList<String> call() throws Exception {
        URL url = new URL(fullPDB_URL);
        ArrayList<String> overviewFiles = new ArrayList<>();
        var r = Json.createReader(getFromURL(url));
        int progress = 0;
        for (var item : r.readArray()) {
            overviewFiles.add(((JsonString) item).getString());
            if(isCancelled()){
                return null;
            }
            progress++;
            updateProgress(progress, 100);

        }

        succeeded();


        return overviewFiles;
    }



    public static void main(String[] args) throws Exception {



    }




}

