package com.example.teacher_panel_application.History.StudyMaterial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacher_panel_application.EditDataFragments.EditDataFragment;
import com.example.teacher_panel_application.Models.PDFModel;
import com.example.teacher_panel_application.R;
import com.google.gson.Gson;
import com.rajat.pdfviewer.PdfViewerActivity;
import com.rajat.pdfviewer.util.saveTo;

import java.util.HashMap;
import java.util.List;

public class ViewStudyHistoryAdapter extends RecyclerView.Adapter<ViewStudyHistoryAdapter.ViewHolder> {
    private Context context;
    private List<PDFModel> pdfModelList;

    public ViewStudyHistoryAdapter(Context context, List<PDFModel> pdfModelList) {
        this.context = context;
        this.pdfModelList = pdfModelList;
    }

    @NonNull
    @Override
    public ViewStudyHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull ViewStudyHistoryAdapter.ViewHolder holder, int position) {
        PDFModel model = pdfModelList.get(position);
        holder.dateTime.setText("Date: "+model.getDateTime());
        holder.purpose.setText("Purpose: "+model.getPurpose());
        holder.pdfName.setText(model.getPDFName());
        holder.yearSemesTxt.setText("Group: "+model.getYear()+" ("+model.getSemester()+")");

        if (model.getPurpose().equals("Assignment")){
            //holder.pdfBg.setBackground(context.getResources().getDrawable(R.drawable.assignment_bg));
            holder.pdfBg.setBackground(ContextCompat.getDrawable(context,R.drawable.assignment_bg));
        }
//        else {
//            holder.purpose.setTextColor(R.color.green);
//        }

        holder.itemView.setOnClickListener(v -> {
            if (model.getPurpose().equals("Assignment")){
                SharedPreferences sharedPreferences = context.getSharedPreferences("loginType", Context.MODE_PRIVATE);
                boolean isTrue = sharedPreferences.getBoolean("typeBool",false);
                if (isTrue){
                    SubmitePDF_Fragment fragment = new SubmitePDF_Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("PDFDataFromTeacher",  new Gson().toJson(model));
                    fragment.setArguments(bundle);
                    fragment.show(((AppCompatActivity) context).getSupportFragmentManager(),fragment.getTag());
                }else {
                    openFragment(model);
                }

            }else {
                launchPDf(model.getPDFUrl(),model.getPDFName());
            }

        });

    }
    private void openFragment(PDFModel model) {

        Received_PDF_From_Student_Fragment editDataFragment = new Received_PDF_From_Student_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("PDFData",  new Gson().toJson(model));
        editDataFragment.setArguments(bundle);
        editDataFragment.show(((AppCompatActivity) context).getSupportFragmentManager(),editDataFragment.getTag());

    }
    private void launchPDf(String url,String name){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("empty","empty");
        context.startActivity(PdfViewerActivity.Companion.launchPdfFromUrl(context,url,name, saveTo.ASK_EVERYTIME,true,hashMap));
    }
    @Override
    public int getItemCount() {
        return pdfModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pdfName,dateTime,purpose,yearSemesTxt;
        private LinearLayout pdfBg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfName = itemView.findViewById(R.id.pdfFileName);
            yearSemesTxt = itemView.findViewById(R.id.yearSemesterTxt);
            purpose = itemView.findViewById(R.id.pdfUploadedPur);
            dateTime = itemView.findViewById(R.id.pdfUploadDate);
            pdfBg = itemView.findViewById(R.id.pdfBg);
        }
    }
}
