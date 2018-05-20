package thibaut.photogran;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SecondeActivity extends AppCompatActivity {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seconde);

        createNotificationChannel();

        Button btn_account = findViewById(R.id.btn_download);
        btn_account.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                GetPhotosService.startActionGetPhotos(SecondeActivity.this);
                Toast.makeText(getApplicationContext(), getString(R.string.toast), Toast.LENGTH_SHORT).show();
            }
        });
        IntentFilter intentFilter = new IntentFilter(PHOTOS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new PhotoUpdate(),intentFilter);

        rv = findViewById(R.id.rv_photos);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv.setAdapter(new PhotosAdapter(getPhotosFromFile()));
    }

    public static final String PHOTOS_UPDATE = "thibaut.photogran.PHOTOS_UPDATE";

    public class PhotoUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            PhotosAdapter ba = (PhotosAdapter) rv.getAdapter();
            ba.setNewPhoto(getPhotosFromFile());
            createNotification();
            Log.d("SecondActivity", "Successfuly dowloaded !");
        }
    }

    public JSONArray getPhotosFromFile(){
        try{
            InputStream is = new FileInputStream(getCacheDir() + "/" + "photos.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        } catch(IOException e){
            e.printStackTrace();
            return new JSONArray();
        } catch(JSONException e){
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            String CHANNEL_ID = "thibaut.photogran.ONE";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotification() {
        String CHANNEL_ID = "thibaut.photogran.ONE";

        final NotificationManager mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final Intent launchNotificationIntent = new Intent(this, SecondeActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, launchNotificationIntent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_desc))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        mNotification.notify(01, mBuilder.build());
    }

    class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoHolder> {

        private JSONArray photos;

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.rv_photo_element, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            try {
                String url = photos.getJSONObject(position).getJSONObject("urls").getString("regular");
                holder.display(url);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return photos.length();
        }

        public class PhotoHolder extends RecyclerView.ViewHolder {

            public ImageView iv;

            public PhotoHolder(View view) {

                super(view);
                iv = view.findViewById(R.id.iv_photo);

            }

            public void display(String url) {
                Picasso.with(getBaseContext()).load(url).into(iv);
            }
        }

        public PhotosAdapter(JSONArray photos) {
            this.photos = photos;
        }

        public void setNewPhoto(JSONArray photos) {
            this.photos = photos;
            notifyDataSetChanged();
        }
    }
}
