package comwow2778.naver.blog.app14;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout Lay1,Lay2;
    DatePicker datePicker;
    ListView listview;
    ArrayAdapter<String> adapter;
    ArrayList<String> data;
    EditText et1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Lay1 = (LinearLayout)findViewById(R.id.Lay1);
        Lay2 = (LinearLayout)findViewById(R.id.Lay2);
        datePicker = (DatePicker)findViewById(R.id.datePicker);
        et1 = (EditText)findViewById(R.id.et1);
        listview = (ListView)findViewById(R.id.listview);
        data = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listview.setAdapter(adapter);
    }

    public void onClick(View v){
        if(v.getId() == R.id.btn1){
            Lay1.setVisibility(View.INVISIBLE);
            Lay2.setVisibility(View.VISIBLE);
        }
        else if(v.getId() == R.id.btncancel){

        }
        else if(v.getId() == R.id.btnsave){
            try {
                String year = String.valueOf(datePicker.getYear());
                String month = String.valueOf(datePicker.getMonth());
                if(datePicker.getMonth() < 10){
                    month = "0"+String.valueOf(datePicker.getMonth());
                }
                String day = String.valueOf(datePicker.getDayOfMonth());
                if(datePicker.getDayOfMonth()<10){
                    day = "0"+ String.valueOf(datePicker.getDayOfMonth());
                }
                String format = year.substring(2,3)+month+day;
                BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir()+year+month+day+".text", true));//true = 계속유지,false 저장X일시적
                bw.write(et1.getText().toString());
                bw.newLine();
                bw.close();
                String str= "";
                File files[] = getFilesDir().listFiles();
                for(File f:files){
                    str +=f.getName() + "\n";
                    data.add(str);
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "저장완료",Toast.LENGTH_SHORT).show();
                Lay1.setVisibility(View.VISIBLE);
                Lay2.setVisibility(View.INVISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        /*
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir() +"test.txt"));
            String readStr = "";
            String str = null;
            while((str = br.readLine()) != null)
                readStr += str +"\n";
            br.close();
            Toast.makeText(this,readStr.substring(0, readStr.length()-1),Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File not found",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
