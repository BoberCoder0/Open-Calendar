package com.example.opencalendar.Event;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.opencalendar.R;

public class EventDetailsFragment extends Fragment {

    private static final String ARG_EVENT = "event";

    public static EventDetailsFragment newInstance(Event event) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        if (getArguments() != null) {
            Event event = getArguments().getParcelable(ARG_EVENT);

            TextView tvTitle = view.findViewById(R.id.tvTitle);
            TextView tvTime = view.findViewById(R.id.tvTime);
            TextView tvDescription = view.findViewById(R.id.tvDescription);

            tvTitle.setText(event.getTitle());
            tvTime.setText(event.getFormattedTime());
            tvDescription.setText(event.getDescription());
        }

        return view;
    }
}