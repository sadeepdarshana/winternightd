package com.example.sadeep.winternightd.textboxes;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sadeep.winternightd.field.fields.Field;
import com.example.sadeep.winternightd.misc.Globals;

/**
 * Created by Sadeep on 10/13/2016.
 */
public class XTextView extends TextView{

    private Field boundField;

    public XTextView(Field boundField,Context context){
        this(context);
        this.boundField = boundField;
    }
    private XTextView(Context context) {
        super(context);
        init();
    }
    private void init() {

        setPadding(0,0,0,3*Globals.dp2px);
        setBackgroundColor(Color.TRANSPARENT);
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setTextSize(TypedValue.COMPLEX_UNIT_FRACTION,  Globals.defaultFontSize);
        setTextColor(Color.BLACK);

    }

}
