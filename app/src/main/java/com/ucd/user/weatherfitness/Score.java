package com.ucd.user.weatherfitness;

import android.util.Log;

public class Score {

	private String precip;
	private double temp;
	private double humidity;
	private double wind;
	
	public Score(String p, double t, double h, double w){
		precip = p;
		temp = t;
		humidity = h;
		wind = w;
	}

	public int calculateScore() {
		
		int precipScore = precipScore();
		int tempScore = tempScore();
		int humidityScore = humidityScore();
		int windScore = windScore();
		
		int score = (precipScore + tempScore + humidityScore + windScore) / 4;
        Log.d("Precip score", String.valueOf(precipScore));
        Log.d("Temp score", String.valueOf(tempScore));
        Log.d("Humidity score", String.valueOf(humidityScore));
        Log.d("Wind score", String.valueOf(windScore));

        return score;
	}
	
	private int precipScore() {
		int score = 0;
		
		if (precip.equalsIgnoreCase("Clear"))
			score = 10;
		else if (precip.equalsIgnoreCase("Clouds"))
			score = 9;
        else if (precip.equalsIgnoreCase("Drizzle"))
            score = 7;
        else if (precip.equalsIgnoreCase("Rain"))
			score = 6;
        else if (precip.equalsIgnoreCase("Thunderstorm"))
            score = 4;
        else if (precip.equalsIgnoreCase("Snow"))
			score = 2;
		else if (precip.equalsIgnoreCase("Extreme"))
			score = 1;
		return score;
	}
	
	private int windScore() {
		// Wind measured in m/s
		// Based on the beaufort Scale
		int score = 0;
		
		if (wind < 0.3) 
			score = 10;
		else if (wind < 1.5)
			score = 9;
		else if (wind < 3.3)
			score = 8;
		else if (wind < 5.5)
			score = 7;
		else if (wind < 7.9)
			score = 6;
		else if (wind < 10.7)
			score = 5;
		else if (wind < 13.8)
			score = 4;
		else if (wind < 17.9)
			score = 3;
		else if (wind < 20.7)
			score = 2;
		else if (wind < 24.8)
			score = 1;
		else
			score = 0;
		
		return score;
	}
	
	private int tempScore() {
		// Temperature measured in Fahrenhight
		
		int score = 0;
		
		if (temp <= -5)	// - 5C
			score = 1;
		else if(temp  < 5 )	// 0C
			score = 2;
		else if(temp  < 10)	// 5C
			score = 5;
		else if(temp  < 15)	// 10C
			score = 7;
		else if(temp  < 20) // 15C
			score = 9;
		else if(temp  < 25) // 20C
			score = 10;
		else if(temp  < 30) // 25C
			score = 8;
		else if(temp  < 35) // 30C
			score = 5;
		else if(temp >= 35) // 35C
			score = 1;

		return score;
	}

	private int humidityScore() {
		// Humidity measured as percentage of water vapor/m^3 volume of air
		
		int score = 0;
		
		if (humidity < 10)
			score = 10;
		else if(humidity < 20)
			score = 9;
		else if(humidity < 30)
			score = 8;
		else if(humidity < 40)
			score = 6;
		else if(humidity < 60)
			score = 4;
		else if(humidity < 80) 
			score = 2;
		else if(humidity <= 100) 
			score = 1;

		
		return Math.round(score);
	}
}
