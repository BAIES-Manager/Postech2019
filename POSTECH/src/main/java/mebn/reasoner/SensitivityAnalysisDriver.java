package mebn.reasoner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.distribution.NormalDistribution;

import edu.gmu.seor.prognos.unbbayesplugin.cps.CPSCompilerMain;
import edu.gmu.seor.prognos.unbbayesplugin.cps.datastructure.EDB;
import edu.gmu.seor.prognos.unbbayesplugin.cps.datastructure.EDBUnit;
import util.TempMathFunctions;
  

/**
 * This is an example of sensitivity analysis which uses "local" weighted elasticity (aka "local" sensitivity score) as the metric of
 * sensitivity in BN with continuous nodes. It uses DMP algorithm as underlying Bayesian inference algorithm, and apache commons math for obtaining percentiles of normal distribution.
 * See (U.S. Environmental Protection Agency, "Risk Assessment Guidance for Superfund (RAGS) Volume III: Part A," EPA, Washington DC, 2001.) for details about sensitivity score
 * and difference between local and non-local score.
 * TODO this class implements the pseudocode at "A Simple Quasi Monte-Carlo Sensitivity Analysis Method for MSAW project" document, which is not optimized for JAVA & DMP.
 * @author Shou Matsumoto
 */
public class SensitivityAnalysisDriver {
	
	/** Smallest probability that this class can use (avoid zero, to avoid negative infinity in cumulative normal distribution) */
	public static final double MINPROB = 1E-16;
	/** Largest probability that this class can use (avoid 1, in order to avoid positive infinity in cumulative normal distribution) */
	public static final double MAXPROB = 1-MINPROB;
	
	public Map<String, Float> mapPosterior_mean = new HashMap<String, Float>(); 
	public Map<String, Float> mapPosterior_var = new HashMap<String, Float>();
	
	/**
	 * Default constructor without arguments.
	 */
	public SensitivityAnalysisDriver() {}
	
	/**
	 * This method simply delegates to {@link #calculateSensitivity(String, String, String, float, int)} for each input variable.
	 * @param net : the Bayes net descriptor
	 * @param target : the name of the target (output) variable to estimate sensitivity
	 * @param inputs : collection of names of input (free) variables to make perturbation.
	 * @param probBound : when making local changes/perturbations to input variables, changes will be limited to plus or minus this amount from mean. 
	 * This is a relative [0-1] value, not absolute. Therefore, 0.5 will cover the entire space (because mean in normal distribution is at the middle). 
	 * Use a value lower than 0.5 (e.g. 0.1 to cover +10% and -10% from mean).
	 * @param n : number of samples to generate. For elasticity, this number can be small (e.g. less than or equal to 10). 
	 * Even numbers (2,4,6,8,10,12,...) are suggested instead of odd numbers, so that same number of samples can be generated before and after mean in quasi random sequence generator.
	 * @return : mapping from names of input variables to scores of sensitivity (weighted local elasticity).
	 * Ordering this map by such score will rank the inputs by effects on target.
	 * This list is sorted in decreasing order of absolute score (so, variables at the beginning are the ones with highest sensitivity).
	 * @see #calculateSensitivity(String, String, String)
	 */
	public List<Entry<String,Float>> calculateSensitivity(
			String net, 
			String target, 
			List<String> inputs,
			float probBound,
			int n) {
		
		// prepare the list to return
		List<Entry<String,Float>> ret = new ArrayList<Map.Entry<String,Float>>();
		
		// init network to get marginal mean and variance
		initPosterior(net);
		
		// iterate on inputs
		for (String input : inputs) {
			float score = calculateSensitivity(net, target, input, probBound, n);
			ret.add(Collections.singletonMap(input, score).entrySet().iterator().next());
		}
		
		// sort the list by absolute score
		Collections.sort(ret, new Comparator<Entry<String,Float>>() {
			public int compare(Entry<String, Float> o1, Entry<String, Float> o2) {
				// ranking should be in terms of absolute values, from greater to lower
				return Float.compare(Math.abs(o2.getValue()), Math.abs(o1.getValue()));
			}
		});
		
		return ret;
	}

