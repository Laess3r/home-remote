package at.dahuawa.homecontrolng.model;

public class TempSensor {

	private char id;
	private String name;
	private float tempValue;
	private float humidity;

	public TempSensor() {
		this.id = ' ';
		this.name = null;
		this.tempValue = 0;
	}

	public TempSensor(char id, String name, float tempValue) {
		this.id = id;
		this.name = name;
		this.tempValue = tempValue;
		this.humidity = -1F;
	}

	public TempSensor(char id, String name, float tempValue, float humidity) {
		this.id = id;
		this.name = name;
		this.tempValue = tempValue;
		this.humidity = humidity;
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

		// // correct sensor A temp value
		// if(id == 'A'){
		// float f = tempValue+3;
		// return (float) (Math.ceil(f * 2) / 2);
		// }

		return tempValue;
	}

	public void setTempValue(float tempValue) {
		this.tempValue = tempValue;
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

};