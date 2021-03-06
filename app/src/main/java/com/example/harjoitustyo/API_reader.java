package com.example.harjoitustyo;

import android.os.StrictMode;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/*Reads an API with granted parameters
Is Singleton*/
public class API_reader {
    private static API_reader api_reader= new API_reader();
    User user = User.getInstance();
    //Variables for calculating
    private String bioWaste = "never";
    private String carton = "never";
    private String electronic = "never";
    private String glass = "never";
    private String hazardous = "never";
    private String metal = "never";
    private String paper = "never";
    private String plastic = "never";
    private String estimate = "low";
    //Result
    private double result = 0.0;
    public static API_reader getInstance() {
        return api_reader;
    }
    //Setters, getters not needed. (Setters could be replaced by public variables)
    public void setBioWaste(String s) {
        bioWaste = s;
    }
    public void setCarton(String s) {
        carton = s;
    }
    public void setElectronic(String s) {
        electronic = s;
    }
    public void setGlass(String s) {
        glass = s;
    }
    public void setHazardous(String s) {
        hazardous = s;
    }
    public void setMetal(String s) {
        metal = s;
    }
    public void setPaper(String s) {
        paper = s;
    }
    public void setPlastic(String s) {
        plastic = s;
    }
    public void setEstimate(String s) {
        estimate = s;
    }
    //Getter for result (just in case)
    public double getResult() {
        return result;
    }
    //Calculation method
    public double calculate() {
        String json = getJSON();
        result = Double.valueOf(json);

        user.getPerson().setC02(result);
        return result;
    }
    //API JSON reader. Returns String of the result value.
    public String getJSON() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Saving data to current Person;
        user.getPerson().setBioWaste(bioWaste);
        user.getPerson().setCarton(carton);
        user.getPerson().setElectronic(electronic);
        user.getPerson().setGlass(glass);
        user.getPerson().setHazardous(hazardous);
        user.getPerson().setMetal(metal);
        user.getPerson().setPaper(paper);
        user.getPerson().setPlastic(plastic);
        user.getPerson().setEstimate(estimate);
        user.getPerson().setHabits();

        String response = null;
        String Url = "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/WasteCalculator?query.bioWaste=" + bioWaste + "&query.carton=" + carton + "&query.electronic=" + electronic + "&query.glass=" + glass + "&query.hazardous=" + hazardous + "&query.metal=" + metal + "&query.paper=" + paper + "&query.plastic=" + plastic + "&query.amountEstimate=" + estimate;
        System.out.println(Url);
        try {
            URL url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader((new InputStreamReader(in)));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            response = sb.toString();
            in.close();
        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
    // Better version for doing other calculations
    public String getJSON2(String bioWaste, String carton, String electronic, String glass, String hazardous, String metal, String paper, String plastic, String estimate) {

        //Policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String response = null;
        String Url = "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/WasteCalculator?query.bioWaste=" + bioWaste + "&query.carton=" + carton + "&query.electronic=" + electronic + "&query.glass=" + glass + "&query.hazardous=" + hazardous + "&query.metal=" + metal + "&query.paper=" + paper + "&query.plastic=" + plastic + "&query.amountEstimate=" + estimate;

        try {
            //Some settings
            URL url = new URL(Url);
            URI url2 = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = url2.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            connection.setRequestMethod("GET");
            connection.setDoOutput(false);

            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader((new InputStreamReader(in)));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            response = sb.toString();
            in.close();
            br.close();
        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return response;
    }


}
