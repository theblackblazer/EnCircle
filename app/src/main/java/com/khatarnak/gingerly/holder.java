package com.khatarnak.gingerly;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class holder extends RecyclerView.ViewHolder{

public TextView txt;

    public holder(View itemView) {
        super(itemView);

        txt=(TextView)itemView.findViewById(R.id.emailtext);
    }
}
