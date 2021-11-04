import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ParseCSV {
        public static void main(String[] args) {
            try {
                //csv file containing data
                String strFile = "C:/CodeSystems/KLCommonCareSocialCodes.csv";
                CSVReader reader = new CSVReader(new FileReader(strFile));
                String [] nextLine;
                int lineNumber = 0;
                while ((nextLine = reader.readNext()) != null) {
                    lineNumber++;
                    System.out.println("Line # " + lineNumber);

                    // nextLine[] is an array of values from the line
                    System.out.println(nextLine[3] + " etc...");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        }
    }

