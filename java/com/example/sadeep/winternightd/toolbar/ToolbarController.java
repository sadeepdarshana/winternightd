package com.example.sadeep.winternightd.toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.buttons.customizedbuttons.ToolbarButton;
import com.example.sadeep.winternightd.selection.XSelection;
import com.example.sadeep.winternightd.spans.LiveFormattingStatus;
import com.example.sadeep.winternightd.spans.SpansController;
import com.example.sadeep.winternightd.spans.SpansFactory;

/**
 * Created by Sadeep on 10/12/2016.
 */
final public class ToolbarController {

    private ToolbarController(){}//ToolbarController cannot be instantiated



    /**boolean[] BIUH----
     * the status (on or off) of the 4 toolbar buttons bold, italic, underline and highlight.
     * (the order has been purposely made to coincide with the relevant span type [@see SpansFactory])
     * @see com.example.sadeep.winternightd.spans.SpansFactory */

    private static ToolbarButton[] toolbarButtons = new ToolbarButton[10];

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


        LinearLayout tbarLL = (LinearLayout) tbarHSV.getChildAt(0);

        /**
         * We store the button's id in its tag property at the XML code. (toolbar.xml)
         * Below loop,
         *      1. We add the buttons to the array toolbarButtons so that toolbarButtons[x] = button with the button id x. (it comes useful later because we can easily get the button by buttonId)
         *      2. Set OnClick event of each button (which is it calls toolbarButtonClicked(x) passing buttonId as x.)
         */
        for (int c = 0; c < tbarLL.getChildCount(); c++)
        {
            ToolbarButton btn=null; try{ btn = (ToolbarButton)tbarLL.getChildAt(c);}catch (Exception e){} // hope java will soon get C#'s 'as'

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

        updateStatus(LiveFormattingStatus.format);
    }

    private static void toolbarButtonClicked(int buttonId) {

        ToolbarButton btn = toolbarButtons[buttonId];

        if(XSelection.isSelectionAvailable()){
            if (buttonId < 3) {
                SpansController.formatRegion(XSelection.getSelectedNote(),XSelection.getSelectionStart(),XSelection.getSelectionEnd(),buttonId,LiveFormattingStatus.format[buttonId]*-1);
            }
        }

        if (buttonId < 3) {
            btn.setMode((btn.getMode() + 1) % 2);
            if (LiveFormattingStatus.format[buttonId] == 1)
                LiveFormattingStatus.format[buttonId] = -1;
            else if (LiveFormattingStatus.format[buttonId] == -1)
                LiveFormattingStatus.format[buttonId] = 1;
        }

    }


    public static void updateStatus(int[] spanStatus) {
        if(toolbarButtons[0]==null)return;
        for(int i =0;i< SpansFactory.NO_OF_ORDINARY_SPAN_TYPES;i++) {


            if(i<3)   toolbarButtons[i].setMode((spanStatus[i]+1)/2);
        }
    }
}
