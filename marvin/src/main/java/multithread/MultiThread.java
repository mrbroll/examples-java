package marvin;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;
import marvin.thread.MarvinThread;
import marvin.thread.MarvinThreadEvent;
import marvin.thread.MarvinThreadListener;

public class MultiThread extends JFrame implements MarvinThreadListener {
    private JButton buttonSingleThread, buttonMultiThread, buttonReset;
    private JLabel labelPerformance;
    private MarvinImagePanel imagePanel;
    private MarvinImage imageIn, imageOut, originalImage;
    private int threadsFinished;
    private long processStartTime;

    public MultiThread() {
        super("Java Fun");

        ButtonHandler buttonHandler = new ButtonHandler();
        buttonSingleThread = new JButton("Single Thread");
        buttonMultiThread = new JButton("Multi Thread");
        buttonReset = new JButton("Reset");
        //Create GUI
        buttonSingleThread.addActionListener(buttonHandler);
        buttonMultiThread.addActionListener(buttonHandler);
        buttonReset.addActionListener(buttonHandler);

        labelPerformance = new JLabel("Performance:");
        JPanel panelIntern = new JPanel();
        panelIntern.add(buttonSingleThread);
        panelIntern.add(buttonMultiThread);
        panelIntern.add(buttonReset);

        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new BorderLayout());
        
        panelBottom.add(panelIntern, BorderLayout.NORTH);
        panelBottom.add(labelPerformance, BorderLayout.SOUTH);

        //ImagePanel
        imagePanel = new MarvinImagePanel();

        Container l_c = getContentPane();
        l_c.setLayout(new BorderLayout());
        l_c.add(panelBottom, BorderLayout.SOUTH);
        l_c.add(imagePanel, BorderLayout.NORTH);

        //Load Image
        loadImages();

        setSize(340, 430);
        setVisible(true);
    }

    private void loadImages() {
        originalImage = MarvinImageIO.loadImage("./res/test.jpg");
        imageIn = new MarvinImage(originalImage.getWidth(), originalImage.getHeight());
        imageOut = new MarvinImage(originalImage.getWidth(), originalImage.getHeight());
        imagePanel.setImage(originalImage);
    }

    public void threadFinished(MarvinThreadEvent e) {
        threadsFinished++;
        if(threadsFinished == 2) {
            imageOut.update();
            imagePanel.setImage(imageOut);
            labelPerformance.setText("Performance: " + (System.currentTimeMillis() - processStartTime) + " ms (MultiThread)");
        }
    }

    public static void main(String[] args) {
        MultiThread jf = new MultiThread();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void singleThread() {
        processStartTime = System.currentTimeMillis();
        MarvinImagePlugin plgImage = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.statistical.maximum.jar");
        plgImage.process(imageIn, imageOut);
        imageOut.update();
        imagePanel.setImage(imageOut);
        labelPerformance.setText("Performance: " + (System.currentTimeMillis() - processStartTime) + " ms (Single Thread)");
        repaint();
    }

    private void multiThread() {
        processStartTime = System.currentTimeMillis();
        MarvinImagePlugin plgImage1 = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.statistical.maximum.jar");
        MarvinImagePlugin plgImage2 = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.statistical.maximum.jar");

        MarvinImageMask mask1 = new MarvinImageMask(imageIn.getWidth(),
                                                    imageIn.getHeight(),
                                                    0,
                                                    0,
                                                    imageIn.getWidth(),
                                                    imageIn.getHeight()/2);
        MarvinImageMask mask2 = new MarvinImageMask(imageIn.getWidth(),
                                                    imageIn.getHeight(),
                                                    0,
                                                    imageIn.getHeight()/2,
                                                    imageIn.getWidth(),
                                                    imageIn.getHeight()/2);

        MarvinThread thread1 = new MarvinThread(plgImage1, imageIn, imageOut, mask1);
        MarvinThread thread2 = new MarvinThread(plgImage2, imageIn, imageOut, mask2);
        thread1.addThreadListener(this);
        thread2.addThreadListener(this);
        thread1.start();
        thread2.start();
        threadsFinished = 0;
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            imageIn = originalImage.clone();
            if (e.getSource() == buttonSingleThread) {
                singleThread();
            } else if (e.getSource() == buttonMultiThread) {
                multiThread();
            } else if (e.getSource() == buttonReset) {
                imagePanel.setImage(originalImage);
            }
        }
    }
}
