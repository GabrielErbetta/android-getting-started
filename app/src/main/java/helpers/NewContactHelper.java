package helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import io.github.gabrielerbetta.contactmanager.NewContactActivity;
import io.github.gabrielerbetta.contactmanager.R;
import models.Contact;


public class NewContactHelper {
    private EditText name, address, phone, website;
    private RatingBar rating;
    private Contact contact;
    private ImageView photo;
    private String photo_path;

    public NewContactHelper(NewContactActivity nca) {
        this.name    = ((EditText) nca.findViewById(R.id.name_input));
        this.address = ((EditText) nca.findViewById(R.id.address_input));
        this.phone   = ((EditText) nca.findViewById(R.id.phone_input));
        this.website = ((EditText) nca.findViewById(R.id.website_input));
        this.rating  = ((RatingBar) nca.findViewById(R.id.rating_input));
        this.photo   = ((ImageView) nca.findViewById(R.id.profile_photo));
    }

    public Contact getContact() {
        if (this.contact == null) this.contact = new Contact();

        this.contact.setName(this.name.getText().toString());
        this.contact.setAddress(this.address.getText().toString());
        this.contact.setPhone(this.phone.getText().toString());
        this.contact.setWebsite(this.website.getText().toString());
        this.contact.setRating(this.rating.getRating());
        if (photo_path != null) this.contact.setPhotoPath(this.photo_path);

        return this.contact;
    }

    public void fillForm(Contact contact) {
        this.contact = contact;

        this.name.setText(contact.getName());
        this.address.setText(contact.getAddress());
        this.phone.setText(contact.getPhone());
        this.website.setText(contact.getWebsite());
        this.rating.setRating((float) contact.getRating());
        setImage(contact.getPhotoPath());
    }

    public void setImage(String photo_path) {
        if (photo_path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(photo_path);
            photo.setImageBitmap(bitmap);
            photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
            this.photo_path = photo_path;
        }
    }
}
