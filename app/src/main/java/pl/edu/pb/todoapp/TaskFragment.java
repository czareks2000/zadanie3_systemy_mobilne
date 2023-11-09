package pl.edu.pb.todoapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TaskFragment extends Fragment {

    private Task task;
    private EditText nameField, dateField;
    private Spinner categorySpinner;
    private CheckBox doneCheckBox;
    private final Calendar calendar = Calendar.getInstance();
    public static final String ARG_TASK_ID = "task_id";

    public TaskFragment() {}

    public static TaskFragment newInstance(UUID taskId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TASK_ID, taskId);
        TaskFragment taskFragment = new TaskFragment();
        taskFragment.setArguments(bundle);
        return taskFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID taskId = (UUID)getArguments().getSerializable(ARG_TASK_ID);
        task = TaskStorage.getInstance().getTask(taskId);
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        nameField = view.findViewById(R.id.task_name);
        nameField.setText(task.getName());
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                task.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        dateField = view.findViewById(R.id.task_date);
        DatePickerDialog.OnDateSetListener date = (view12, year, month , day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            setupDateFieldValue(calendar.getTime());
            task.setDate(calendar.getTime());
        };
        dateField.setOnClickListener(view1 ->
                new DatePickerDialog(getContext(), date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show());
        setupDateFieldValue(task.getDate());

        doneCheckBox = view.findViewById(R.id.task_done);
        doneCheckBox.setChecked(task.isDone());
        doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);
            if (task.isDone()) {
                nameField.setPaintFlags(nameField.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                nameField.setPaintFlags(nameField.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });

        if (task.isDone()) {
            nameField.setPaintFlags(nameField.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            nameField.setPaintFlags(nameField.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        categorySpinner = view.findViewById(R.id.category_select);
        categorySpinner.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_expandable_list_item_1, Category.values()));
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> partent, View view, int position, long id) {
                task.setCategory(Category.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categorySpinner.setSelection(task.getCategory().ordinal());


        return view;
    }

    private void setupDateFieldValue(Date date) {
        Locale locale = new Locale("pl", "PL");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy", locale);
        dateField.setText(dateFormat.format(date));
    }
}
