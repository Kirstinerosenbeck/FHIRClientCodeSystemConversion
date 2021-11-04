import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.hl7.fhir.r4.model.CodeSystem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Shorts {
    public static void main(String[] args) throws IOException {
        List<String> attributelist = new ArrayList<>();
        List<String> danishList = new ArrayList();
        try {
            //csv file containing data
//                        String strFile = filename;
//                        CSVReader reader = new CSVReader(new FileReader(strFile));
            CSVParser parser = new CSVParserBuilder().withSeparator('|').build();
            BufferedReader br = Files.newBufferedReader(Paths.get("C:/Users/kirst/Projects/tabelkonvertering/tabel.csv"),  StandardCharsets.ISO_8859_1);
            CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build();
            String [] nextLine;
            String klasse="Observation.";
            int lineNumber = 0;
            while ((nextLine = reader.readNext()) != null) {
                String temp = nextLine[3].replaceAll(klasse,"");
                temp = temp.replaceAll("extension:","extension[");
                temp = "* "+temp+" ^short = \"[DK] "+nextLine[1]+"\"";


                //System.out.println(temp);
                String temp1 = nextLine[1].replaceAll("borgervurdering","");
                System.out.println(temp);

            }


    } catch (
    FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (
    CsvValidationException e) {
        e.printStackTrace();
    }
    }
}
