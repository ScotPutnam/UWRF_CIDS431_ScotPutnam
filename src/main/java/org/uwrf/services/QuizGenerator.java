package org.uwrf.services;

/**
 * Interface for generating quiz questions from a lecture transcript.
 *
 * <p>Two implementations are provided:
 * <ul>
 *   <li>{@link MockQuizGenerator} -- returns canned quiz data; zero AWS cost; use during development</li>
 *   <li>{@link BedrockQuizGenerator} -- calls AWS Bedrock (Claude); use for final submission</li>
 * </ul>
 *
 * <p>Toggle between them by setting the {@code MOCK_BEDROCK} Lambda environment variable:
 * <pre>
 *   MOCK_BEDROCK=true   →  MockQuizGenerator  (development / testing)
 *   MOCK_BEDROCK=false  →  BedrockQuizGenerator (real AI, costs money)
 * </pre>
 */
@FunctionalInterface
public interface QuizGenerator {

    /**
     * Generate quiz questions from a lecture transcript.
     *
     * @param transcript the full text of the lecture transcript
     * @return a JSON string containing an array of quiz question objects, e.g.:
     *         <pre>[{"question":"...","options":{"A":"...","B":"...","C":"...","D":"..."},"correctAnswer":"A"}]</pre>
     * @throws Exception if generation fails
     */
    String generateQuiz(String transcript) throws Exception;
}
