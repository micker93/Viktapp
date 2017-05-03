package com.example.riiss.viktapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Grafsidan extends AppCompatActivity {

    String newWeight = "";
    List<Float> weightList= new ArrayList<Float>();
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    private TextView weightTextView;
    private DatabaseReference mDatabase;
    private GraphView graph;
    private Button button;
    Date date = new Date();
    SimpleDateFormat formatter= new SimpleDateFormat("yy-MM-dd");
    String format=formatter.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafsidan);

        button=(Button)findViewById(R.id.listknapp);
        button.setInputType(InputType.TYPE_CLASS_NUMBER);

        mDatabase=FirebaseDatabase.getInstance().getReference().child("Vikterna");
        weightTextView=(TextView) findViewById(R.id.viktlistan);
        graph = (GraphView) findViewById(R.id.graph);

        weightTextView.setMovementMethod(new ScrollingMovementMethod());

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<Float>> t = new GenericTypeIndicator<ArrayList<Float>>() {};
                weightList = dataSnapshot.getValue(t);

                weightTextView.setText("");
                series.resetData(new DataPoint[0]);

                for(int i=0; i < weightList.size();i++) {

                    weightTextView.setText(weightTextView.getText() +  "\n" +  weightList.get(i).toString() + "kg" + "\n" + format + "\n" );
                    DataPoint point = new DataPoint(i+1, weightList.get(i));
                    series.appendData(point,true,weightList.size());

                }

                graph.addSeries(series);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                weightTextView.setText(databaseError.getCode());
            }

        });

    }


    public void bildbibleiotek(View v) {

        Intent intent = new Intent(this, Bildbibeliotek.class);
        startActivity(intent);
    }


    public void läggtillvärden(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Grafsidan.this);
        builder.setTitle("Lägg till vikt");
        builder.setMessage("Skriv in vikten");
        final EditText editext = new EditText(this);
        editext.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(editext);
        builder.setPositiveButton("Klar", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int press){


                newWeight=editext.getText().toString();
                weightList.add(Float.parseFloat(newWeight));
                mDatabase.setValue(weightList);

            }
        });

        builder.setNegativeButton("Tillbaka" , null);
        builder.create();
        builder.show();

    }

    public void tabortknapp(final View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(Grafsidan.this);
        builder.setTitle("Ta bort?");
        builder.setMessage("Är du säker på att du vill ta bort?");
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int press){
                newWeight="";
                weightList.removeAll(weightList);
                mDatabase.child("Vikterna").removeValue();
                weightTextView.setText("");
                graph.removeAllSeries();

            }
        });

        builder.setNegativeButton("Nej" , null);
        builder.create();
        builder.show();

    }

}