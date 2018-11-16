package com.example.android.sensegraph;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecViewHolder> {

    private static final String TAG = RecAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;
    private static int viewHolderCount;
    private DataPoint[][][] dpArray;
    private int mNumberItems;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RecAdapter(DataPoint[][][] dataset, ListItemClickListener listener) {
        /*  this.dpArray = dpArray;*/
        dpArray = dataset;
        mNumberItems = dataset.length < 10 ? dataset.length : 10;
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
        //viewHolder.mGraph.addSeries(new LineGraphSeries());
        viewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + viewHolderCount);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecViewHolder holder, int position) {
        Log.d("ViewHolder Generation", "Position: "+position);
        for (DataPoint[] dp : dpArray[position]) {
            LineGraphSeries<DataPoint> lgs = new LineGraphSeries<>();
            Log.d("ViewHolder Creation", Arrays.toString(dpArray[position]));
            lgs.resetData(dp);
            holder.mGraph.addSeries(lgs);
        }
    }


    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class RecViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemRecDetView;
        GraphView mGraph;

        public RecViewHolder(View itemView) {
            super(itemView);
            listItemRecDetView = itemView.findViewById(R.id.tv_item_number);
            mGraph = itemView.findViewById(R.id.tv_graphView);
            setUpGraphview();
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

        private void setUpGraphview() {
            mGraph.getViewport().setXAxisBoundsManual(true);
            mGraph.getViewport().setMinX(0);
            mGraph.getViewport().setMaxX(30);

            mGraph.getViewport().setYAxisBoundsManual(true);
            mGraph.getViewport().setMinY(-1000);
            mGraph.getViewport().setMaxY(50000);
            mGraph.getViewport().setScrollable(true);
            mGraph.getViewport().setScalable(true);
            mGraph.getGridLabelRenderer().setNumHorizontalLabels(5);
        }

    }

}
