package com.example.minibus.ui.report;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minibus.R;
import com.example.minibus.Report;

import java.text.DateFormat;
import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.MyViewHolder> {

    private List<Report> reportsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView baslangic_adres, baslangic_tarih, bitis_adres, bitis_tarih;

        public MyViewHolder(View view) {
            super(view);
            baslangic_adres =  view.findViewById(R.id.baslangic_adres);
            baslangic_tarih =  view.findViewById(R.id.baslangic_tarih);
            bitis_adres =  view.findViewById(R.id.bitis_adres);
            bitis_tarih=view.findViewById(R.id.bitis_tarih);
        }
    }

    public ReportsAdapter(List<Report> reportsList) {
        this.reportsList = reportsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Report report = reportsList.get(position);
        holder.baslangic_adres.setText(report.getStartAddress());
        holder.baslangic_tarih.setText(DateFormat.getDateTimeInstance().format(report.getStartDate()));
        holder.bitis_adres.setText(report.getFinishAddress());
        holder.bitis_tarih.setText(DateFormat.getDateTimeInstance().format(report.getFinishDate()));
    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }
}
