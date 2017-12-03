package com.ucd.user.weatherfitness;

import android.util.Log;


class Score {
	// calculates the Score for the weather, based on several weather dimensions

	private String precip;
	private double temp;
	private double humidity;
	private double wind;
	
	Score(String p, double t, double h, double w){
		precip = p;
		temp = t;
		humidity = h;
		wind = w;
	}

	int calculateScore() {
		// Calculates the score and returns it
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
		// Generates score for precipitation description
		int score = 0;
		
		if (precip.equalsIgnoreCase("Clear"))
			score = 10;
		else if (precip.equalsIgnoreCase("Clouds"))
			score = 9;
        else if (precip.equalsIgnoreCase("Drizzle"))
            score = 8;
        else if (precip.equalsIgnoreCase("Rain"))
			score = 7;
        else if (precip.equalsIgnoreCase("Thunderstorm"))
            score = 3;
        else if (precip.equalsIgnoreCase("Snow"))
			score = 2;
		else if (precip.equalsIgnoreCase("Extreme"))
			score = 1;
		return score;
	}
	
	private int windScore() {
		// Generate Score based on wind measured in m/s
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
		
		return Math.round(score);
	}
	
	private int tempScore() {
		// Generate Score based on temperature in Celsius
		int score = 0;
		
		if (temp <= -5)	// - 5C
			score = 1;
		else if(temp  < 5 )	// 0C
			score = 3;
		else if(temp  < 10)	// 5C
			score = 6;
		else if(temp  < 15)	// 10C
			score = 9;
		else if(temp  < 20) // 15C
			score = 10;
		else if(temp  < 25) // 20C
			score = 10;
		else if(temp  < 30) // 25C
			score = 7;
		else if(temp  < 35) // 30C
			score = 5;
		else if(temp >= 35) // 35C
			score = 2;
		return Math.round(score);
	}

	private int humidityScore() {
		// Generate Score based on Humidity
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
