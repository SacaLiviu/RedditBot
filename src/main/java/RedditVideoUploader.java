import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ActivityListResponse;

import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

public class RedditVideoUploader {
    private static final String DEVELOPER_KEY = "GOOGLE_DEVELOPER_API_KEY";
    private static final String APPLICATION_NAME = "APP Name";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Call function to create API service object. Define and
     * execute API request. Print API response.
     *
     * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
     */
    public static void main(String[] args)
            throws GeneralSecurityException, IOException, InterruptedException {
        YouTube youtubeService = getService();
        // Define and execute the API request
        YouTube.Activities.List request = youtubeService.activities()
                .list(Collections.singletonList("snippet,contentDetails"));
        //Date
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.s'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        VideoInfo videoInfo = new VideoInfo();
        Reddit redditInfo = new Reddit();
        String VideoId;

        /* YYYY-MM-DDThh:mm:ss.s */
        while(true) {
            try {
                FileWriter output = new FileWriter("OUTPUT_LOCATION");
                String nowAsISO = df.format(new Date(System.currentTimeMillis() - 180000 * 1000)); //curr date - 1day (mil*300)
                ActivityListResponse response = request.setKey(DEVELOPER_KEY)
                        .setChannelId("CHANNEL_ID")
                        .setMaxResults(1L)
                        .setPublishedAfter(nowAsISO)
                        .setPrettyPrint(true)
                        .execute();
                output.write(response.toPrettyString());
                output.close();
                videoInfo.setStaticTitle(videoInfo.getVideoTitle());
                if (videoInfo.checkTitle()) {
                    VideoId = videoInfo.getVideoId();
                    if (VideoId != null) {
                        String LinkVideo = "https://www.youtube.com/watch?v=" + VideoId;
                        redditInfo.submitToReddit(videoInfo.getVideoTitle(), LinkVideo);
                    }
                }
                Thread.sleep(120 * 1000);
                System.out.println("Checking again");
            }
            catch(NullPointerException e){
                System.out.println("An error has occured");
                Thread.sleep(120 * 1000);
            }
        }
    }
}