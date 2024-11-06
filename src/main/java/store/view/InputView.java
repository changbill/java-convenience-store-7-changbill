package store.view;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputView {

    public List<String> readFile(String fileLocation) {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileLocation));
            while(true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }

            br.close();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return lines;
    }
}
