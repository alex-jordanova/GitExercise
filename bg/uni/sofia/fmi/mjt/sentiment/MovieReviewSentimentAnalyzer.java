package bg.uni.sofia.fmi.mjt.sentiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer {

	private Set<String> stopwords;
	private Map<String, WordProperties> sentimentDictionary;

	public MovieReviewSentimentAnalyzer(String reviewsFileName, String stopwordsFileName) {
		stopwords = Reviewer.extractStopwords(stopwordsFileName);
		sentimentDictionary = new HashMap<>();

		try (BufferedReader reviewsToAnalyze = new BufferedReader(new FileReader(reviewsFileName))) {
			while (reviewsToAnalyze.ready()) {
				Reviewer.learnWords(reviewsToAnalyze.readLine(), sentimentDictionary, stopwords);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Reviewer.evaluateEachWordScore(sentimentDictionary);
	}

	@Override
	public double getReviewSentiment(String review) {
		double reviewScore = 0.0;
		int occurredKnownWords = 0;

		if (review != null) {
			review = review.toLowerCase();
			String[] containedWords = review.split("[^a-zA-Z0-9]+");

			for (String word : containedWords) {
				if (sentimentDictionary.containsKey(word)) {
					reviewScore += sentimentDictionary.get(word).getScore();
					++occurredKnownWords;
				}
			}
		}

		return occurredKnownWords != 0 ? reviewScore / occurredKnownWords : ScoreRepresentation.UNKNOWN.getScore();
	}

	@Override
	public String getReviewSentimentAsName(String review) {
		return ScoreRepresentation.getScoreDescription(getReviewSentiment(review));
	}

	@Override
	public double getWordSentiment(String word) {
		if (sentimentDictionary.containsKey(word)) {
			return sentimentDictionary.get(word).getScore();
		}

		return ScoreRepresentation.UNKNOWN.getScore();
	}

	@Override
	public Collection<String> getMostFrequentWords(int n) {
		return sortDictionary(mostFrequentWords(), n);
	}

	@Override
	public Collection<String> getMostPositiveWords(int n) {
		return sortDictionary(mostPositiveWords(), n);
	}

	@Override
	public Collection<String> getMostNegativeWords(int n) {
		return sortDictionary(mostNegativeWords(), n);
	}

	@Override
	public int getSentimentDictionarySize() {
		return sentimentDictionary.size();
	}

	@Override
	public boolean isStopWord(String word) {
		return stopwords.contains(word);
	}
	
	private Collection<String> sortDictionary(Comparator<String> wordComparator, int n) {
		return sentimentDictionary.keySet().stream()
				.sorted(wordComparator)
				.limit(n).collect(Collectors.toList());
	}

	private Comparator<String> mostFrequentWords() {
		return new Comparator<String>() {

			@Override
			public int compare(String word1, String word2) {
				return Integer.compare(sentimentDictionary.get(word2).getOccurrences(),
						sentimentDictionary.get(word1).getOccurrences());
			}

		};
	}

	private Comparator<String> mostPositiveWords() {
		return new Comparator<String>() {

			@Override
			public int compare(String word1, String word2) {
				return Double.compare(sentimentDictionary.get(word2).getScore(),
						sentimentDictionary.get(word1).getScore());
			}

		};
	}

	private Comparator<String> mostNegativeWords() {
		return new Comparator<String>() {

			@Override
			public int compare(String word1, String word2) {
				return Double.compare(sentimentDictionary.get(word1).getOccurrences(),
						sentimentDictionary.get(word2).getOccurrences());
			}

		};
	}

}