	/**
	 * This is the main method for doing sensitivity analysis.
	 * The code is not very efficient or very well organized, because I'm following precisely the pseudocode in the 
	 * "A Simple Quasi Monte-Carlo Sensitivity Analysis Method for MSAW project" document.
	 * TODO: make it clean for Java, and more efficient for DMP. 
	 * @param net : the Bayes net descriptor
	 * @param output : the name of the target (output) variable to estimate sensitivity
	 * @param input : name of input (free) variable to make perturbation.
	 * @param probBound : when making local changes/perturbations to input variables, changes will be limited to plus or minus this amount from mean. 
	 * This is a relative [0-1] value, not absolute. Therefore, 0.5 will cover the entire space (because mean in normal distribution is at the middle). 
	 * Use a value lower than 0.5 (e.g. 0.1 to cover +10% and -10% from mean).
	 * @param n : number of samples to generate. For elasticity, this number can be small (e.g. less than or equal to 10). 
	 * Even numbers (2,4,6,8,10,12,...) are suggested instead of odd numbers, so that same number of samples can be generated before and after mean in quasi random sequence generator.
	 * @return : scores of sensitivity (weighted elasticity).
	 * @see #calculateSensitivity(String, String, Collection)
	 */
	protected float calculateSensitivity(String net, String output, String input, float probBound, int n) {
		
		// gets a sequence of numbers from 0 to 1
		float[] seq = getSequence(n);
		
		// bounds are + probBound or - probBound from current mean, but not exceeding min/max allowed
		// 50% percentile of normal distribution is the mean
		float lowerPBound = (float) Math.max(0.5 - probBound, MINPROB);
		float upperPBound = (float) Math.min(0.5 + probBound, MAXPROB);
		
		
		// converts the sequence (0-1) to a sequence limited with lower bound and upper bound 
		for (int i = 0; i < seq.length; i++) {
			seq[i] = lowerPBound + (seq[i] * (upperPBound - lowerPBound));
		}
		
		// converts sequence of probabilities to sequence of values (respective percentiles of input)
		float[] inputSamples = getPercentiles(net, input, seq);
		
		// run Bayesian inference for the samples of input
		float[] targetSamples = generateSamples(net, input, inputSamples, output);
		
		return averageScore(net, input, inputSamples, output, targetSamples);
		
	}

	/**
	 * Calculates the average of elasticity of (input,output) pair.
	 * @param net : Bayes net 
	 * @param input : name of input variable
	 * @param inputSamples : samples of input variable
	 * @param output : name of output variable
	 * @param targetSamples : sample of output variable
	 * @return sensitivity score (elasticity in this implementation). Extensions may override this method to implement different metrics (e.g. Spearman's correlation).
	 */
	protected float averageScore(String net, String input, float[] inputSamples,
			String output, float[] targetSamples) {

		if (inputSamples.length != targetSamples.length) {
			throw new IllegalArgumentException("Samples must have same size");
		}
		int n = targetSamples.length;
		
		// prepare target priors
//		float T0 = getPriorMean(net, output) - 0.000001f;
		float T0 = getPriorMean(net, output);
		// prepare input priors
//		float I0 = getPriorMean(net, input)  - 0.000001f;
		float I0 = getPriorMean(net, input);
		float sigma = getPriorStdev(net, input);
		
		// weight to be used for this input. Use weight 1 to disable weighted elasticity
		float weight = sigma/I0;
		 
		// calculate elasticity for each pair
		double sum = 0;
		int ignored = 0;
		for (int sample = 0; sample < n; sample++) {
			float T = targetSamples[sample];
			float I = inputSamples[sample];
			
			// calculate weighted elasticity
//			float score = ((T-T0) / T0) / ((I-I0) / I0) * weight;
			float score = ((T - T0)) / ((I-I0)) ;
			
			
			if (I-I0 == 0) {
				// don't consider cases of no change change.
				score = 0;	
				ignored++;	// ignore this case
			}
			
			sum += score;
		}
		
		// return the average weighted elasticity
		return (float) (sum / (n-ignored));
	}

	/**
	 * Obtains values of output nodes (in this case, a point estimate--mean) given evidences in input node.
	 * @param net : network to propagate evidences
	 * @param input : name of input node
	 * @param inputSamples : evidences in input node
	 * @param output : name of output node
	 * @return : values of output node given each evidence in input
	 */
	protected float[] generateSamples(String net, String input, float[] inputSamples, String output) {
		float[] ret = new float[inputSamples.length];
		for (int i = 0; i < inputSamples.length; i++) {
			ret[i] = getPosterior(net, output, true, input, inputSamples[i]);
		}
		return ret;
	}

