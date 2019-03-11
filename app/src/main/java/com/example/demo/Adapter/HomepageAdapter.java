package com.example.demo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo.Database.ChatDBHelper;
import com.example.demo.Database.ChatDBUtility;
import com.example.demo.Model.DataRecords;
import com.example.demo.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class HomepageAdapter extends  RecyclerView.Adapter<HomepageAdapter.MyViewHolder> {

    private Context context;
    LayoutInflater vi;
    ArrayList<DataRecords> dataResponse;
    ArrayList<DataRecords> dataResponseConstant;

    private FriendFilter friendFilter;

    ChatDBHelper chatDBHelper;
    ChatDBUtility chatDBUtility;

    public HomepageAdapter(Context context, ArrayList<DataRecords> dataResponse) {
        this.dataResponse = dataResponse;
        this.context = context;
        this.dataResponseConstant=dataResponse;

        vi = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        chatDBUtility = new ChatDBUtility();
        chatDBHelper = chatDBUtility.CreateChatDB(context);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView delete,email,age_et,phone,name_et;
        ImageView imageView;



        public MyViewHolder(View itemView) {
            super(itemView);

            this.delete=(TextView) itemView.findViewById(R.id.delete);
            this.email=(TextView) itemView.findViewById(R.id.email);
            this.phone=(TextView) itemView.findViewById(R.id.phone);
            this.age_et=(TextView) itemView.findViewById(R.id.age_et);
            this.name_et=(TextView) itemView.findViewById(R.id.name_et);
            this.imageView=(ImageView)itemView.findViewById(R.id.image);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {


        View    view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_homepage, parent, false);





        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public int getItemCount() {
        return dataResponse.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatDBUtility.DeleteFromDataAccTOID(chatDBHelper,dataResponse.get(position).getId());
                dataResponse.remove(position);
                HomepageAdapter.this.notifyDataSetChanged();


            }
        });

        holder.setIsRecyclable(false);


        try {

            if(dataResponse.get(position).getEmail()==null||dataResponse.get(position).getEmail().equals(""))
            {
                holder.email.setHint("Email is not Present");
            }else
            {
                holder.email.setText(dataResponse.get(position).getEmail());
            }
            if(dataResponse.get(position).getAge()==0)
            {
                holder.age_et.setHint("Age is Not Present");
            }else
            {
                holder.age_et.setText(String.valueOf(dataResponse.get(position).getAge()));
            }

           if(dataResponse.get(position).getName()==null||dataResponse.get(position).getName().equals(""))
           {
               holder.name_et.setHint("Name is not Present");
           }else
           {
               holder.name_et.setText(dataResponse.get(position).getName());
           }

           if(dataResponse.get(position).getPhone_number()==0)
           {
               holder.phone.setHint("Phone number is not present");
           }else
           {
               holder.phone.setText(String.valueOf(dataResponse.get(position).getPhone_number()));
           }



            if(dataResponse.get(position).getImage_url()!=null)
            {
                Picasso.get().load(new File(dataResponse.get(position).getImage_url())).error(context.getResources().getDrawable(R.drawable.ic_error_outline_black_24dp)).into(holder.imageView);

            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }





    }


    public Filter getFilter() {

        friendFilter = null;

        if (friendFilter == null) {

            this.dataResponse = dataResponseConstant;


            friendFilter = new FriendFilter();
        }

        return friendFilter;
    }


    private class FriendFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<DataRecords> tempList = new ArrayList<DataRecords>();


                // search content in friend list
                for (DataRecords users : dataResponse) {
                    if (users.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(users);
                    }

                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;

            } else {
                filterResults.count = dataResponse.size();
                filterResults.values = dataResponse;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataResponse = (ArrayList<DataRecords>) results.values;
            if(dataResponse != null) {

                notifyDataSetChanged();
            }else {


            }

        }
    }




}
