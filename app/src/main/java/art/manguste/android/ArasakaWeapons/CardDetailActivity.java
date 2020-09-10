package art.manguste.android.ArasakaWeapons;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;

import art.manguste.android.ArasakaWeapons.data.Order;
import art.manguste.android.ArasakaWeapons.data.Product;
import art.manguste.android.ArasakaWeapons.data.WeaponType;


public class CardDetailActivity extends AppCompatActivity {
    TextView mCartImage;
    TextView mItemsCount;
    TextView mProductName;
    TextView mProductType;
    TextView mProductDescription;
    ImageView mImage;
    TextView mPrice;

    private Integer itemsCount = 1;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        // toolbar and return button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        mCartImage = findViewById(R.id.tv_move_to_cart);
        mItemsCount = findViewById(R.id.tv_items_count);
        mProductName = findViewById(R.id.product_name);
        mProductType = findViewById(R.id.product_type);
        mProductDescription = findViewById(R.id.product_description);
        mPrice = findViewById(R.id.price);
        mImage = findViewById(R.id.image);

        // Get parcelable object
        Bundle arguments = getIntent().getExtras();
        if (arguments != null){
            product = arguments.getParcelable(Product.class.getSimpleName());
        }

        setCardData();

        // Buttons Listeners -->
        /** increase items count */
        ((ImageButton) findViewById(R.id.btn_inc_count)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if we still can increase - do it
                if (Order.getMaxNumPerItem() > itemsCount){
                    itemsCount++;
                    updateItemsAndPrice();
                } else {
                    Toast.makeText(getBaseContext(), R.string.warning_max_items, Toast.LENGTH_SHORT).show();
                }
            }
        });

        /** decrease items count */
        ((ImageButton) findViewById(R.id.btn_dec_count)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if we still can decrease - do it
                if (Order.getMinNumPerItem() < itemsCount){
                    itemsCount--;
                    updateItemsAndPrice();
                }
            }
        });

        /** add to cart and refresh cart icon */
        ((TextView) findViewById(R.id.tv_add_to_cart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order.getCurrentOrder().placeOrderToCart(product, itemsCount);
                Toast.makeText(getBaseContext(), "\"" + product.getTitle() + "\" added to your cart", Toast.LENGTH_LONG).show();
                CheckCartImage();
            }
        });

        /** move to cart activity */
        (findViewById(R.id.tv_move_to_cart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), CartActivity.class));
            }
        });

        CheckCartImage();

        // TODO save items count on device rotation
    }

    private void setCardData() {
        if (product != null) {
            mProductName.setText(product.getTitle());
            if (product.getWeaponType().equals(WeaponType.NONE)) {
                mProductType.setVisibility(View.GONE);
            } else {
                mProductType.setText(product.getWeaponType().toString());
            }
            mProductDescription.setText(product.getFullDescription());
            mImage.setImageResource(product.getImageResourceId());
            updateItemsAndPrice();
        }
    }

    private void updateItemsAndPrice() {
        mItemsCount.setText(String.valueOf(itemsCount));
        mPrice.setText(String.valueOf(new DecimalFormat("##.##").format(itemsCount * product.getPrice())));
    }

    /**
     * Refresh cart icon after order changes
     * */
    public void CheckCartImage() {
        Order order = Order.getCurrentOrder();
        if (order.isAnyProductInCart()){
            mCartImage.setBackground(getResources().getDrawable(R.drawable.ic_cart));
        } else {
            mCartImage.setBackground(getResources().getDrawable(R.drawable.ic_empty_cart));
        }
        // TODO refresh on resume
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}