	/**
	 * Simply returns percentiles of normal distribution (we are assuming that nodes in net are normal distribution).
	 * This method must be overridden if variables are not normally distributed.
	 * @param net : Bayes net
	 * @param input : node in Bayes net to get percentiles
	 * @param percentiles : probabilities 
	 * @return values of input which has the cumulative probability p
	 */
	protected float[] getPercentiles(String net, String input, float[] percentiles) {
		
		// assume normal distribution
		float mean = getPriorMean(net, input);
		float stdev = getPriorStdev(net, input);
		NormalDistribution normal = new NormalDistribution(mean, stdev);
		
		// get percentiles
		float[] ret = new float[percentiles.length];
		for (int i = 0; i < percentiles.length; i++) {
			float p = percentiles[i];
			ret[i] = (float) normal.inverseCumulativeProbability(p);
		}
		return ret;
	}

	/**
	 * Generates a sequence of numbers (usually, a random sequence) between 0 and 1.
	 * In this example, the sequence is deterministic (quasi monte carlo method):  [0.025, 0.075, 0.125, 0.175, ..., 0.875, 0.925, 0.975]
	 * Extensions should override this method in order to implement other random or quasi-random sequence generator.
	 * @param numSamples : how many samples to generate. Use 20 as default.
	 * @return random or quasi-random sequence of numbers between 0 and 1
	 */
	protected float[] getSequence(int numSamples) {
		
		float[] ret = new float[numSamples];
		
		float val = 0.025f;
		float increment = (float) ((0.975-0.025) / (numSamples-1f));
		for (int i = 0; i < numSamples; i++) {
			ret[i] = val;
			val += increment;
		}
		
		return ret;
	}
	

	/**
	 * Calculates cumulative probability of normal distribution.
	 * @param x : value to calculate cumulative probability
	 * @param mean : mean of normal distribution
	 * @param stdev : standard deviation of normal distribution
	 * @return obtains the cumulative probability of x in a normal distribution.
	 * @see #getInverseCDFNormal(float, float, float)
	 */
	protected double getCDFNormal(float x, float mean, float stdev) {
		NormalDistribution inputNormal = new NormalDistribution(mean, stdev);
		return inputNormal.cumulativeProbability(x);
	}
	
	/**
	 * Calculates inverse cumulative probability (i.e. quantile/percentile function) of normal distribution.
	 * @param p : cumulative probability
	 * @param mean : mean of normal distribution
	 * @param stdev : standard deviation of normal distribution
	 * @return obtains the inverse of CDF of normal distribution (i.e. value of random variable specified by a cumulative probability)
	 * @see #getCDFNormal(float, float, float)
	 */
	protected double getInverseCDFNormal(float p, float mean, float stdev) {
		NormalDistribution inputNormal = new NormalDistribution(mean, stdev);
		return inputNormal.inverseCumulativeProbability(p);
	}

	/**
	 * Obtains the standard deviation from prior distribution of network
	 * @param net : BN to get prior distribution from
	 * @param nodeName : name of node to get standard deviation
	 * @return : standard deviation of node.
	 */
	protected float getPriorStdev(String net, String nodeName) {
		return getPosterior(nodeName, false);
	}

	/**
	 * Obtains the mean from prior distribution of network
	 * @param net : BN to get prior distribution from
	 * @param nodeName : name of node to get mean
	 * @return : mean of node.
	 */
	protected float getPriorMean(String net, String nodeName) {
		return getPosterior(nodeName, true);
	}
	
	/**
	 * Calculates posterior distribution from a Bayes net.
	 * If evidence is not set, it calculates prior distribution.
	 * @param net : BN to get prior distribution from
	 * @param nodeName : name of node to get mean
	 * @param isMean : if true, this method returns the mean. If false, this method returns the standard deviation.
	 * @param evidenceName : name of node to insert evidence. If null/empty, prior probability will be returned.
	 * @param evidenceValue : value of evidence.
	 * @return : mean or standard deviation.
	 */
	protected float getPosterior(String net, String nodeName, boolean isMean, String evidenceName, float evidenceValue) {
		
		TempMathFunctions tmath = new TempMathFunctions();
		String ev = tmath.getSafeNumberString(Float.valueOf(evidenceValue).toString());
		
		// Run CPS with no evidence
	 	CPSCompilerMain cpsCompiler = new CPSCompilerMain();
        cpsCompiler.InitCompiler(); 
        if (evidenceName == null || evidenceName.trim().isEmpty() ) {
        	cpsCompiler.compile( net +
        			"run(DMP);" );
        } else {
        	cpsCompiler.compile( net +
        			"defineEvidence( " + evidenceName + ", "+ ev +" );"+
        			"run(DMP);" );
        }
         
        EDBUnit dmp = EDB.This().get("ROOT.ENGINES.DMP"); 
        EDBUnit nodes = dmp.get("NODES"); 
 		EDBUnit ISA = nodes.getRel("ISA");  
		

		EDBUnit node = ISA.map.get(nodeName);
		EDBUnit evidence = node.get("EVIDENCE");
		EDBUnit bel = node.get("BEL");

		if (evidence != null) { 
			
			return isMean ? evidence.getDataByDouble().floatValue() : 0;
			
		} else if (bel != null) { 
			
			bel.printOnAString("         ");	// TODO I copied this from an example. Need to check what this is for...
			 
			if (isMean) {
				return (float) bel.get("MU").getMatrixData();
			} else {
			
				double variance = bel.get("SIGMA").getMatrixData();	// TODO accordingly to test_dmp.java, this is variance... Make sure this is correct.
				
				return (float) Math.sqrt(Math.abs(variance));		// convert to standard deviation
			}
		}
		
		throw new RuntimeException("Unable to calculate prior of node " + nodeName + " in net: \n" + net);
	}
	
