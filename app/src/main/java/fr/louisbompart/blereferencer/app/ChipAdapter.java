package fr.louisbompart.blereferencer.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by louis on 29/01/2016.
 */
public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ViewHolder> {
    private List<Chip> mDataset;

    public boolean add(Chip chip) {
        return mDataset.add(chip);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mSerialNumber;
        public TextView mMinor;
        public TextView mMajor;
        public ViewHolder(View v) {
            super(v);
            mSerialNumber = (TextView) v.findViewById(R.id.chipSerialNumber);
            mMajor = (TextView) v.findViewById(R.id.chipMajor);
            mMinor = (TextView) v.findViewById(R.id.chipMinor);
        }
    }

    public ChipAdapter(List<Chip> chips) {
        mDataset=chips;
    }

    @Override
    public ChipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_chip, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ChipAdapter.ViewHolder viewHolder, int i) {
        viewHolder.mSerialNumber.setText(mDataset.get(i).getSerialNumber());
        viewHolder.mMajor.setText(mDataset.get(i).getMajor());
        viewHolder.mMinor.setText(mDataset.get(i).getMinor());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
