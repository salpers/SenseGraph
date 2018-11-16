package com.example.android.sensegraph;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecViewHolder> {

    private static final String TAG = RecAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;
    private static int viewHolderCount;
    /*private DataPoint[][] dpArray;*/
    private int mNumberItems;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RecAdapter(int numberOfItems, ListItemClickListener listener) {
      /*  this.dpArray = dpArray;*/
        mNumberItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;


    }

    @Override
    public RecViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recording_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        RecViewHolder viewHolder = new RecViewHolder(view);
        viewHolder.listItemRecDetView.setText("ViewHolder index: " + viewHolderCount);
        //SET WITH DATA FROM SQL
        //viewHolder.recGraph.addSeries(new LineGraphSeries());
        viewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + viewHolderCount);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind("" + position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class RecViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemRecDetView;
        GraphView recGraph;

        public RecViewHolder(View itemView) {
            super(itemView);
            listItemRecDetView = (TextView) itemView.findViewById(R.id.tv_item_number);
            recGraph = (GraphView) itemView.findViewById(R.id.tv_graphView);

            //listItemRecDetView.setText("TEST");
            LineGraphSeries lgs = new LineGraphSeries();

            lgs.resetData(new DataPoint[]{new DataPoint(0, 0), new DataPoint(1, 0), new DataPoint(3, 4), new DataPoint(4, 5)});
            recGraph.addSeries(lgs);
            itemView.setOnClickListener(this);
        }

        void bind(String recNr) {
            listItemRecDetView.setText(recNr);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }

}
