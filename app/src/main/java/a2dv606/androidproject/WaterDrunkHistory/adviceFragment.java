package a2dv606.androidproject.WaterDrunkHistory;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import a2dv606.androidproject.R;


public class adviceFragment extends Fragment {

    final static String Review="review";
    final static String image="image";
    private ViewGroup rootView;





    public static adviceFragment create(int review, int img) {

        adviceFragment newFragment = new adviceFragment();
        Bundle args = new Bundle();
        args.putInt(image, img);
        args.putInt(Review, review);


        newFragment.setArguments(args);
        return newFragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = (ViewGroup) inflater.inflate(R.layout.activity_fragment, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();


        int bReview=0,bImage=0 ;
        Bundle args = getArguments();
        if (args != null) {

            bReview = args.getInt(Review);
            bImage = args.getInt(image);

        }

        ((TextView) rootView.findViewById(R.id.review_view)).setText(bReview);
        ((ImageView) rootView.findViewById(R.id.image_view)).setImageResource(bImage);

    }


}

