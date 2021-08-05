package br.com.gust.calc.visao;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.gust.calc.model.Memory;
import br.com.gust.calc.model.ObserverMemory;

@SuppressWarnings("serial")
public class Display extends JPanel implements ObserverMemory{
	private final JLabel label;
	
	public Display() {
		Memory.getI().addObserver(this);
		
		
		setBackground(new Color(46, 49, 50));
		label = new JLabel(Memory.getI().getCcText());
		label.setForeground(Color.white);
		label.setFont(new Font("courier", Font.PLAIN, 30));
		
		setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 20));
		add(label);
	}

	@Override
	public void updatedValue(String newValue) {
		label.setText(newValue);
		
	}
	
}
