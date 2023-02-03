package jp.ac.jec.cm0129.android111r;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    private int dobString;
    private String date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        String year = intent.getStringExtra("inputYear");
        String dob = intent.getStringExtra("dob");
        String m = dob.split("/")[1];
        String d = dob.split("/")[2];
        dobString = getPerfectAgeInYears(Integer.valueOf(year),Integer.valueOf(m),Integer.valueOf(d));

        TextView b = (TextView)findViewById(R.id.age);
        b.setText("あなたはの年齢は "+String.valueOf(dobString)+" 歳です。");

        String dd = dob.replace("/", "-");
        String newDD = dd;
        System.out.println("jpage--->"+newDD);

        date = newDD;

        LocalDate localDate = LocalDate.parse(date);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd").withChronology(IsoChronology.INSTANCE)
                .withLocale(Locale.ENGLISH);
        DateTimeFormatter japanDateFormat = DateTimeFormatter.ofPattern("yyyy年MM月dd日")
                .withChronology(IsoChronology.INSTANCE)
                .withLocale(Locale.JAPAN);

        System.out.println("jpage--->"+localDate.format(dateFormat));
        System.out.println("jpage--->"+localDate.format(japanDateFormat));

        //String dobJP = intent.getStringExtra("dobForJP");
        TextView txt = (TextView) findViewById(R.id.txtMyHistory);
        TextView a = findViewById(R.id.ageForNormal);

        SharedPreferences sp = getSharedPreferences("android11R",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("birth",localDate.format(japanDateFormat));
        editor.apply();
        System.out.println("onresume"+localDate.format(japanDateFormat));

        Toast.makeText(ResultActivity.this,"保存しました",Toast.LENGTH_SHORT).show();

        a.setText("あなたは"+localDate.format(japanDateFormat)+"生まれです");
        DateTimeFormatter japaneseEraDtf = DateTimeFormatter.ofPattern("GGGGy年MM月dd日")
                .withChronology(JapaneseChronology.INSTANCE)
                .withLocale(Locale.JAPAN);

        System.out.println("jpage--->"+parseDateToddMMyyyy(dob));

        LocalDate gregorianDate = LocalDate.parse(date);
        JapaneseDate japaneseDate = JapaneseDate.from(gregorianDate);
        System.out.println(japaneseDate.format(japaneseEraDtf));

        txt.setText("あなたは"+japaneseDate.format(japaneseEraDtf)+"生まれです");


        TextView history = findViewById(R.id.history);
        int syo = (Integer.valueOf(year)+13);
        history.setText(syo+"年"+"3月"+"      小学校卒業"+"\n");

        int chy = (syo+3);
        history.append(chy+"年"+"3月"+"      中学校卒業"+"\n");
        history.append(chy+"年"+"4月"+"      高校入学"+"\n");
        int kou = chy+3;
        history.append(chy+"年"+"3月"+"      高校卒業"+"\n");
        history.append(chy+"年"+"4月"+"      専門学校入学"+"\n");

        int sen = kou+3;
        history.append(chy+"年"+"3月"+"      専門学校卒業");

        Button btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static int getPerfectAgeInYears(int year, int month, int date) {

        Calendar dobCalendar = Calendar.getInstance();

        dobCalendar.set(Calendar.YEAR, year);
        dobCalendar.set(Calendar.MONTH, month);
        dobCalendar.set(Calendar.DATE, date);

        int ageInteger = 0;

        Calendar today = Calendar.getInstance();

        ageInteger = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);

        if (today.get(Calendar.MONTH) == dobCalendar.get(Calendar.MONTH)) {

            if (today.get(Calendar.DAY_OF_MONTH) < dobCalendar.get(Calendar.DAY_OF_MONTH)) {

                ageInteger = ageInteger - 1;
            }

        } else if (today.get(Calendar.MONTH) < dobCalendar.get(Calendar.MONTH)) {

            ageInteger = ageInteger - 1;

        }

        return ageInteger;
    }
}