	protected float getPosterior(String nodeName, boolean isMean) {
		if (isMean) {
			return mapPosterior_mean.get(nodeName);
		} else {
			return (float) Math.sqrt(Math.abs(mapPosterior_var.get(nodeName)));	// convert to standard deviation
		}
	}
		
	protected void initPosterior(String net) {
		 
		// Run CPS with no evidence
	 	CPSCompilerMain cpsCompiler = new CPSCompilerMain();
        cpsCompiler.InitCompiler();  
    	cpsCompiler.compile( net + "run(DMP);" );
    
        EDBUnit dmp = EDB.This().get("ROOT.ENGINES.DMP"); 
        EDBUnit nodes = dmp.get("NODES"); 
 		EDBUnit ISA = nodes.getRel("ISA");  
		
 		for (String str : ISA.getMap().keySet()) { 
 			EDBUnit node = ISA.map.get(str);
 			EDBUnit evidence = node.get("EVIDENCE");
 			EDBUnit bel = node.get("BEL");
 			
 			System.out.println(" " + str + ":");
 			
 			if (evidence != null) {
 				System.out.println(evidence.printOnAString("         "));
 				System.out.println("EVIDENCE: " + evidence.getDataByDouble());
 			} else
 			if (bel != null) { 
 				System.out.println(bel.printOnAString("         "));
 				System.out.println("MU: " + bel.get("MU").getMatrixData());
 				System.out.println("SIGMA: " + bel.get("SIGMA").getMatrixData()); // "SIGMA" is an variance.
 			}
 			String mean = String.valueOf(bel.get("MU").getMatrixData());
 			mapPosterior_mean.put(str, Float.valueOf(mean));
 			
 			String var = String.valueOf(bel.get("SIGMA").getMatrixData());
 			mapPosterior_var.put(str, Float.valueOf(var));
 		}    
	}



	/**
	 * Simply run {@link #SensitivityAnalysisTestDriver()} in a toy network with 3 nodes.
	 * @param args
	 */
	public static void main(String[] args) {
		String net = new String(
				"defineNode(X, desc);"+
						"{ defineState(Continuous);" +
						"p( X ) = NormalDist( " + 2 + ", " + 2.5 + " ); }" +
						 
						"defineNode(Y, desc);"+
					  	"{ defineState(Continuous);" +
					  	"p( Y | X) = 0.5*X + NormalDist( " + 2 + ", " + 1 + " ); }" + 
						
					  	"defineNode(Z, desc);"+
					  	"{ defineState(Continuous);" +
					  	"p( Z | X, Y) =  X - 0.5*Y +  NormalDist( " + 3 + ", " + 0.5 + " ); }"   
					  	);
		
		List<String> inputs = new ArrayList<String>();
		inputs.add("X");
		inputs.add("Y");
		
		// prepare calculator of ranking (with score of sensitivity)
		SensitivityAnalysisDriver driver = new SensitivityAnalysisDriver();
		
		// looks like 4 samples is enough for this net and config. Even numbers [2,4,6,8,...] are suggested
		List<Entry<String, Float>> ranking = driver.calculateSensitivity(net, "Z", inputs, 0.5f, 20); 
		
		// print the ranking
		for (int i = 0; i < ranking.size(); i++) {
			System.out.println("Rank" + (i+1) + ": Node = " + ranking.get(i).getKey() + ", score = " + ranking.get(i).getValue());
		}

	}

}