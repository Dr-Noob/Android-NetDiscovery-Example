package drnoob.discovery;

import android.net.nsd.NsdServiceInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private LinkedList<NsdServiceInfo> hosts;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textMain;
        public TextView textSub;
        public MyViewHolder(View v) {
            super(v);
            textMain = v.findViewById(R.id.text_main);
            textSub = v.findViewById(R.id.text_sub);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(LinkedList<NsdServiceInfo> h) {
        hosts = h;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.card, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textMain.setText(hosts.get(position).getHost().getHostAddress());
        holder.textSub.setText(String.valueOf(hosts.get(position).getPort()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return hosts.size();
    }
}