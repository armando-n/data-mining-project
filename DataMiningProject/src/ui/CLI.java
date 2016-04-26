package ui;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import application.AprioriSession;
import application.ID3Session;
import domain.xmeans.MeanLauncher;

public class CLI {
    private static final String PROGRAM_NAME = "dm-proj";
    
    // general options
    private static final String OPTIONS_ORDER = "iahdombclt"; // specifies argument order in help/usage messages
    private static final String OPT_ALGORITHM_S = "a";
    private static final String OPT_ALGORITHM_L = "algorithm";
    private static final String OPT_INPUT_FILE_S = "i";
    private static final String OPT_INPUT_FILE_L = "input";
    private static final String OPT_OUTPUT_FILE_S = "o";
    private static final String OPT_OUTPUT_FILE_L = "output";
    private static final String OPT_DELIMITER_S = "d";
    private static final String OPT_DELIMITER_L = "delimiter";
    private static final String OPT_HELP_S = "h";
    private static final String OPT_HELP_L = "help";
    
    // apriori options
    private static final String OPT_MIN_SUP_S = "m";
    private static final String OPT_MIN_SUP_L = "min-sup";
    private static final String OPT_BUCKET_MAX_S = "b";
    private static final String OPT_BUCKET_MAX_L= "bucket";
    private static final String OPT_CHILDREN_PER_NODE_S = "c";
    private static final String OPT_CHILDREN_PER_NODE_L = "children";
    
    // id3 options
    private static final String OPT_LABEL_INDEX_S = "l";
    private static final String OPT_LABEL_INDEX_L = "label-index";
    private static final String OPT_TESTING_FILE_S = "t";
    private static final String OPT_TESTING_FILE_L = "testing-file";
    
    private static final String OPT_MIN_K = "min_k";
    private static final String OPT_MAX_K = "max_k";

    private static Options helpOptions;
    private static Options mainOptions;
    private static CommandLine cmd;
    private static String algorithm = null;
    private static String inputFileName = null;
    private static String outputFileName = null;
    private static String delimiter = null;
    
    public static void main(String[] args) {
        
        createOptions();
        parseCommonOptions(args);
            
        // parse remaining options according to which algorithm was specified
        if (algorithm.equalsIgnoreCase("apriori") || algorithm.equalsIgnoreCase("a"))
            apriori();
        else if (algorithm.equalsIgnoreCase("id3") || algorithm.equalsIgnoreCase("i"))
            id3();
        else if(algorithm.equalsIgnoreCase("xmeans") || algorithm.equalsIgnoreCase("x"))
            xMeans();
        else
            err_exit("Unrecognized algorithm specified: " + algorithm);
        
    }
    
    private static void createOptions() {
        helpOptions = new Options();
        mainOptions = new Options();
        
        // create help options
        helpOptions.addOption(Option.builder(OPT_HELP_S).longOpt(OPT_HELP_L).desc("print this message").build());
        
        // create general options
        mainOptions.addOption(Option.builder(OPT_INPUT_FILE_S).required().hasArg().argName("infile").longOpt(OPT_INPUT_FILE_L).desc("the file with data to run algorithm on").build());
        mainOptions.addOption(Option.builder(OPT_ALGORITHM_S).required().hasArg().argName("algorithm").longOpt(OPT_ALGORITHM_L).desc("the algorithm to run on the specified input").build());
        mainOptions.addOption(Option.builder(OPT_OUTPUT_FILE_S).hasArg().argName("outfile").longOpt(OPT_OUTPUT_FILE_L).desc("write output to file").build());
        mainOptions.addOption(Option.builder(OPT_DELIMITER_S).hasArg().argName("delimiter").longOpt(OPT_DELIMITER_L).desc("delimiter separating input attributes").build());
        mainOptions.addOption(Option.builder(OPT_HELP_S).longOpt(OPT_HELP_L).desc("print this message").build());
        
        // create apriori-specific options
        mainOptions.addOption(Option.builder(OPT_MIN_SUP_S).hasArg().argName("min-sup").longOpt(OPT_MIN_SUP_L).desc("apriori: absolute min support (required)").build());
        mainOptions.addOption(Option.builder(OPT_BUCKET_MAX_S).hasArg().argName("bucket-size").longOpt(OPT_BUCKET_MAX_L).desc("apriori: max bucket size for hash trees").build());
        mainOptions.addOption(Option.builder(OPT_CHILDREN_PER_NODE_S).hasArg().argName("#-per-node").longOpt(OPT_CHILDREN_PER_NODE_L).desc("apriori: # of children per node in hash trees").build());
        
        // create id3-specific options
        mainOptions.addOption(Option.builder(OPT_LABEL_INDEX_S).hasArg().argName("label-index").longOpt(OPT_LABEL_INDEX_L).desc("id3: index of class label attribute (required)").build());
        mainOptions.addOption(Option.builder(OPT_TESTING_FILE_S).hasArg().argName("testing-file").longOpt(OPT_TESTING_FILE_L).desc("id3: unlabled data to classify").build());
        
        mainOptions.addOption(Option.builder(OPT_MIN_K).hasArg().argName("min-k").longOpt(OPT_MIN_K).desc("xMeans: Lower K bound").build());
        mainOptions.addOption(Option.builder(OPT_MAX_K).hasArg().argName("max-k").longOpt(OPT_MIN_K).desc("xMeans: Upper K bound").build());
    }
    
