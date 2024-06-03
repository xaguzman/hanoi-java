package com.xavierguzman;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class Main {

    public Main() {
        createAndShowUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    private void createAndShowUI() {
        Canvas canvas = new Canvas();
        JFrame window = new JFrame();
        window.setLayout(new BorderLayout(5, 0));
        window.add(canvas, BorderLayout.CENTER);
        window.add(new InfoPanel(canvas), BorderLayout.WEST);
        window.setTitle("Hanoi Towers Visualization - Xavier Guzman");
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }



    public static class InfoPanel extends JPanel implements Canvas.CanvasListener {

        DefaultListModel<String> steps;

        public InfoPanel(final Canvas canvas){
            JButton start = new JButton("Solve it!");
            start.addActionListener(e -> canvas.startSimulation());

            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

            String[] diskStrings = new String[]{
                    "3 disks",
                    "4 disks",
                    "5 disks",
                    "6 disks",
                    "7 disks",
            };
            SpinnerListModel disksModel = new SpinnerListModel(diskStrings);
            JSpinner spinner = new JSpinner(disksModel);
            spinner.addChangeListener((ChangeEvent e) -> {

                if (canvas.isSimulationRunning())
                    return;

                String val = spinner.getModel().getValue().toString();
                int disks = Integer.parseInt(val.substring(0,1));
                canvas.setDiskCount(disks);
                steps.clear();
            });

            steps = new DefaultListModel<>();
            JList stepsList = new JList<>(steps);
            JScrollPane stepsPane = new JScrollPane(stepsList);
            stepsPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            stepsPane.setPreferredSize(new Dimension(300, 270));


            content.add(Box.createRigidArea(new Dimension(1, 15)));
            content.add(start);
            content.add(Box.createRigidArea(new Dimension(1, 20)));
            content.add(spinner);
            content.add(Box.createRigidArea(new Dimension(1, 20)));
            content.add(new JLabel("Steps"));
            content.add(stepsPane);

            canvas.addListener(this);

            add(content);
            setPreferredSize(new Dimension(300, 400));
        }

        @Override
        public void onStepChange(String message) {
            steps.addElement( steps.size() + 1 + ". " +  message);
        }
    }
}