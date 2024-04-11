package com.example.homemadefood.CustomerPage.BottomSheetDialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.homemadefood.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DeliveryFeeBottomSheetFragment extends BottomSheetDialogFragment {

    private TextView[] intervals;
    private DeliveryFeeListener listener;
    private SeekBar seekBar;
    private static final String PREFS_NAME = "DeliveryPrefs";
    private static final String INTERVAL_KEY = "selectedInterval";

    public void setListener(DeliveryFeeListener listener) {
        this.listener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.delivery_fees_bottom_sheet_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Drawable customTickMark = ContextCompat.getDrawable(requireContext(), R.drawable.custom_seekbar_tick_mark);

        intervals = new TextView[]{
                view.findViewById(R.id.interval1),
                view.findViewById(R.id.interval2),
                view.findViewById(R.id.interval3),
                view.findViewById(R.id.interval4)
        };

        seekBar = view.findViewById(R.id.seekBarDeliveryFee);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for (int i = 0; i < intervals.length; i++) {
                    if (i <= progress) {
                        intervals[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                    } else {
                        intervals[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.default_grey));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedInterval = sharedPreferences.getInt(INTERVAL_KEY, 0);
        seekBar.setProgress(savedInterval);

        Button viewResults = view.findViewById(R.id.viewResults);
        viewResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && seekBar != null) {
                    int selectedInterval = seekBar.getProgress();
                    listener.onDeliveryFeeSelected(selectedInterval);
                }
                dismiss();
            }
        });

        Button resetButton = view.findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onResetClicked(); // Notify listener about reset action
                }
                dismiss();
            }
        });
    }
}