    private static void parseCommonOptions(String[] args) {
        
        // parse help options
        try { cmd = new DefaultParser().parse(helpOptions, args, true); }
        catch (ParseException e) { help_exit(mainOptions, e.getMessage()); }
        
        if (cmd.hasOption(OPT_HELP_S))
            help_exit(mainOptions, null);
        
        // parse main options
        try { cmd = new DefaultParser().parse(mainOptions, args); }
        catch (ParseException e) { help_exit(mainOptions, e.getMessage()); }
        
        if (!cmd.hasOption(OPT_ALGORITHM_S) || (algorithm = cmd.getOptionValue(OPT_ALGORITHM_S)) == null)
            err_exit("An algorithm to run on the input must be specified");
        if (!cmd.hasOption(OPT_INPUT_FILE_S) || (inputFileName = cmd.getOptionValue(OPT_INPUT_FILE_S)) == null)
            err_exit("An input file must be specified");
        if (cmd.hasOption(OPT_OUTPUT_FILE_S))
            outputFileName = cmd.getOptionValue(OPT_OUTPUT_FILE_S);
        if (cmd.hasOption(OPT_DELIMITER_S))
            delimiter = cmd.getOptionValue(OPT_DELIMITER_S);
    }
    
    /** Handles the processing of apriori-specific command line arguments, and sends request to run the algorithm. **/
    private static void apriori() {
        String absMinSup = null;
        String maxBucketSize = null;
        String children = null;
        
        // absolute minimum support
        if (cmd.hasOption(OPT_MIN_SUP_S))
            absMinSup = cmd.getOptionValue(OPT_MIN_SUP_S);
        
        // max bucket size
        if (cmd.hasOption(OPT_BUCKET_MAX_S))
            maxBucketSize = cmd.getOptionValue(OPT_BUCKET_MAX_S);
        
        // children per node
        if (cmd.hasOption(OPT_CHILDREN_PER_NODE_S))
            children = cmd.getOptionValue(OPT_CHILDREN_PER_NODE_S);
        
        AprioriSession.getSession().run(inputFileName, delimiter, outputFileName, absMinSup, maxBucketSize, children);
    }
    
    /** Handles the processing of id3-specific command line arguments, and sends request to run the algorithm. **/
    private static void id3() {
        String labelIndex = null;
        String testingFile = null;
        
        // class label index
        if (cmd.hasOption(OPT_LABEL_INDEX_S))
            labelIndex = cmd.getOptionValue(OPT_LABEL_INDEX_S);
        
        // testing file
        if (cmd.hasOption(OPT_TESTING_FILE_S))
            testingFile = cmd.getOptionValue(OPT_TESTING_FILE_L);
        
        ID3Session.getSession().run(inputFileName, delimiter, outputFileName, labelIndex, testingFile);
    }
    
    /** Handles the processing of kmeans-specific command line arguments, and sends request to run the algorithm. **/
    private static void xMeans() {
    	String minK = "";
    	String maxK = "";
    	
    	int lowK = 0, highK = 0;
    	
    	if(cmd.hasOption(OPT_MIN_K))
    		minK = cmd.getOptionValue(OPT_MIN_K);
    	else
    		help_exit(mainOptions, "X Means needs a lower bound set by using -min_k\n");
    	
    	if(cmd.hasOption(OPT_MAX_K))
    		maxK = cmd.getOptionValue(OPT_MAX_K);
    	else
    		help_exit(mainOptions, "X Means needs an upper bound set by using -max_k\n");
    	
    	try{
    		lowK = Integer.parseInt(minK);
    		highK = Integer.parseInt(maxK);
    		
    	}catch(NumberFormatException e){
    		help_exit(mainOptions, "X Means upper and lower bounds must be integers\n");
    	}
    	
    	MeanLauncher.runXMeans(inputFileName, lowK, highK);
    		
    }
    
    /** Prints the specified message to standard output and exits with an error return value. **/
    private static void err_exit(String message) {
        System.err.println(message);
        System.exit(1);
    }
    
    /** Prints a help and usage message and exits with successful return value. **/
    private static void help_exit(Options options, String message) {
        if (message != null)
            System.out.println(message);
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator( (Option arg0, Option arg1)
                -> OPTIONS_ORDER.indexOf(arg0.getOpt()) - OPTIONS_ORDER.indexOf(arg1.getOpt()) );
        helpFormatter.printHelp(PROGRAM_NAME, options, true);
        System.exit(0);
    }

}
