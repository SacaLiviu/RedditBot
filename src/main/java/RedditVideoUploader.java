
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ActivityListResponse;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

public class RedditVideoUploader {
    // You need to set this value for your code to compile.
    // For example: ... DEVELOPER_KEY = "YOUR ACTUAL KEY";
    private static final String DEVELOPER_KEY = "";
    private static final String REDDIT_DEVELOPER_KEY="";
    private static final String APPLICATION_NAME = "";
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
            throws GeneralSecurityException, IOException, ParseException, InterruptedException {
        YouTube youtubeService = getService();
        // Define and execute the API request
        YouTube.Activities.List request = youtubeService.activities()
                .list(Collections.singletonList("snippet,contentDetails"));
        //Date
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.s'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        while(true) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setStaticTitle(videoInfo.getVideoTitle());
            Reddit redditInfo = new Reddit();
            String VideoId;
            String nowAsISO = df.format(new Date(System.currentTimeMillis() - 86400 * 1000)); //curr date - 1day (mil*300)
            FileWriter output = new FileWriter("");
            /* YYYY-MM-DDThh:mm:ss.s */
            ActivityListResponse response = request.setKey(DEVELOPER_KEY)
                    .setChannelId("")
                    .setMaxResults(1L)
                    .setPublishedAfter(nowAsISO)
                    .setPrettyPrint(true)
                    .execute();
            output.write(response.toPrettyString());
            output.close();
            if (videoInfo.checkTitle()) {
                VideoId = videoInfo.getVideoId();
                String LinkVideo = "https://www.youtube.com/watch?v=" + VideoId;
                redditInfo.submitToReddit(videoInfo.getVideoTitle(), LinkVideo);
            }
            Thread.sleep(120*1000);
            System.out.println("Checking again");
        }
    }
}