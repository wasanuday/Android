package a2dv606.androidproject.WaterDrunkHistory;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import a2dv606.androidproject.R;


public class OutlineActivity extends Activity {
    private   TextView pageMun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_outline);
        Button share = (Button) findViewById(R.id.shareBtn);

        share.setOnClickListener(new  shareWith());
        pageMun = (TextView) findViewById(R.id.pageNum);
        // Instantiate a ViewPager and a PagerAdapter.
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        final PagerAdapter pagerAdapter = new MyPagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                pageMun.setText(((position + 1) + "/" + pagerAdapter.getCount()) );

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ImageView cancel = (ImageView) findViewById(R.id.cancel_icon);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

    }
    private class  shareWith implements View.OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent msgIntent = new Intent(Intent.ACTION_SENDTO);
            msgIntent.setType("text/plain");
            msgIntent.setData(Uri.parse("smsto:"));
            msgIntent.putExtra(android.content.Intent.EXTRA_TEXT, "hallo");
            Intent intentChooser = Intent.createChooser(msgIntent,"Share with");
            startActivity(intentChooser);
        }}

    private class MyPagerAdapter extends FragmentPagerAdapter {
        Fragment[] fragments = new Fragment[4];

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);

            fragments[0] = adviceFragment.create(R.string.wake_up_review, R.drawable.wakeup);
            fragments[1] = adviceFragment.create( R.string.sleep_review, R.drawable.sleep);
            fragments[2] = adviceFragment.create( R.string.shower_review, R.drawable.shower);
            fragments[3] = adviceFragment.create( R.string.meal_review, R.drawable.eat);


        }


        @Override
        public Fragment getItem(int position) {

            return fragments[position];

        }

        @Override
        public int getCount() {
            return fragments.length;
        }




    }
}

