package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import static java.lang.System.*;

import domain.apriori.Apriori;
import domain.apriori.HashTree;
import domain.apriori.IntegerItem;
import domain.apriori.ItemSet;
import domain.apriori.StringItem;
import domain.id3.ID3;
import domain.id3.id3Launcher;

public class ID3Session {
    
    private static final String DELIMITER_DEFAULT = "\\s+";
    private static final String OUTPUT_FILENAME_DEFAULT = "output_id3.txt";
//    private static final int MAX_BUCKET_SIZE_DEFAULT = 5;
//    private static final int CHILDREN_DEFAULT = 3;
    
    private static ID3Session id3Session; // singleton
    
    private ArrayList<ArrayList<String>> tuples;
    private String inputFile;
    private String outputFile;
    private String delimiter;
    private String positiveAttributeName;
    private int classLabelIndex;

    private ID3Session() {
    }
    
    /** @return The singleton instance of the apriori session. **/
    public static ID3Session getSession() {
        if (id3Session == null)
            id3Session = new ID3Session();
        return id3Session;
    }
    
    /** Runs the apriori algorithm with the specified parameters. Default values can be used for everything except the input file.
     * @param inFile The input file containing the data the apriori algorithm will be run on.
     * @param delimiter The delimiter used in the input file to separate attributes. If null, a default value is used.
     * @param outFile The file to write the algorithm's output to. If null, a default value is used.
     * @param posAttrName The name of the positive attribute, e.g. "Yes" or "True".
     * @param labelIndex The index of the class label column in the input file.
     */
    public void run(String inFile, String delimiter, String outFile, String posAttrName, String labelIndex) {
//        if (posAttrName == null) {
//            err.println("Positive attribute name must be specified for the ID3 algorithm.");
//            exit(1);
//        }
//        if (labelIndex == null) {
//            err.println("The class label index must be specified for the ID3 algorithm.");
//            exit(1);
//        }
        
        try {
//            this.inputFile = inFile;
//            this.delimiter = whichDelimiter(delimiter);
//            this.outputFile = (outFile == null) ? OUTPUT_FILENAME_DEFAULT : outFile;
//            this.positiveAttributeName = posAttrName;
//            this.classLabelIndex = Integer.parseInt(labelIndex);
            
            out.println("\nID3\n");
            
            out.print("Reading input... ");
            this.readID3Input();
            out.println("Done.");
            
            out.println("Running ID3 algorithm...\n");
//            int[] ignore = {5};
//            id3Launcher.kickoff("Yes", 4, ignore);
            ArrayList<Integer> attributeList = new ArrayList<Integer>();
            attributeList.add(0); attributeList.add(1); attributeList.add(2);
            attributeList.add(3); attributeList.add(4);
            ArrayList<String> attributeTitles = new ArrayList<String>();
            attributeTitles.add("Outlook");
            attributeTitles.add("Temperature");
            attributeTitles.add("Humidity");
            attributeTitles.add("Windy");
            attributeTitles.add("Class");
            ID3.getID3().run(tuples, attributeList, attributeTitles, "yes", 4);
//            Apriori.getApriori().run(this.transactions, this.minSup, this.maxBucketSize, this.childrenPerNode);
            out.println("...algorithm finished.");
            
            out.print("Writing to output file \"" + this.outputFile + "\"... ");
//            this.writeOutput();
            out.println("Done.");
        }
        catch (NumberFormatException e) {
            err.println("Unable to parse integer. Make sure integer arguments are valid integers.");
            exit(1);
        }
        catch (FileNotFoundException e) {
            err.println(e.getMessage());
            exit(1);
        }
//        catch (IOException e) {
//            err.println(e.getMessage());
//            exit(1);
//        }
    }
    
    /** Attempts to read from this.inputFile and generate the list of this.transactions.
     * If the input file cannot be found, an error message is printed and the method returns. **/
    private void readID3Input() throws FileNotFoundException {
        Scanner fileScan = null;
        Scanner lineScan = null;
        ArrayList<String> aTuple;
        
        if (delimiter == null || delimiter.isEmpty())
            delimiter = DELIMITER_DEFAULT;
        
        try {
        
            tuples = new ArrayList<ArrayList<String>>();
            fileScan = new Scanner(new BufferedReader(new FileReader(inputFile)));
            
            while (fileScan.hasNextLine()) {
                aTuple = new ArrayList<String>();
                lineScan = new Scanner(fileScan.nextLine());
                lineScan.useDelimiter(delimiter);
                
                while (lineScan.hasNext())
                    aTuple.add(lineScan.next());
                
                tuples.add(aTuple);
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
    }

    /** Attempts to write the algorithm's output to this.outputFile.
     * If the output file cannot be found, an error message is printed and the method returns. **/
    private void writeOutput() throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(String.format("ID3"));
            writer.newLine();
            writer.newLine();
            
            for (HashTree tree : Apriori.getApriori().getHashTrees()) {
                writer.write(tree.toString());
                writer.newLine();
            }
        }
        catch (IOException e) { throw new IOException("Unable to write to output file \"" + outputFile + "\""); }
        finally {
            try { if (writer != null) writer.close(); }
            catch (IOException e) { }
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
    
}
