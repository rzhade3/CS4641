package project2;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.SwapNeighbor;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.SwapMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.*;
//import shared.FixedIterationTrainer;

/**
 *
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class TravelingSalesmanTest {
    /** The n value */
    private static final int N = 50;
    private static DecimalFormat def = new DecimalFormat("0.000");
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Random random = new Random();
        // create the random points
        double[][] points = new double[N][2];
        for (int i = 0; i < points.length; i++) {
            points[i][0] = random.nextDouble();
            points[i][1] = random.nextDouble();
        }
        // for rhc, sa, and ga we use a permutation based encoding
        TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanRouteEvaluationFunction(points);
        Distribution odd = new DiscretePermutationDistribution(N);
        NeighborFunction nf = new SwapNeighbor();
        MutationFunction mf = new SwapMutation();
        CrossoverFunction cf = new TravelingSalesmanCrossOver(ef);
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);






        long starttime = System.currentTimeMillis();
        double start, trainingTime, end = 0;
        System.out.println("Randomized Hill Climbing");
        RandomizedHillClimbing rhc = null;
        start = System.nanoTime();
        rhc = new RandomizedHillClimbing(hcp);
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 500);
        for (int i = 500; i <= 20000; i += 500) {
            System.out.println("----------" + i + " iterations-----------");
            fit.train();
            System.out.println("RHC: " + ef.value(rhc.getOptimal()));
            System.out.println("Time : " + def.format((System.currentTimeMillis() - starttime) / Math.pow(10, 3)) + " seconds");

        }
        System.out.println("============================");







        System.out.println("Simulated Annealing");
        starttime = System.currentTimeMillis();
        SimulatedAnnealing sa = new SimulatedAnnealing(1E10,.8, hcp);
        fit = new FixedIterationTrainer(sa, 500);
        for (int i = 500; i <= 20000; i += 500) {
            System.out.println("----------" + i + " iterations-----------");
            fit.train();
            System.out.println("SA: " + ef.value(sa.getOptimal()));
            System.out.println("Time : " + def.format((System.currentTimeMillis() - starttime) / Math.pow(10, 3)) + " seconds");
        }
        System.out.println("============================");







        starttime = System.currentTimeMillis();
        System.out.println("Genetic Algorithm");
        StandardGeneticAlgorithm ga = null;
        ga = new StandardGeneticAlgorithm(200, 150, 20, gap);
        fit = new FixedIterationTrainer(ga, 500);
        for (int i = 500; i <= 20000; i += 500) {
            System.out.println("----------" + i + " iterations-----------");
            fit.train();
            System.out.println("GA: " + ef.value(ga.getOptimal()));
            System.out.println("Time : " + def.format((System.currentTimeMillis() - starttime) / Math.pow(10, 3)) + " seconds");
        }
        System.out.println("============================");





        // for mimic we use a sort encoding
        System.out.println("MIMIC");
        ef = new TravelingSalesmanSortEvaluationFunction(points);
        int[] ranges = new int[N];
        Arrays.fill(ranges, N);
        odd = new  DiscreteUniformDistribution(ranges);
        Distribution df = new DiscreteDependencyTree(.1, ranges);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

        starttime = System.currentTimeMillis();
        MIMIC mimic = new MIMIC(200, 100, pop);
        fit = new FixedIterationTrainer(mimic, 500);
        for (int i = 500; i <= 20000; i += 500) {
            System.out.println("----------" + i + " iterations-----------");
            fit.train();
            System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
            System.out.println("Time : " + def.format((System.currentTimeMillis() - starttime) / Math.pow(10, 3)) + " seconds");

        }
    }
}
