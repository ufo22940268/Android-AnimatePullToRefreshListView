package bettycc.com.animatepulltorefresh;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


public class MyActivity extends Activity {

    public static final int TYPE_EATING = 0;
    public static final int TYPE_SQUIRRIEL = 1;

    private PullToRefreshListView mRefreshListView;
    private ListView mListView;
    private String[] mDemoStrs = new String[]{
            "妈妈",
            "再也",
            "不用",
            "担心我的列",
            "表没有",
            "动画",
            "了",
    };
    private int mType;
    private ResourceChooser mRc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getIntent().getIntExtra("type", TYPE_EATING);
        mRc = new ResourceChooser(this, mType);
        setContentView(mRc.getMainLayout());
        mRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
        mListView = mRefreshListView.getRefreshableView();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, mRc.getItemLayout(), mDemoStrs);
        mListView.setAdapter(adapter);
        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mRefreshListView.onRefreshComplete();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class ResourceChooser {
        private final Resources mRes;
        private Context mContext;
        private int mType;

        public ResourceChooser(Context context, int type) {
            mContext = context;
            mType = type;
            mRes = mContext.getResources();
        }

        private int getMainLayout() {
            int layout;
            switch (mType) {
                case TYPE_EATING:
                default:
                    layout = R.layout.activity_eating;
                    break;

                case TYPE_SQUIRRIEL:
                    layout = R.layout.activity_squirrel;
                    break;
            }
            return layout;
        }

        private int getItemLayout() {
            int layout;
            switch (mType) {
                case TYPE_EATING:
                default:
                    layout = R.layout.eating_item;
                    break;

                case TYPE_SQUIRRIEL:
                    layout = R.layout.squirrel_item;
                    break;
            }
            return layout;
        }
    }
}
