package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import static java.lang.System.*;

import domain.apriori.Apriori;
import domain.apriori.HashTree;
import domain.apriori.IntegerItem;
import domain.apriori.ItemSet;
import domain.apriori.StringItem;

public class AprioriSession {
    
    private static final String DELIMITER_DEFAULT = "\\s+";
    private static final String OUTPUT_FILENAME_DEFAULT = "output_apriori.txt";
    private static final int MAX_BUCKET_SIZE_DEFAULT = 5;
    private static final int CHILDREN_DEFAULT = 3;
    
    private static AprioriSession aprioriSession; // singleton
    
    private Set<ItemSet> transactions;
    private String inputFile;
    private String outputFile;
    private String delimiter;
    private int minSup;
    private int maxBucketSize;
    private int childrenPerNode;

    private AprioriSession() {
    }
    
    /** @return The singleton instance of the apriori session. **/
    public static AprioriSession getSession() {
        if (aprioriSession == null)
            aprioriSession = new AprioriSession();
        return aprioriSession;
    }
    
    /** Runs the apriori algorithm with the specified parameters.
     * Default values can be used for everything except the input file and minimum support.
     * @param inFile The input file containing the data the apriori algorithm will be run on.
     * @param delimiter The delimiter used in the input file to separate attributes. If null, a default value is used.
     * @param outFile The file to write the algorithm's output to. If null, a default value is used.
     * @param minSup The absolute minimum support required for an itemset to be considered frequent.
     * @param maxBucketSize The maximum bucket size of bucket nodes in the generated hash trees. If null, a default value is used.
     * @param children The number of children per node in the generated hash trees. If null, a default value is used.
     */
    public void run(String inFile, String delimiter, String outFile, String minSup, String maxBucketSize, String children) {
        if (minSup == null) {
            err.println("Minimum support must be specified for the Apriori algorithm.");
            exit(1);
        }
        
        try {
            this.minSup = Integer.parseInt(minSup);
            this.inputFile = inFile;
            this.delimiter = whichDelimiter(delimiter);
            this.outputFile = (outFile == null) ? OUTPUT_FILENAME_DEFAULT : outFile;
            this.maxBucketSize = (maxBucketSize == null) ? MAX_BUCKET_SIZE_DEFAULT : Integer.parseInt(maxBucketSize);
            this.childrenPerNode = (children == null) ? CHILDREN_DEFAULT : Integer.parseInt(children);
            
            out.println("\nAPRIORI\n");
            
            out.print("Reading input... ");
            this.readAprioriInput();
            out.println("Done.");
            
            out.println("Running Apriori algorithm...\n");
            Apriori.getApriori().run(this.transactions, this.minSup, this.maxBucketSize, this.childrenPerNode);
            out.println("...algorithm finished.");
            
            out.print("Writing to output file \"" + this.outputFile + "\"... ");
            this.writeOutput();
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
        catch (IOException e) {
            err.println(e.getMessage());
            exit(1);
        }
    }
    
    /** Attempts to read from this.inputFile and generate the list of this.transactions.
     * If the input file cannot be found, an exception is thrown.
     * @throws FileNotFoundException When this.inputFile cannot be found (invalid filename specified).**/
    private void readAprioriInput() throws FileNotFoundException {
        Scanner fileScan = null;
        Scanner lineScan = null;
        ItemSet itemSet;
        
        try {
        
            transactions = new HashSet<ItemSet>();
            fileScan = new Scanner(new BufferedReader(new FileReader(inputFile)));
            
            while (fileScan.hasNextLine()) {
                itemSet = new ItemSet();
                lineScan = new Scanner(fileScan.nextLine());
                lineScan.useDelimiter(delimiter);
                
                while (lineScan.hasNext()) {
                    if (lineScan.hasNextInt())
                        itemSet.add(new IntegerItem(lineScan.nextInt()));
                    else
                        itemSet.add(new StringItem(lineScan.next()));
                }
                
                transactions.add(itemSet);
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
     * @throws IOException When the output file cannot be written to for some reason. **/
    private void writeOutput() throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(String.format("APRIORI"));
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
        if (delimiter == null || delimiter.isEmpty())
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
