package ro.pub.cs.systems.eim.lab04.contactsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private EditText jobTitleEditText;
    private EditText companyEditText;
    private EditText websiteEditText;
    private EditText imEditText;

    private Button show_hide_detail_button;
    private Button save_button;
    private Button cancel_button;
    private LinearLayout additionalFieldsContainer;


    CancelListener cancelListener = new CancelListener();
    ShowHideListener showHideListener = new ShowHideListener();
    SaveListener saveListener = new SaveListener();


    private class SaveListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String name = nameEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String address = addressEditText.getText().toString();
            String jobTitle = jobTitleEditText.getText().toString();
            String company = companyEditText.getText().toString();
            String website = websiteEditText.getText().toString();
            String im = imEditText.getText().toString();

            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            if (name != null) {
                intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
            }
            if (phone != null) {
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
            }
            if (email != null) {
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
            }
            if (address != null) {
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
            }
            if (jobTitle != null) {
                intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
            }
            if (company != null) {
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
            }
            ArrayList<ContentValues> contactData = new ArrayList<>();
            if (website != null) {
                ContentValues websiteRow = new ContentValues();
                websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                contactData.add(websiteRow);
            }
            if (im != null) {
                ContentValues imRow = new ContentValues();
                imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                contactData.add(imRow);
            }
            intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
            startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
        }
    }

    private class ShowHideListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (additionalFieldsContainer.getVisibility()) {
                case View.VISIBLE:
                    show_hide_detail_button.setText(getResources().getString(R.string.show_additional_fields));
                    additionalFieldsContainer.setVisibility(View.INVISIBLE);
                    break;
                case View.INVISIBLE:
                    show_hide_detail_button.setText(getResources().getString(R.string.hide_additional_fields));
                    additionalFieldsContainer.setVisibility(View.VISIBLE);
                    break;
                case View.GONE:
                    break;
            }
        }
    }

    private class CancelListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);

        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        phoneEditText = (EditText) findViewById(R.id.phone_number_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        addressEditText = (EditText) findViewById(R.id.address_edit_text);
        jobTitleEditText = (EditText) findViewById(R.id.job_title_edit_text);
        companyEditText = (EditText) findViewById(R.id.company_edit_text);
        websiteEditText = (EditText) findViewById(R.id.website_edit_text);
        imEditText = (EditText) findViewById(R.id.im_edit_text);

        show_hide_detail_button = (Button) findViewById(R.id.show_hide_additional_fields);
        show_hide_detail_button.setOnClickListener(showHideListener);
        save_button = (Button) findViewById(R.id.save_button);
        save_button.setOnClickListener(saveListener);
        cancel_button = (Button) findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(cancelListener);

        additionalFieldsContainer = (LinearLayout) findViewById(R.id.additional_fields_container);

        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneEditText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == Constants.CONTACTS_MANAGER_REQUEST_CODE) {
            setResult(resultCode, new Intent());
            finish();
        }
    }
}