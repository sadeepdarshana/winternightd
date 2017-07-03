package com.example.sadeep.winternightd.spans;

import android.support.v7.util.SortedList;
import android.text.Spannable;
import android.text.Spanned;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.sadeep.winternightd.spans.SpanWrapper.*;
import static com.example.sadeep.winternightd.spans.SpansFactory.*;

/**
 * Created by Sadeep on 6/10/2017.
 */
public class SpansController {
    private SpansController() {}

    public static void updateSpansOnRegion(Spannable text, int start, int end){
        Comparator<SpanWrapper> comparator = new CompareSpanWrapperByStart();
        RecognizedSpan[] spans = text.getSpans(start-1, end+1, RecognizedSpan.class);

        ArrayList<ArrayList<SpanWrapper>>spansCategorys = new ArrayList<ArrayList<SpanWrapper>>(); //ArrayList(span categories) of ArrayLists(SpanWrappers)
        for(int c = 0; c<SpansFactory.NO_OF_ORDINARY_SPAN_TYPES; c++)spansCategorys.add(new ArrayList<SpanWrapper>());
        //spansCategory[3] is the ArrayList which will hold the SpanWrappers whose [span's] span type(see SpansFactory) is 3

        for (SpansFactory.RecognizedSpan span:spans) {
            if(span.getSpanType()==SpansFactory.XSelectionSpan.spanType)continue; //ignore XSelectionSpans as we don't consider them here.

            int spanStart = text.getSpanStart(span),    spanEnd = text.getSpanEnd(span),    spanType = span.getSpanType();

            SpanWrapper wrapper = new SpanWrapper(span,spanStart,spanEnd);
            spansCategorys.get(spanType) .add(wrapper); //put each SpanWrapper into the relevant category ArrayList
        }

        for(ArrayList<SpanWrapper> spanCategory:spansCategorys) Collections.sort(spanCategory,comparator);

        for (int i = 0; i<SpansFactory.NO_OF_ORDINARY_SPAN_TYPES; i++){

            if(i==XBoldSpan.spanType||i==XItalicSpan.spanType||i==XUnderlineSpan.spanType){

                if(LiveFormattingStatus.format[i]==LiveFormattingStatus.ON)
                {
                    SpanWrapper.eraseSpanWrappersFromRegion(spansCategorys.get(i),text,start,end);

                    SpanWrapper newSpanWrapper = new SpanWrapper(SpansFactory.createSpan(i),start,end);
                    text.setSpan(newSpanWrapper.span,newSpanWrapper.start,newSpanWrapper.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spansCategorys.get(i).add(newSpanWrapper);
                }
                else if(LiveFormattingStatus.format[i]==LiveFormattingStatus.OFF)
                {
                    SpanWrapper.eraseSpanWrappersFromRegion(spansCategorys.get(i),text,start,end);
                }
                Collections.sort(spansCategorys.get(i),comparator);
                SpanWrapper.combinePossibleSpanWrappers(spansCategorys.get(i),text);
            }
        }
    }

    public static void updateToolbarForCurrentPosition(Spannable text, int position){
        Object[] spans =  text.getSpans(Math.max(position - 1, 0), Math.min(position + 1, text.length()),RecognizedSpan.class );

        int spanStatus[] = new int[]{-1,-1,-1};
        for(Object span:spans)
        {
            SpansFactory.RecognizedSpan rspan = (RecognizedSpan) span;
            if( rspan.getSpanType()== XBoldSpan.spanType ||
                rspan.getSpanType()== XItalicSpan.spanType ||
                rspan.getSpanType()== XUnderlineSpan.spanType )spanStatus[rspan.getSpanType()]=1;
        }
        LiveFormattingStatus.update(spanStatus);
    }
}
