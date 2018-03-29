package bg.uni.sofia.fmi.mjt.sentiment;

public class WordProperties {
	private int occurrences;
	private double score;

	public WordProperties(double score) {
		this.score = score;
		occurrences = 1;
	}

	public int getOccurrences() {
		return occurrences;
	}

	public double getScore() {
		return score;
	}

	public void increaseOccurrence() {
		++occurrences;
	}

	public void addScore(double score) {
		this.score += score;
	}

	public void evaluateScore() {
		score /= occurrences;
	}

}
