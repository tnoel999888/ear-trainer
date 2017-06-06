package EarTrainer.Controllers;/*
*      _______                       _____   _____ _____
*     |__   __|                     |  __ \ / ____|  __ \
*        | | __ _ _ __ ___  ___  ___| |  | | (___ | |__) |
*        | |/ _` | '__/ __|/ _ \/ __| |  | |\___ \|  ___/
*        | | (_| | |  \__ \ (_) \__ \ |__| |____) | |
*        |_|\__,_|_|  |___/\___/|___/_____/|_____/|_|
*
* -------------------------------------------------------------
*
* TarsosDSP is developed by Joren Six at IPEM, University Ghent
*
* -------------------------------------------------------------
*
*  Info: http://0110.be/tag/TarsosDSP
*  Github: https://github.com/JorenSix/TarsosDSP
*  Releases: http://0110.be/releases/TarsosDSP/
*
*  TarsosDSP includes modified source code by various authors,
*  for credits and info, see README.
*
*/


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class InputPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    Mixer mixer = null;
    public JPanel buttonPanel;



    public InputPanel(){
        super(new BorderLayout());
        this.setBorder(new TitledBorder("Choose a microphone input"));
        buttonPanel = new JPanel(new GridLayout(0,1));
        ButtonGroup group = new ButtonGroup();
        for(Mixer.Info info : Shared.getMixerInfo(false, true)){
            List<String> list = Arrays.asList(Shared.toLocalString(info).split(","));
            JRadioButton button = new JRadioButton();
            button.setText(list.get(0));
            buttonPanel.add(button);
            group.add(button);
            button.setActionCommand(info.toString());
            button.addActionListener(setInput);
        }

        this.add(new JScrollPane(buttonPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),BorderLayout.CENTER);
        this.setMaximumSize(new Dimension(220,150));
        this.setPreferredSize(new Dimension(220,150));
    }


    private ActionListener setInput = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent arg0) {
            for(Mixer.Info info : Shared.getMixerInfo(false, true)){
                if(arg0.getActionCommand().equals(info.toString())){
                    Mixer newValue = AudioSystem.getMixer(info);
                    InputPanel.this.firePropertyChange("mixer", mixer, newValue);
                    InputPanel.this.mixer = newValue;
                    break;
                }
            }
        }
    };

}