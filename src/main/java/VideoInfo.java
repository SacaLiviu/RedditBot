import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.IOException;

public class VideoInfo {
    public String StaticTitle="";
    public void setStaticTitle(String StaticTitle){
        this.StaticTitle=StaticTitle;
    }
    public String getStaticTitle(){
        return StaticTitle;
    }
    public JsonObject getVideoInfo() throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader("OUTPUT_LOCATION"));
        return gson.fromJson(reader,JsonObject.class);
    }
    public String getVideoTitle() throws IOException {
        VideoInfo titlu = new VideoInfo();
        JsonObject JsonData= titlu.getVideoInfo();
        try {
            JsonArray Items = JsonData.getAsJsonArray("items");
            return Items.get(0)
                    .getAsJsonObject()
                    .get("snippet")
                    .getAsJsonObject()
                    .get("title")
                    .getAsString();
        }
        catch (Exception e){
            return JsonData.get("pageInfo")
                    .getAsJsonObject()
                    .get("totalResults") + " results available. Trying again in 5 minutes";
        }

    }

    public boolean checkTitle() throws IOException {
        boolean titleBoolean=false;
        if (getVideoTitle().equals(getStaticTitle())) {
             titleBoolean=true;

        }
        else {
            setStaticTitle(getVideoTitle());
        }
        return titleBoolean;
    }

    public String getVideoId() throws IOException {
        VideoInfo video = new VideoInfo();
        JsonObject JsonData= video.getVideoInfo();
        try {
            JsonArray Items = JsonData.getAsJsonArray("items");
            return Items.get(0)
                    .getAsJsonObject()
                    .get("contentDetails")
                    .getAsJsonObject()
                    .get("upload")
                    .getAsJsonObject()
                    .get("videoId")
                    .getAsString();
        }
        catch (Exception e){
            return null;
        }
    }
}
