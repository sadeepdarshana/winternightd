package com.example.sadeep.winternightd.toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.buttons.MultiStatusButton;
import com.example.sadeep.winternightd.spans.LiveFormattingStatus;

/**
 * Created by Sadeep on 10/12/2016.
 */
final public class ToolbarController {

    private ToolbarController(){}//ToolbarController cannot be instantiated



    /**boolean[] BIUH----
     * the status (on or off) of the 4 toolbar buttons bold, italic, underline and highlight.
     * (the order has been purposely made to coincide with the relevant span type [@see SpansFactory])
     * @see com.example.sadeep.winternightd.spans.SpansFactory */
    public final static boolean[] BIUH =new boolean[] { false, false, false, false };

    private static Note boundNote;

    private static MultiStatusButton[] toolbarButtons = new MultiStatusButton[10];

    /**
     *  ButtonId    Button
     *
     *      0       bold
     *      1       italic
     *      2       underline
     *      3       highlight
     *      4       decrease indent
     *      5       increase indent
     *      ?!!     i've forgotten to add a btn 6
     *      7       numbered list
     *      8       bulleted list
     *      9       checked list
     */



    public static void initialize(ViewGroup tbarHSV) {

        BIUH[0]=false; BIUH[1]=false; BIUH[2]=false; BIUH[3]=false;
        boundNote = null;

        LinearLayout tbarLL = (LinearLayout) tbarHSV.getChildAt(0);

        /**
         * We store the button's id in its tag property at the XML code. (toolbar.xml)
         * Below loop,
         *      1. We add the buttons to the array toolbarButtons so that toolbarButtons[x] = button with the button id x. (it comes useful later because we can easily get the button by buttonId)
         *      2. Set OnClick event of each button (which is it calls toolbarButtonClicked(x) passing buttonId as x.)
         */
        for (int c = 0; c < tbarLL.getChildCount(); c++)
        {
            MultiStatusButton btn=null; try{ btn = (MultiStatusButton)tbarLL.getChildAt(c);}catch (Exception e){} // hope java will soon get C#'s 'as'

            if (btn != null)
            {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolbarButtonClicked(Integer.parseInt((String)v.getTag()));
                    }
                });
                int tag = Integer.parseInt((String)btn.getTag());// button ID number stored(@xml code) in tag
                toolbarButtons[tag] = btn;
            }
        }
    }

    private static void toolbarButtonClicked(int buttonId) {

        MultiStatusButton btn = toolbarButtons[buttonId];

        /**
         * ImageViewToolbarButton tags 0,1,2,3 - Bold, Italic, Underline, Highlight (can only be in Mode=0 or Mode=1)
         * For every new char typed XEditText checks (in _TextChanged event) ToolbarController.BIUH array to determine its work on spans.
         *
         * XEditText changes ToolbarController.BIUH (in its OnSelectionChanged method) when user changes the cursor position to suit the spans in the new position.
         * When user clicks on a BIUH button
         *    1. ToolbarController.ToolbarBtnClick method toggles BIUH[x]
         *    2. ToolbarController.ToolbarBtnClick method fires FormatSelectedText on the focused XEditText allowing selected text (if any) to change spans
         **/
        if (buttonId <= 3)
        {
            btn.setMode((btn.getMode()+1)%2);
            BIUH[buttonId] = (btn.getMode() == 1);  //the the relevant BIUH index coincides with the relevant buttonId
            //if(GlobalStaticStorage.FocusedXEditText!=null) GlobalStaticStorage.FocusedXEditText.FormatSelectedText(tag);
            if(LiveFormattingStatus.format[buttonId]==1)LiveFormattingStatus.format[buttonId]=-1;
            else if(LiveFormattingStatus.format[buttonId]==-1)LiveFormattingStatus.format[buttonId]=1;

        }
    }


}
