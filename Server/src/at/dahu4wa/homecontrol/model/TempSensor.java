package at.dahu4wa.homecontrol.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TempSensor {

	private char id;
	private String name;
	private float tempValue;

	public TempSensor() {
		this.id = ' ';
		this.name = null;
		this.tempValue = 0;
	}

	public TempSensor(char id, String name, float tempValue) {
		this.id = id;
		this.name = name;
		this.tempValue = tempValue;
	}

	public char getId() {
		return id;
	}

	public void setId(char id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getTempValue() {

		float rounded = Math.round(tempValue * 10) / 10F;
		// rounded = rounded - 5.5F;

		return rounded;
	}

	public void setTempValue(float tempValue) {
		this.tempValue = tempValue;
	}

}