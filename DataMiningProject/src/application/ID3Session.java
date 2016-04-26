package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import static java.lang.System.*;

import domain.id3.ID3;

public class ID3Session {
    
    private static final String DELIMITER_DEFAULT = "\\s+";
    private static final String OUTPUT_FILENAME_DEFAULT = "output_id3.txt";
    
    private static ID3Session id3Session; // singleton
    
    private ArrayList<String[]> trainingTuples;
    private ArrayList<String[]> testingTuples;
//    private String inputFile;
//    private String outputFile;
    private String delimiter;
//    private int classLabelIndex;
//    private ArrayList<String> attrTitles;

    private ID3Session() {
    }
    
    /** @return The singleton instance of the apriori session. **/
    public static ID3Session getSession() {
        if (id3Session == null)
            id3Session = new ID3Session();
        return id3Session;
    }
    
    public static void main(String[] args) {
        ID3Session.getSession().run(
                "id3_simple-training-2.txt",
                null,
                "id3_output.txt",
                "4",
                "id3_simple-testing-2.txt");
    }
    
    /** Runs the apriori algorithm with the specified parameters. Default values can be used for everything except the input file.
     * @param inFile The input file containing the data the apriori algorithm will be run on.
     * @param delimiter The delimiter used in the input file to separate attributes. If null, a default value is used.
     * @param outFile The file to write the algorithm's output to. If null, a default value is used.
     * @param classIndex The index of the class label column in the input file.
     */
    public void run(String inFile, String delimiter, String outFile, String classIndexString, String testingFile) {
        int classIndex;
        String[] attrTitles;
        String[] testingAttrTitles;
        
        if (classIndexString == null) {
            err.println("The class label index must be specified for the ID3 algorithm.");
            exit(1);
        }
        
        try {
            this.delimiter = whichDelimiter(delimiter);
            outFile = (outFile == null) ? OUTPUT_FILENAME_DEFAULT : outFile;
            classIndex = Integer.parseInt(classIndexString);
            
            out.println("\nID3\n");
            
            out.format("Reading input from \"%s\"... ", inFile);
            attrTitles = readID3Input(inFile);
            out.println("Done.");
            
            out.println("Running ID3 algorithm...\n");
            ID3.getID3().run(trainingTuples, attrTitles, classIndex);
            out.println("...algorithm finished.");
            
            out.format("Writing to output file \"%s\"... ", outFile);
            writeOutput(outFile);
            out.println("Done.");
            
            if (testingFile != null) {
                out.format("Reading testing data from \"%s\"... ", testingFile);
                readTestingInput(testingFile, attrTitles);
                out.println("Done.");
                
                testingAttrTitles = new String[attrTitles.length];
                System.arraycopy(attrTitles, 0, testingAttrTitles, 0, attrTitles.length);
                
                out.print("Classifying tuples... ");
                ID3.getID3().classify(testingTuples, testingAttrTitles, classIndex);
                out.format("Done.%n%n");
                
                out.println("Classification results:");
                printTuples(testingTuples, testingAttrTitles);
            }
        }
        catch (NumberFormatException e) {
            err.println("Unable to parse integer. Make sure integer arguments are valid integers.");
            exit(1);
        }
        catch (FileNotFoundException e) {
            err.println(e.getMessage());
            exit(1);
        }
        catch (IOException e) {
            err.println(e.getMessage());
            exit(1);
        }
    }
    
