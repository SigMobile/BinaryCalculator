package com.ACM.binarycalculator;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CalculatorActivity extends Activity implements
		View.OnClickListener, OnCheckedChangeListener {
	Button one, two, three, four, five, six, seven, eight, nine, a, b, c, d, e,
			f, equals, times, divides, plus, minus, AND, OR, sll, srl, oneComp,
			twoComp;
	TextView display, binaryRep;
	RadioGroup bases;
	long CurrentNumber = 0;
	boolean restart = true; // true when a new number is being entered

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculator);
		setup();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calculator, menu);
		return true;
	}
	private void setup() {
		// TODO Auto-generated method stub
		display = (TextView) findViewById(R.id.tvFirstNum);
		binaryRep = (TextView) findViewById(R.id.tvBiinaryFirstNum);

		bases = (RadioGroup) findViewById(R.id.rgBases);

		one = (Button) findViewById(R.id.bOne);
		one.setOnClickListener(this);
		two = (Button) findViewById(R.id.btwo);
		two.setOnClickListener(this);
		three = (Button) findViewById(R.id.bThree);
		three.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bClear:

			break;

		case R.id.bOne:
			addNumber(1);
			break;

		case R.id.btwo:
			addNumber(2);
			break;

		case R.id.bThree:
			addNumber(3);
			break;
		}
	}

	private void addNumber(int i) {
		// TODO Auto-generated method stub
		if (restart) {
			display.setText("" + i);
			restart = false;
			updateBinary();
			return;

		}
		CurrentNumber = CurrentNumber * 10 + i;
		display.setText("" + CurrentNumber);

		updateBinary();
	}

	private void updateBinary() {
		// TODO Auto-generated method stub
		String temp;
		temp = Long.toBinaryString(CurrentNumber);
		binaryRep.setText(temp);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub

	}

}
