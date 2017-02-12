package io.github.gabrielerbetta.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

import daos.ContactDAO;
import helpers.NewContactHelper;
import models.Contact;

public class NewContactActivity extends AppCompatActivity {
    private NewContactHelper new_contact_helper;
    private String photo_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        new_contact_helper = new NewContactHelper(this);

        Intent intent = this.getIntent();
        Contact contact = (Contact) intent.getSerializableExtra("contact");

        if (contact != null) {
            new_contact_helper.fillForm(contact);
        }

        ImageButton photo_button = (ImageButton) findViewById(R.id.photo_button);
        photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photo_path = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                File photo_file = new File(photo_path);
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(NewContactActivity.this, "io.github.gabrielerbetta.contactmanager.fileProvider", photo_file));
                startActivityForResult(camera_intent, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int request_code, int result_code, Intent data) {
        if (request_code == 2 && result_code == Activity.RESULT_OK) {
            new_contact_helper.setImage(photo_path);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_button:
                Contact contact = new_contact_helper.getContact();
                ContactDAO contact_dao = new ContactDAO(this);

                Boolean dao_success;
                String toast_text;
                if (contact.getId() == null) {
                    dao_success = contact_dao.insert(contact);
                    toast_text = "Contato " + contact.getName() + " salvo!";
                }
                else {
                    dao_success = contact_dao.update(contact);
                    toast_text = "Contato " + contact.getName() + " atualizado!";
                }
                contact_dao.close();

                if (dao_success) {
                    Toast.makeText(NewContactActivity.this, toast_text, Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(NewContactActivity.this, "Algo deu errado! Tente novamente", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
