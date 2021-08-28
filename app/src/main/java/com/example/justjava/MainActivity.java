package com.example.justjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    private String name;
    private int quantity = 2;
    boolean addWhippedCream = false;
    boolean addChocolate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if(quantity == 10) {
            Toast.makeText(this, "You can't have more than 10 cups of coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity++;
        displayQuantity();
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if(quantity == 1) {
            Toast.makeText(this, "You can't have less than 1 cup of coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity--;
        displayQuantity();
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity() {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText nameField = (EditText) findViewById(R.id.name_field);
        this.name = nameField.getText().toString();
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        this.quantity = Integer.parseInt(quantityTextView.getText().toString());
        CheckBox whippedCreamCheckbox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        this.addWhippedCream = whippedCreamCheckbox.isChecked();
        CheckBox chocolateCheckbox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        this.addChocolate = chocolateCheckbox.isChecked();

        String message = createOrderSummary();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Creates summary of the order.
     * @return text summary
     */
    private String createOrderSummary() {
        String price = NumberFormat.getCurrencyInstance().format(calculatePrice());
        String message = getString(R.string.order_summary_name, name);
        message += '\n' + getString(R.string.order_summary_whipped_cream, addWhippedCream);
        message += '\n' + getString(R.string.order_summary_chocolate, addChocolate);
        message += '\n' + getString(R.string.order_summary_quantity, quantity);
        message += '\n' + getString(R.string.order_summary_price, price);
        message += '\n' + getString(R.string.thank_you);

        return message;
    }

    /**
     * Calculates the price of the order.
     * @return total price
     */
    private int calculatePrice() {
        final int basePrice = 5;
        final int whippedCreamPrice = 1;
        final int chocolatePrice = 2;
        return quantity * (basePrice + (addWhippedCream?whippedCreamPrice:0) + (addChocolate?chocolatePrice:0));
    }
}