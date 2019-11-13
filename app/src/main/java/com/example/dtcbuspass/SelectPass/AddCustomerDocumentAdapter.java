package com.example.dtcbuspass.SelectPass;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.dtcbuspass.R;

import java.util.List;

public class AddCustomerDocumentAdapter  extends RecyclerView.Adapter<AddCustomerDocumentAdapter.ViewHolder>{



    private List<AddCustomerFDocumentData> imageDocumentList;

    private OnAdapterClickListener onAdapterClickListener;

    private Context context;

    AddCustomerDocumentAdapter(List<AddCustomerFDocumentData> imageDocumentList, Context context) {
        this.imageDocumentList = imageDocumentList;
        this.context = context;
    }

    @NonNull
    @Override
    public AddCustomerDocumentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.add_customer_document, parent, false);

        if (this.context instanceof OnAdapterClickListener) {
            this.onAdapterClickListener = (OnAdapterClickListener) this.context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAdapterClickListener");
        }
        return new AddCustomerDocumentAdapter.ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull AddCustomerDocumentAdapter.ViewHolder holder, int position) {
        final AddCustomerFDocumentData data = imageDocumentList.get(position);

        final ImagePopup imagePopup = new ImagePopup(context);
        imagePopup.setWindowHeight(800); // Optional
        imagePopup.setWindowWidth(800); // Optional
        //imagePopup.setBackgroundColor(context.getColor(R.color.offwhite_01));
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(true);  // Optional
        imagePopup.setImageOnClickClose(true);

        if (data.getPhotoUri() != null) {

            Glide.with(context)
                    .load(data.getPhotoUri())
                    .apply(new RequestOptions().centerCrop()
                            .fitCenter()
                            .placeholder(R.drawable.image_svg))
                    .into(holder.ivevent);

        } else {
            holder.ivevent.setImageDrawable(context.getDrawable(R.drawable.image_svg));
        }

        // Optional

        imagePopup.initiatePopupWithPicasso(data.getPhotoUri());
        holder.ivevent.setOnClickListener(view -> imagePopup.viewPopup());



        holder.ivcancel.setOnClickListener(view -> {
            if (data.getBtnstate().equals("1")) {
                onAdapterClickListener.onAdapterInteraction(data.getButtonId());
            } else if (data.getBtnstate().equals("3")) {
                onAdapterClickListener.onAdapterInteraction(holder.getAdapterPosition()+"z");
            }
        });
    }

    @Override
    public int getItemCount() {
        if (imageDocumentList != null) {
            return imageDocumentList.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivevent, ivcancel;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivevent = itemView.findViewById(R.id.iv_document);
            ivcancel = itemView.findViewById(R.id.iv_cancel);
        }
    }

    public interface OnAdapterClickListener {
        void onAdapterInteraction(String buttonId);
    }


}
