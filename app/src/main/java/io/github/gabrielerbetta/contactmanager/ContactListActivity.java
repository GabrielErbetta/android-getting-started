package io.github.gabrielerbetta.contactmanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import daos.ContactDAO;
import models.Contact;

public class ContactListActivity extends AppCompatActivity {
    private ListView lv_contact_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        lv_contact_list = (ListView) findViewById(R.id.contact_list);

        getContactList();

        registerForContextMenu(findViewById(R.id.contact_list));

        lv_contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View item, int position, long id) {
                Contact contact = (Contact) list.getItemAtPosition(position);

                Intent intent = new Intent(ContactListActivity.this, NewContactActivity.class);
                intent.putExtra("contact", contact);
                startActivity(intent);
            }
        });

        Button new_contact = (Button) findViewById(R.id.new_contact_button);
        new_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactListActivity.this, NewContactActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getContactList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo adapter_view = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Contact contact = (Contact) lv_contact_list.getItemAtPosition(adapter_view.position);

        MenuItem call_item = menu.add("Ligar");
        call_item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int checkPermission = ContextCompat.checkSelfPermission(ContactListActivity.this, Manifest.permission.CALL_PHONE);
                if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ContactListActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    Intent call_intent = new Intent(Intent.ACTION_CALL);
                    call_intent.setData(Uri.parse("tel:" + contact.getPhone()));
                    startActivity(call_intent);
                }
                return false;
            }
        });

        MenuItem sms_item = menu.add("Enviar SMS");
        Intent sms_intent = new Intent(Intent.ACTION_VIEW);
        sms_intent.setData(Uri.parse("sms:" + contact.getPhone()));
        sms_item.setIntent(sms_intent);

        MenuItem geo_item = menu.add("Ver no mapa");
        Intent geo_intent = new Intent(Intent.ACTION_VIEW);
        geo_intent.setData(Uri.parse("geo:0,0?q=" + contact.getAddress()));
        geo_item.setIntent(geo_intent);

        MenuItem website_item = menu.add("Visitar site");
        Intent website_intent = new Intent(Intent.ACTION_VIEW);
        String website = contact.getWebsite();
        if (!(website.startsWith("http://") || website.startsWith("https://"))) website = "http://" + website;
        website_intent.setData(Uri.parse(website));
        website_item.setIntent(website_intent);

        MenuItem delete_item = menu.add("Deletar");
        delete_item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ContactDAO dao = new ContactDAO(ContactListActivity.this);
                String toast_text = dao.delete(contact) ? contact.getName() + " deletado com sucesso!" : "Erro ao deletar " + contact.getName();
                Toast.makeText(ContactListActivity.this, toast_text, Toast.LENGTH_SHORT).show();
                dao.close();

                getContactList();
                return false;
            }
        });
    }

    private void getContactList() {
        ContactDAO contact_dao                   = new ContactDAO(this);
        List<Contact> contact_list               = contact_dao.getContacts();
        ArrayAdapter<Contact> contact_list_array = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contact_list);

        contact_dao.close();

        lv_contact_list.setAdapter(contact_list_array);
    }
}
