package bearmaps.hw4;

import bearmaps.proj2ab.DoubleMapPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private SolverOutcome outcome;
    private double solutionWeight;
    private List<Vertex> solution;
    private double timeSpent;
    private int numFringeOpt;
    private ExtrinsicMinPQ<Vertex> fringe;
    private Map<Vertex, Double> disTo;
    private Map<Vertex, Vertex> edgeTo;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout){
        /** 1. start the watch */
        Stopwatch sw = new Stopwatch();

        /** 2. define and instantiate disTo and edgeTo */
        disTo = new HashMap<>();
        disTo.put(start, 0.0);
        edgeTo = new HashMap<>();
        edgeTo.put(start,null);

        /** 3. define and instantiate fringe and numFringeOpt */
        fringe = new DoubleMapPQ<>();
        fringe.add(start, disTo.get(start) + input.estimatedDistanceToGoal(start,end));
        numFringeOpt = 0;

        /** 4. Add soruce to solution List */
        solution = new ArrayList<>();

        /** 5. Loop until fringe becomes empty or reach goal or watch exceeds timeout */
        while(fringe.size() != 0){
            /** 6. Check if watch exceeds timeout then clear the solution List */
//            if(sw.elapsedTime() > timeout){
//                outcome = SolverOutcome.TIMEOUT;
//                solution.clear();
//                solutionWeight = 0;
//                timeSpent = sw.elapsedTime();
//                return;
//            }

            /** 7. pop the min priority vertex and increment fringe opt */
            Vertex root = fringe.removeSmallest();
            solution.add(root);
            numFringeOpt += 1;

            /** 8. create the list of neighbors for min priority vertex and then traverse */
            List<WeightedEdge<Vertex>> neighborEdges =  input.neighbors(root);
            for(WeightedEdge<Vertex> e : neighborEdges){
                /** 9. Check if goal is reached */
                if(e.to().equals(end)){
                    disTo.put(end, disTo.get(e.from()) + e.weight());
                    edgeTo.put(end, e.from());
                    solution.add(end);
                    outcome = SolverOutcome.SOLVED;
                    solutionWeight = disTo.get(end);
                    timeSpent = sw.elapsedTime();
                    return;
                }

                /** 10. Relax all the edges of p */
                Vertex p = e.from();
                Vertex q = e.to();

                if(!disTo.containsKey(q)){
                    disTo.put(q, Double.MAX_VALUE);
                }
                if(!edgeTo.containsKey(q)){
                    edgeTo.put(q,p);
                }

                double w = e.weight();

                if(disTo.get(p) + w < disTo.get(q)){
                    disTo.put(q, disTo.get(p) + w);
                    if(!fringe.contains(q)){
                        fringe.add(q, disTo.get(q) + input.estimatedDistanceToGoal(q,end));
                    }
                    else{
                        fringe.changePriority(q, disTo.get(q) + input.estimatedDistanceToGoal(q,end));
                    }
                }
            }

        }
        outcome = SolverOutcome.UNSOLVABLE;
        solution.clear();
        solutionWeight = 0;
        timeSpent = sw.elapsedTime();
    }

    public SolverOutcome outcome(){
        return outcome;
    }
    public List<Vertex> solution(){
        return solution;
    }
    public double solutionWeight(){
        return solutionWeight;
    }
    public int numStatesExplored(){
        return numFringeOpt;
    }
    public double explorationTime(){
        return timeSpent;
    }
}

