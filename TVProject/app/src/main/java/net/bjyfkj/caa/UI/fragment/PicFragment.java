package net.bjyfkj.caa.UI.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bjyfkj.caa.R;

/**
 * Created by YFKJ-1 on 2016/11/6.
 */
public class PicFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.inflate(R.layout.picfragment, container, false);
        return view;
    }
}
