package org.uwrf.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import org.uwrf.services.BedrockQuizGenerator;
import org.uwrf.services.MockQuizGenerator;
import org.uwrf.services.QuizGenerator;

/**
 * Lambda function that handles S3 events when a video file is uploaded.
 *
 * YOUR TASKS:
 * 1. Call AWS Transcribe to convert the video's audio to text
 * 2. Send the transcript to AWS Bedrock to generate quiz questions
 * 3. Write the quiz JSON back to S3
 *
 * COST TIP: This handler uses a QuizGenerator interface so you can develop locally
 * without paying for Bedrock tokens. Set the Lambda environment variable:
 *   MOCK_BEDROCK=true   → uses MockQuizGenerator (free, returns canned questions)
 *   MOCK_BEDROCK=false  → uses BedrockQuizGenerator (real AI, costs money)
 */
public class VideoHandler implements RequestHandler<S3Event, String> {

    private final QuizGenerator quizGenerator;

    /**
     * Default constructor used by AWS Lambda.
     * Checks the MOCK_BEDROCK environment variable to select the quiz generator.
     */
    public VideoHandler() {
        this("true".equalsIgnoreCase(System.getenv("MOCK_BEDROCK"))
                ? new MockQuizGenerator()
                : new BedrockQuizGenerator());
    }

    /**
     * Constructor for unit tests -- inject any QuizGenerator implementation directly.
     */
    VideoHandler(QuizGenerator quizGenerator) {
        this.quizGenerator = quizGenerator;
    }

    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        System.out.println("=== Lambda Function Triggered ===");
        System.out.println("Received S3 event with " + s3Event.getRecords().size() + " record(s)");

        for (S3EventNotification.S3EventNotificationRecord record : s3Event.getRecords()) {
            String bucketName = record.getS3().getBucket().getName();
            String objectKey = record.getS3().getObject().getKey();
            long objectSize = record.getS3().getObject().getSizeAsLong();
            String eventName = record.getEventName();

            System.out.println("--- S3 Event Details ---");
            System.out.println("Event Type: " + eventName);
            System.out.println("Bucket: " + bucketName);
            System.out.println("File: " + objectKey);
            System.out.println("Size: " + objectSize + " bytes");
            System.out.println("Event Time: " + record.getEventTime());
            System.out.println("------------------------");

            // TODO: Step 1 - Call AWS Transcribe
            // Use the bucketName and objectKey to start a transcription job
            // Hint: TranscribeClient transcribeClient = TranscribeClient.create();
            // Start a job with transcribeClient.startTranscriptionJob(...)
            // Output the transcript JSON to S3 using outputBucketName and outputKey

            // TODO: Step 2 - Wait for transcription to complete and get the text
            // Transcription is async -- poll getTranscriptionJob() until status is COMPLETED
            // Then read the transcript JSON from S3 and extract the text:
            //   transcriptNode.get("results").get("transcripts").get(0).get("transcript").asText()

            // TODO: Step 3 - Call Bedrock with the transcript
            // Replace "transcript" below with the actual transcript string from Step 2:
            //
            //   String quizJson = this.quizGenerator.generateQuiz(transcript);
            //
            // While MOCK_BEDROCK=true this returns canned questions at zero cost.
            // Flip MOCK_BEDROCK=false when you're ready to test real AI generation.

            // TODO: Step 4 - Build the quiz object
            // Wrap the quiz JSON array in an object that includes metadata:
            //   { "sourceVideo": objectKey, "generatedAt": "...", "questions": [...] }

            // TODO: Step 5 - Write the quiz JSON back to S3
            // Use S3Client to put a JSON file at "quizzes/<videoName>-quiz.json"
        }

        return "Processed " + s3Event.getRecords().size() + " record(s)";
    }
}
