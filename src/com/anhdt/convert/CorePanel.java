package com.anhdt.convert;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import org.json.JSONException;
import org.json.JSONObject;

import com.anhdt.convert.core.CoreJSONInput;

public class CorePanel extends JPanel {
	
	private JPanel panelSource;
	private JPanel panelResult;
	private JLabel labelSource;
	private JLabel labelResult;
    private JTextArea source;
    private JTextArea result;
    private JButton convertBtn;
    private CoreJSONInput core;
    public CorePanel() {
    	
    	core = new CoreJSONInput();
    	
    	panelSource = new JPanel(new BorderLayout());
    	labelSource = new JLabel("Enter JSON: ");
    	source = new JTextArea(40, 50);
    	labelSource.setLabelFor(source);
    	panelSource.add(labelSource, BorderLayout.NORTH);
    	panelSource.add(new JScrollPane(source));
    	
    	panelResult = new JPanel(new BorderLayout());
    	labelResult = new JLabel("Result: ");
    	result = new JTextArea(40, 50);
    	labelResult.setLabelFor(result);
    	panelResult.add(labelResult, BorderLayout.NORTH);
    	panelResult.add(new JScrollPane(result));
    	
    	convertBtn = new JButton("Convert");
    	convertBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				core.reset();
				String sourceData = source.getText();
				if (sourceData != null && sourceData.length() > 0) {
					try {
						JSONObject jsonObject = new JSONObject(sourceData);
						core.jsonToJSONObject(jsonObject);
						result.setText(core.getProcessResult());
					} catch (JSONException ex) {
						JOptionPane.showMessageDialog(null, "Something has wrong with your JSON!!!");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please type your JSON!!!");
				}
				
			}
		});
        add(panelSource);
        add(convertBtn);
        add(panelResult);
    }
	
	
}