    /** Attempts to read from this.inputFile and generate the list of this.transactions.
     * @return An array of attribute titles **/
    private String[] readID3Input(String inputFile) throws FileNotFoundException {
        Scanner fileScan = null;
        Scanner lineScan = null;
        String[] aTuple;
        ArrayList<String> attrTitles = new ArrayList<String>();
        
        if (delimiter == null || delimiter.isEmpty())
            delimiter = DELIMITER_DEFAULT;
        
        try {
        
            trainingTuples = new ArrayList<String[]>();
            fileScan = new Scanner(new BufferedReader(new FileReader(inputFile)));
            
            // the first line of input should contain attribute titles
            lineScan = new Scanner(fileScan.nextLine());
            while (lineScan.hasNext())
                attrTitles.add(lineScan.next());
            lineScan.close();
            
            // the remaining lines should be the actual trainingTuples
            while (fileScan.hasNextLine()) {
                lineScan = new Scanner(fileScan.nextLine());
                lineScan.useDelimiter(delimiter);
                aTuple = new String[attrTitles.size()];
                
                for (int i = 0; i < aTuple.length; i++)
                    aTuple[i] = lineScan.next();
                
                trainingTuples.add(aTuple);
                lineScan.close();
            }
        }
        catch (FileNotFoundException e) { throw new FileNotFoundException(("Input file \"" + inputFile + "\" not found")); }
        finally {
            if (fileScan != null)
                fileScan.close();
            if (lineScan != null)
                lineScan.close();
        }
        
        return attrTitles.toArray(new String[attrTitles.size()]);
    }

    /** Attempts to write the algorithm's output to this.outputFile.
     * If the output file cannot be found, an error message is printed and the method returns. **/
    private void writeOutput(String outputFile) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(String.format("ID3"));
            writer.newLine();
            writer.newLine();
            
            writer.write(ID3.getID3().drawDecisionTree());
            writer.newLine();
        }
        catch (IOException e) { throw new IOException("Unable to write to output file \"" + outputFile + "\""); }
        finally {
            try { if (writer != null) writer.close(); }
            catch (IOException e) { }
        }
    }
    
    private void readTestingInput(String testingFile, String[] attrTitles) throws FileNotFoundException {
        Scanner fileScan = null;
        Scanner lineScan = null;
        String[] aTuple;
        
        if (delimiter == null || delimiter.isEmpty())
            delimiter = DELIMITER_DEFAULT;
        
        try {
        
            testingTuples = new ArrayList<String[]>();
            fileScan = new Scanner(new BufferedReader(new FileReader(testingFile)));
            
            // read trainingTuples
            while (fileScan.hasNextLine()) {
                lineScan = new Scanner(fileScan.nextLine());
                lineScan.useDelimiter(delimiter);
                aTuple = new String[attrTitles.length];
                
                for (int i = 0; i < aTuple.length-1; i++) // last slot should be class slot, and is empty
                    aTuple[i] = lineScan.next();
                
                testingTuples.add(aTuple);
                lineScan.close();
            }
        }
        catch (FileNotFoundException e) { throw new FileNotFoundException(String.format("Testing file \"%s\" not found", testingFile)); }
        finally {
            if (fileScan != null)
                fileScan.close();
            if (lineScan != null)
                lineScan.close();
        }
    }
    
    /** Converts a simple delimiter string such as "|" into a regular expression that ignores surrounding spaces.
     * Special values ("comma", "space", and "tab") are converted to their intended delimiters.
     * If delimiter is null, a default value is used.
     * @return A delimiter regular expression. **/
    private String whichDelimiter(String delimiter) {
        if (delimiter == null)
            return DELIMITER_DEFAULT;
        if (delimiter.equalsIgnoreCase("comma"))
            return ",\\s*";
        if (delimiter.equalsIgnoreCase("space"))
            return "\\s+";
        if (delimiter.equalsIgnoreCase("tab"))
            return "\\t+";
        
        return "\\s*" + delimiter + "\\s*";
    }
    
    private void printTuples(ArrayList<String[]> tuples, String[] attributeTitles) {
        for (String attrTitle : attributeTitles) {
            if (attrTitle.length() < 5)
                out.format("%-"+(14)+"s", attrTitle);
            else
                out.format("%-"+(attrTitle.length()+4)+"s", attrTitle);
        }
        out.format("%n");
        
        for (String[] tuple : tuples) {
            for (int i = 0; i < attributeTitles.length; i++) {
                if (attributeTitles[i].length() < 5)
                    out.format("%-"+(14)+"s", tuple[i]);
                else
                    out.format("%-"+(attributeTitles[i].length()+4)+"s", tuple[i]);
            }
            out.format("%n");
        }
    }
    
}
