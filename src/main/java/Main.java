import java.io.*;

/**
 * Created by e080868 on 11/28/2018.
 */
public class Main {
    public static void main(String[] args){
        QueryParser parser = new QueryParser();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String query = br.readLine();

            QueryParameter parameter = parser.parseQuery(query);
            String result = CsvQueryProcessor.getInstance().executeQuery(parameter);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


