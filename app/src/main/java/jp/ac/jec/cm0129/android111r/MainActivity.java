package jp.ac.jec.cm0129.android111r;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edt;
    private int startYear = 2020;
    private int starthMonth, startDay = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.btnSel);
        btn.setOnClickListener(this);

        Button okBtn = (Button) findViewById(R.id.btnSubmit);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                edt = (EditText) findViewById(R.id.editText);
                String date = edt.getText().toString();
                System.out.println("Edit Text -------->" + date);
                if (date.equals("")) {
                    Toast.makeText(MainActivity.this, "日付文字列を正しく入力してください！ Ex: 2023/01/20", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar today = Calendar.getInstance();

                    String strA[] = date.split("/");
                    String year = strA[0];
                    if(strA.length == 3){
                        if(year.equals(String.valueOf(today.get(Calendar.YEAR)))) {
                            Toast.makeText(MainActivity.this, "日付文字列を正しく入力してください！ Ex: "+(today.get(Calendar.YEAR) - 1), Toast.LENGTH_SHORT).show();
                        } else {
                            intent.putExtra("inputYear", year);
                            intent.putExtra("dob", date);

                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "日付文字列を正しく入力してください！ Ex: 2023/01/20", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sp = getSharedPreferences("android11R",MODE_PRIVATE);
        String data = sp.getString("birth","");
        TextView txtHistory = findViewById(R.id.dataHistory);
        System.out.println("onresume"+data);
        txtHistory.setText(data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        EditText edtText = (EditText) findViewById(R.id.editText);
        final String edtStr = edtText.getText().toString(); // 2023/01/20
        if (!edtStr.equals("")) {
            // EditTextに日付文字列がある場合
            try {
                final String[] splitStr = edtStr.split("/"); // ["2023","01","20"]
                final int year = Integer.parseInt(splitStr[0]);
                final int month = Integer.parseInt(splitStr[1]);
                final int date = Integer.parseInt(splitStr[2]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month - 1, date);
                DatePickerDialog dialog = new DatePickerDialog(
                        this,
                        new DialogDataSetEvent(),
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dialog.show();
            } catch (Exception ex) {
                Toast.makeText(this, "日付文字列を正しく入力してください！ Ex: 2023/01/20", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 未入力の場合
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    new DialogDataSetEvent(),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        }
    }


    class DialogDataSetEvent implements DatePickerDialog.OnDateSetListener {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            EditText editText = (EditText) findViewById(R.id.editText);
            //editText.setText(year+ "/" +(month+1) +"/"+dayOfMonth);

            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new
                    SimpleDateFormat("yyyy/MM/dd");
            String currentDateString = dateFormat.format(calendar.getTime());

            System.out.println("editText-->" + currentDateString);
            editText.setText(currentDateString);

            System.out.println("year" + year + "---> month--->" + month + "---->day---->" + dayOfMonth);
        }
    }


}