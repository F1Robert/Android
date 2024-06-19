package com.shsany.lampassistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 作者 zf
 * 日期 2024/6/19
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private List<SettingItem> settingItems;

    public SettingsAdapter(List<SettingItem> settingItems) {
        this.settingItems = settingItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SettingItem item = settingItems.get(position);
        holder.tvName.setText(item.getName());
        holder.etValue.setText(item.getValue());
        holder.btnUpdate.setOnClickListener(v -> {
            item.setValue(holder.tvName.getText().toString(), holder.etValue.getText().toString());
        });
    }

    @Override
    public int getItemCount() {
        return settingItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        EditText etValue;
        TextView btnUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            etValue = itemView.findViewById(R.id.etValue);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }
}
