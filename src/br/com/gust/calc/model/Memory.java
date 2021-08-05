package br.com.gust.calc.model;

import java.util.ArrayList;
import java.util.List;

public class Memory {
	
	private enum CommandType{
		ZERO, NUMBER, DIV, MULT, MINUS, PLUS, EQUAL, DOT, SINAL;
	}
	
	private CommandType lastOperation = null;
	private boolean alter = false;
	private static final Memory i = new Memory();
	private String ccText = "";
	private String bufferText = "";
	
	private final List<ObserverMemory> list = new ArrayList<>();
	
	private Memory() {
		// TODO Auto-generated constructor stub
	}

	public static Memory getI() {
		return i;
	}
	public void addObserver(ObserverMemory o) {
		list.add(o);
	}

	public String getCcText() {
		return ccText.isEmpty() ? "0" : ccText;
	}
	
	public void processedCommand(String value) {
				
		CommandType type = detectCommandType(value);
		
		if(type == null) {
			return;
		} else if(type == CommandType.ZERO) {
			ccText = "";
			bufferText = "";
			alter = false;
			lastOperation = null;
			
		} else if(type == CommandType.NUMBER || type == CommandType.DOT) {
			ccText = alter ? value : ccText + value;
			alter = false;
		} else if(type == CommandType.SINAL  && ccText.contains("-")) {
			ccText = ccText.substring(1);
		} else if(type == CommandType.SINAL  && !ccText.contains("-")) {
			ccText = "-" + ccText;
					
		} else {
			alter = true;
			ccText = getResultOperation();
			bufferText = ccText;
			lastOperation = type;
		}
				
		list.forEach(o -> o.updatedValue(getCcText()));
	}

	private String getResultOperation() {
		if (lastOperation == null || lastOperation == CommandType.EQUAL) {
			return ccText;
		}
		double numberBuffer = Double.parseDouble(bufferText.replace(",", "."));
		double numberCurrent = Double.parseDouble(ccText.replace(",", "."));
		
		double result = 0;
		
		if(lastOperation == CommandType.PLUS) {
			result = numberBuffer + numberCurrent;
		}
		else if(lastOperation == CommandType.MINUS) {
			result = numberBuffer - numberCurrent;
		}
		else if(lastOperation == CommandType.DIV) {
			result = numberBuffer / numberCurrent;
		}
		else if(lastOperation == CommandType.MULT) {
			result = numberBuffer * numberCurrent;
		}
		String resultString = Double.toString(result).replace(".", ",");
		boolean integer = resultString.endsWith(",0");
		return integer? resultString.replace(",0", "") : resultString;
	}

	private CommandType detectCommandType(String value) {
		if(ccText.isEmpty() && value == "0") {
			return null;
		}
		
		try {
			Integer.parseInt(value);
			return CommandType.NUMBER;
			
		} catch (NumberFormatException e) {
			if("AC".equals(value)) { 
				return CommandType.ZERO;
			} 
			else if("/".equals(value)) {
				return CommandType.DIV;
			}
			else if("*".equals(value)) {
				return CommandType.MULT;
			}
			else if("+".equals(value)) {
				return CommandType.PLUS;
			}
			else if("-".equals(value)) {
				return CommandType.MINUS;
			}
			else if(",".equals(value) && !ccText.contains(",")) {
				return CommandType.DOT;
			}
			else if("=".equals(value)) {
				return CommandType.EQUAL;
			}
			else if("±".equals(value)) {
				return CommandType.SINAL;
			}
		}
		return null;
	}
	
	
}
