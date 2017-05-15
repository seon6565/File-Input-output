package comwow2778.naver.blog.app14;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText memo;
    TextView tv1;
    ListView lv1;
    Button btnsave;
    ArrayList<String> name = new ArrayList<>();
    ArrayAdapter<String> adapter;
    DatePicker dp1;
    LinearLayout linear1, linear2;
    int count=0;
    int positions=0;
    String temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memo = (EditText) findViewById(R.id.et1);
        lv1 = (ListView) findViewById(R.id.listview);
        dp1 = (DatePicker) findViewById(R.id.datePicker);
        tv1 = (TextView) findViewById(R.id.tvCount);
        linear1 = (LinearLayout) findViewById(R.id.Lay1);
        linear2 = (LinearLayout) findViewById(R.id.Lay2);
        btnsave = (Button) findViewById(R.id.btnsave);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);
        lv1.setAdapter(adapter);
        File file = new File(getFilesDir().getPath() + "diary");
        file.mkdir();
        showFileList();
        listViewAction();
    }
    void showFileList() {
        String path = getFilesDir().getPath();
        int count = 0;
        name.clear();
        File[] files = new File(path + "diary").listFiles();
        for (File f : files) {
            name.add(f.getName().substring(0, 13));
            count++;
        }
        Comparator<String> compare = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        Collections.sort(name,compare);
        adapter.notifyDataSetChanged();
        tv1.setText("등록된 메모 개수: " + String.valueOf(count));
    }
    String nameFormat(Date d) {
        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd");
        String name = df.format(d) + ".memo";
        return name;
    }
    public void onClick(View v) {
        if (v.getId() == R.id.btn1) {
            linear1.setVisibility(View.INVISIBLE);
            linear2.setVisibility(View.VISIBLE);
            memo.setText("");
        }
        if (v.getId() == R.id.btnsave) {
            linear1.setVisibility(View.VISIBLE);
            linear2.setVisibility(View.INVISIBLE);
            Date date = new Date(dp1.getYear(), dp1.getMonth(), dp1.getDayOfMonth());
            final String filename = nameFormat(date);
            final String path = getFilesDir().getPath()+"";

            if (btnsave.getText().toString().equals("저장")) {
                count = 0;
                if (name.contains(filename)) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setMessage("이미 파일이 존재합니다 수정하시겠습니까?")
                       .setNegativeButton("아니오", null)
                       .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    readFile(path + "diary/" + filename + ".txt");
                                    btnsave.setText("수정");
                                }
                            }).show();
                    linear1.setVisibility(View.INVISIBLE);
                    linear2.setVisibility(View.VISIBLE);
                    count++;
                    return;
                }
                writeFile(path + "diary/" + filename + ".txt");
                Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show();

            } else {
                deleteExternalFile(path + "diary/" + temp + ".txt");
                writeFile(path + "diary/" + filename + ".txt");
                Toast.makeText(this, "수정완료", Toast.LENGTH_SHORT).show();
                btnsave.setText("저장");
            }
            showFileList();
        }
        if (v.getId() == R.id.btncancel) {
            linear1.setVisibility(View.VISIBLE);
            linear2.setVisibility(View.INVISIBLE);
            btnsave.setText("저장");
        }
    }
    void listViewAction() {
        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                positions = position;
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setMessage("삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String path = getFilesDir().getPath();
                                String item = name.get(position);
                                deleteExternalFile(path + "diary/" + item + ".txt");
                                showFileList();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
                return true;
            }
        });
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = getFilesDir().getPath();
                readFile(path + "diary/" + name.get(position) + ".txt");
                btnsave.setText("수정");
                linear1.setVisibility(View.INVISIBLE);
                linear2.setVisibility(View.VISIBLE);
                temp = name.get(position);
            }
        });
    }

    void readFile(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String readStr = "";
            String str;
            while ((str = br.readLine()) != null) {
                readStr += str;
            }
            br.close();
            memo.setText(readStr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void writeFile(String path) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path, false));
            bw.write(memo.getText().toString());
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void deleteExternalFile(String path) {
        File file = new File(path);
        file.delete();
    }
}
