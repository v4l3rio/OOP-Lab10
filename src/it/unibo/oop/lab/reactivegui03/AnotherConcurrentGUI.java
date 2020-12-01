package it.unibo.oop.lab.reactivegui03;

import it.unibo.oop.lab.reactivegui02.ConcurrentGUI;

public class AnotherConcurrentGUI extends ConcurrentGUI {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * spotbugs volutamente ignorato
     */
    public AnotherConcurrentGUI() {
        super();
        final Agent2 agent2 = new Agent2();
        new Thread(agent2).start();
    }

    private class Agent2 implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            actionPerformed(null);
        }
    }

}
