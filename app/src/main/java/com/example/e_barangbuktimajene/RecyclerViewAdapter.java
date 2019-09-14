package com.example.e_barangbuktimajene;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import static com.example.e_barangbuktimajene.DetailActivity.EXTRA_IMAGE;
import static com.example.e_barangbuktimajene.DetailActivity.EXTRA_IMAGEID;
import static com.example.e_barangbuktimajene.DetailActivity.EXTRA_INFO;
import static com.example.e_barangbuktimajene.DetailActivity.EXTRA_NAME;
import static com.example.e_barangbuktimajene.DetailActivity.EXTRA_REG;
import static com.example.e_barangbuktimajene.DetailActivity.EXTRA_STATUS;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    List<ImageUploadInfo> MainImageUploadInfoList;

    public RecyclerViewAdapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);

        holder.imageNameTextView.setText(UploadInfo.getImageName());
        holder.registrationNameTextView.setText(UploadInfo.getRegistrationName());
        holder.informationNameTextView.setText(UploadInfo.getInformationName());
        holder.statusNameTextView.setText(UploadInfo.getStatusName());

        //Loading image from Glide library.
        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUploadInfo clickedDataItem = MainImageUploadInfoList.get(holder.getAdapterPosition());
                Toast.makeText(context, clickedDataItem.getImageName(), Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(EXTRA_NAME, clickedDataItem.getImageName());
                intent.putExtra(EXTRA_REG, clickedDataItem.getRegistrationName());
                intent.putExtra(EXTRA_INFO, clickedDataItem.getInformationName());
                intent.putExtra(EXTRA_STATUS, clickedDataItem.getStatusName());
                intent.putExtra(EXTRA_IMAGE, clickedDataItem.getImageURL());
                intent.putExtra(EXTRA_IMAGEID, clickedDataItem.getImageID());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView imageNameTextView;
        public TextView registrationNameTextView;
        public TextView informationNameTextView;
        public TextView statusNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_photo);

            imageNameTextView = itemView.findViewById(R.id.tv_item_name);
            registrationNameTextView = itemView.findViewById(R.id.tv_item_Registration);
            informationNameTextView = itemView.findViewById(R.id.tv_item_info);
            statusNameTextView = itemView.findViewById(R.id.tv_item_status);
        }
    }
}