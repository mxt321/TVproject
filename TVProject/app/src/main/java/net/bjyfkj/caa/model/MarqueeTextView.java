package net.bjyfkj.caa.model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by YFKJ-1 on 2016/11/11.
 */
public class MarqueeTextView extends TextView {

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}