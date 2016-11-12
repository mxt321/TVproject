package net.bjyfkj.caa.model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by YFKJ-1 on 2016/11/11.
 */
public class AlwaysMarqueeTextView extends TextView {
    public AlwaysMarqueeTextView(Context context) {
        super(context);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
