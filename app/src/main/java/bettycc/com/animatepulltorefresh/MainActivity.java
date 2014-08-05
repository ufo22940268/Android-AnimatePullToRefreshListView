package bettycc.com.animatepulltorefresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by ccheng on 8/4/14.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onEating(View view) {
        launchItem(MyActivity.TYPE_EATING);
    }

    public void onSqurriel(View view) {
        launchItem(MyActivity.TYPE_SQUIRRIEL);
    }

    private void launchItem(int type) {
        Intent intent = new Intent(this, MyActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}
