package com.example.newsmreader.btsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newsmreader.AllstaticObj;
import com.example.newsmreader.R;
import com.example.newsmreader.S_P;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class StateBillNav extends BottomSheetDialogFragment {

     TextView txt_total,txt_online,txt_cash;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.botnav3, container, true);


        txt_cash=view.findViewById(R.id.txt_cash);
        txt_online=view.findViewById(R.id.txt_online);
        txt_total=view.findViewById(R.id.txt_total);

        txt_cash.setText(""+ S_P.R+AllstaticObj.cashamt);
        txt_online.setText(""+ S_P.R+AllstaticObj.onlineamt);
        AllstaticObj.total=AllstaticObj.onlineamt+AllstaticObj.cashamt;
        txt_total.setText(S_P.R+AllstaticObj.total);
        return view;
    }
}
