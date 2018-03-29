package bg.uni.sofia.fmi.mjt.sentiment;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum ScoreRepresentation {
	UNKNOWN(-1.0, "unknown"), 
	NEGATIVE(0.0, "negative"), 
	SNEGATIVE(1.0, "somewhat negative"), 
	NEUTRAL(2.0, "neutral"),
	SPOSITIVE(3.0, "somewhat positive"),
	POSITIVE(4.0, "positive");

	private final double score;
	private final String scoreDescription;

	private ScoreRepresentation(double score, String scoreDescription) {
		this.score = score;
		this.scoreDescription = scoreDescription;
	}

	private static double roundScore(double reviewScore) {
		BigDecimal roundedScore = new BigDecimal(Double.toString(reviewScore));
		roundedScore = roundedScore.setScale(0, RoundingMode.HALF_UP);
		return roundedScore.doubleValue();
	}

	public double getScore() {
		return score;
	}
	
	private String getScoreDescription() {
		return scoreDescription;
	}
	
	public static String getScoreDescription(double reviewScore) {
		reviewScore = roundScore(reviewScore);

		if (Double.compare(reviewScore, NEGATIVE.getScore()) == 0) {
			return NEGATIVE.getScoreDescription();
		}

		if (Double.compare(reviewScore, SNEGATIVE.getScore()) == 0) {
			return SNEGATIVE.getScoreDescription();
		}

		if (Double.compare(reviewScore, NEUTRAL.getScore()) == 0) {
			return NEUTRAL.getScoreDescription();
		}

		if (Double.compare(reviewScore, SPOSITIVE.getScore()) == 0) {
			return SPOSITIVE.getScoreDescription();
		}

		if (Double.compare(reviewScore, POSITIVE.getScore()) == 0) {
			return POSITIVE.getScoreDescription();
		}

		return UNKNOWN.getScoreDescription();
	}
}
