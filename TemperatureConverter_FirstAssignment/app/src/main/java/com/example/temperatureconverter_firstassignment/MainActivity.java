package com.example.temperatureconverter_firstassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    private RadioButton CelsiustoFarenhit;
    private RadioButton FarenhittoCelsius;
    private EditText UserInput;
    private TextView ConvertedOutput;
    private TextView UnitofInputValue;
    private TextView UnitofOutputValue;
    private TextView RecentConversions;
    private Button ConvertButton;
    private String string="";
    private String CheckRadioButton = "Fahrenheit to Celsius";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CelsiustoFarenhit = (RadioButton) findViewById(R.id.CelsiusToFarenhit);
        FarenhittoCelsius = (RadioButton) findViewById(R.id.FarenhitToCelsius);
        UserInput = (EditText) findViewById(R.id.Input);
        ConvertedOutput = (TextView) findViewById(R.id.Output);
        UnitofInputValue = (TextView) findViewById(R.id.UnitOfInput);
        UnitofOutputValue = (TextView) findViewById(R.id.UnitOfOutput);
        RecentConversions = (TextView) findViewById(R.id.ConversionHistoryView);
        ConvertButton =(Button) findViewById(R.id.ClearButton);
        RecentConversions.setMovementMethod(new ScrollingMovementMethod());
    }

    public void selectUnit(View v)
    {
        CheckRadioButton = ((RadioButton) v).getText().toString();
        if(CheckRadioButton.trim().equals("Fahrenheit to Celsius"))
        {
            UnitofInputValue.setText("Fahrenheit Degree");
            UnitofOutputValue.setText("Celsius Degree");
        }
        else
        {
            UnitofInputValue.setText("Celsius Degree");
            UnitofOutputValue.setText("Fahrenheit Degree");
        }
        UserInput.setText("");
        ConvertedOutput.setText("");
    }


    public void onClickClear(View v)
    {
        string="";
        RecentConversions.setText(string);
    }

    public void TempConversion (View v)
    {
        Double TemperatureInput = Double.parseDouble(UserInput.getText().toString());
        Double TemperatureResult;
        if (CelsiustoFarenhit.isChecked())
        {
            if (UserInput.getText().length() == 0)
            {
                Toast.makeText(getApplicationContext(), "Please Enter Temperature in Celsius", Toast.LENGTH_SHORT).show();
            }
            else
            {
                TemperatureResult = (TemperatureInput * 1.8) + 32;
                ConvertedOutput.setText(String.format("%,.1f", TemperatureResult));
                double tempinput = Double.parseDouble(UserInput.getText().toString());
                string = "C to F: " + (String.format("%,.1f", tempinput)) + " -> " + (String.format("%,.1f", TemperatureResult)) + "\n" + string;
                RecentConversions.setText(string);
            }
        }

        if (FarenhittoCelsius.isChecked())
        {
            if (UserInput.getText().length() == 0)
            {
                Toast.makeText(getApplicationContext(), "Please Enter Temperature in Fahrenheit ", Toast.LENGTH_SHORT).show();
            }
            else
            {
                TemperatureResult = (TemperatureInput - 32) / 1.8;
                ConvertedOutput.setText(String.format("%,.1f", TemperatureResult));
                double tmpinput =  Double.parseDouble(UserInput.getText().toString());
                string = "F to C: " + (String.format("%,.1f", tmpinput)) + " -> " + (String.format("%,.1f", TemperatureResult)) + "\n" + string;
                RecentConversions.setText(string);
            }

        }
    }

    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString("RecentConversions", string);
        outState.putString("UnitofInputValue", UnitofInputValue.getText().toString());
        outState.putString("UnitofOutputValue", UnitofOutputValue.getText().toString());
        outState.putString("inputValue", UserInput.getText().toString());
        outState.putString("outputValue", ConvertedOutput.getText().toString());
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);

        string = savedInstanceState.getString("RecentConversions");
        RecentConversions.setText(string);
        UserInput.setText(savedInstanceState.getString("inputValue"));
        ConvertedOutput.setText(savedInstanceState.getString("outputValue"));
        UnitofInputValue.setText(savedInstanceState.getString("UnitofInputValue"));
        UnitofOutputValue.setText(savedInstanceState.getString("UnitofOutputValue"));
        if(savedInstanceState.getString("UnitofInputValue").toString().equalsIgnoreCase("Celsius Degree"))
        {
            CelsiustoFarenhit.setChecked(true);
            FarenhittoCelsius.setChecked(false);
        }
        else
        {
            CelsiustoFarenhit.setChecked(false);
            FarenhittoCelsius.setChecked(true);
        }
    }

}
