package bg.uni.sofia.fmi.mjt.sentiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Reviewer {

	public static Set<String> extractStopwords(String stopwordsFile) {
		Set<String> stopwords = new HashSet<String>();
		try (BufferedReader file = new BufferedReader(new FileReader(stopwordsFile))) {
			while (file.ready()) {
				stopwords.add(file.readLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stopwords;
	}

	public static void learnWords(String review, Map<String, WordProperties> sentimentDictionary,
			Set<String> stopwords) {
		review = review.toLowerCase();
		String[] words = review.split("[^a-zA-Z0-9]+");
		double reviewScore = Double.parseDouble(words[0]);

		for (int i = 1; i < words.length; ++i) {
			if (!stopwords.contains(words[i]) && sentimentDictionary.containsKey(words[i])) {
				sentimentDictionary.get(words[i]).increaseOccurrence();
				sentimentDictionary.get(words[i]).addScore(reviewScore);
			}

			else if (!stopwords.contains(words[i])) {
				sentimentDictionary.put(words[i], new WordProperties(reviewScore));
			}
		}
	}

	public static void evaluateEachWordScore(Map<String, WordProperties> sentimentDictionary) {
		Collection<WordProperties> words = sentimentDictionary.values();
		for (WordProperties word : words) {
			word.evaluateScore();
		}
	}

}
