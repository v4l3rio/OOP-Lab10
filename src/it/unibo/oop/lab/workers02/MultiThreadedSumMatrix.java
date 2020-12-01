package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;



public class MultiThreadedSumMatrix implements SumMatrix{
    
    private final int nthread;
    public int totalSize;

    public MultiThreadedSumMatrix(final int nthred) {
       this.nthread=nthred;
       this.totalSize=0;
    }
    
    private static class Worker extends Thread {
        private final double[][] mat;
        private final int startpos;
        private final int nelem;
        private long res;

        /**
         * Build a new worker.
         * 
         * @param list
         *            the list to sum
         * @param startpos
         *            the initial position for this worker
         * @param nelem
         *            the no. of elems to sum up for this worker
         */
        Worker(final double[][] mat, final int startpos, final int nelem) {
            super();
            this.mat=mat;
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        public void run() {
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            for (int j = startpos; j< mat.length && j < startpos + nelem; j++) {
                for (int i = startpos; i < mat[j].length && i < startpos + nelem; i++) {
                    this.res += this.mat[j][i];
                }
            }
        }

        /**
         * Returns the result of summing up the integers within the list.
         * 
         * @return the sum of every element in the array
         */
        public long getResult() {
            return this.res;
        }

    }
    
    @Override
    public double sum(final double[][] matrix) {
       
        for(int i = 0; i< matrix.length; i++) {
            for(int j = 0; j< matrix[i].length; j++) {
                 this.totalSize++; 
            }
        }
        
        final int size = this.totalSize % nthread + this.totalSize / nthread;
        /*
         * Build a list of workers
         */
        final List<Worker> workers = new ArrayList<>(nthread);
        for (int start = 0; start < this.totalSize; start += size) {
            workers.add(new Worker(matrix, start, size));
        }
        /*
         * Start them
         */
        for (final Worker w: workers) {
            w.start();
        }
        /*
         * Wait for every one of them to finish. This operation is _way_ better done by
         * using barriers and latches, and the whole operation would be better done with
         * futures.
         */
        long sum = 0;
        for (final Worker w: workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        /*
         * Return the sum
         */
        return sum;
    }

}
