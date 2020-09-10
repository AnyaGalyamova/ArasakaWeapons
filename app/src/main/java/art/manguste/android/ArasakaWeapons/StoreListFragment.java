package art.manguste.android.ArasakaWeapons;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import art.manguste.android.ArasakaWeapons.data.CatalogType;
import art.manguste.android.ArasakaWeapons.data.Order;
import art.manguste.android.ArasakaWeapons.data.Product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreListFragment extends Fragment
        implements CardAdapter.ListItemClickListener {

    private static final String CATALOG_TYPE = "catalog_type";

    private CatalogType catalogType;
    private Context mContex;

    private ViewGroup mViewGroup;

    public StoreListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param catalogType sets fragment type.
     * @return A new instance of fragment StoreListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreListFragment newInstance(CatalogType catalogType){
    //(String param1, String param2) {
        StoreListFragment fragment = new StoreListFragment();
        Bundle args = new Bundle();
        args.putString(CATALOG_TYPE, String.valueOf(catalogType));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            catalogType = CatalogType.valueOf(getArguments().getString(CATALOG_TYPE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_list, container, false);

        mViewGroup = container;
        mContex = getContext();

        // add adapter
        CardAdapter adapter;
        adapter = new CardAdapter(catalogType, this);

        recyclerView.setAdapter(adapter);
        // connect data and view
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);

        return recyclerView;
    }


    /**
     * After click on whole ViewCard from RecyclerView
     * */
    @Override
    public void onListItemClick(int position, Product product) {
        Intent intent = new Intent(mContex, CardDetailActivity.class);
        intent.putExtra(Product.class.getSimpleName(), product);
        startActivity(intent);
    }

    @Override
    public void onViewClick(View v, int position, MaterialCardView item, Product product) {
        if (v.getId() == R.id.ib_add_position_in_cart || v.getId() == R.id.ll_add_position_in_cart){

            // Change visibility
            //item.findViewById(R.id.ib_add_position_in_cart).setVisibility(View.GONE);
            //item.findViewById(R.id.ll_add_position_in_cart).setVisibility(View.GONE);
            //item.findViewById(R.id.tv_move_to_cart_from_card).setVisibility(View.VISIBLE);
            Order.getCurrentOrder().placeOrderToCart(product, 1);
            ((MainActivity) mContex).CheckCartImage();

            // Snackbar interaction
            String snackMessage = "\"" + ((TextView) item.findViewById(R.id.product_name)).getText() + "\" added to your cart";
            Snackbar snackbar = Snackbar
                    .make(mViewGroup, snackMessage, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.go_to_cart), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getContext(), CartActivity.class));
                        }
                    });
            //change snackbar colors
            snackbar.setActionTextColor(getResources().getColor(R.color.colorArasakaBackground));
            snackbar.setBackgroundTint(getResources().getColor(R.color.colorDarkBackground));
            //snackbar message appearance
            TextView tvSnackbar = ((TextView) snackbar.getView().findViewById(R.id.snackbar_text));
            tvSnackbar.setTextColor(getResources().getColor(R.color.colorArasakaRed));
            tvSnackbar.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            snackbar.show();



        } else if (v.getId() == R.id.tv_move_to_cart_from_card){
            // Do your stuff here
            Intent intent = new Intent(mContex, CartActivity.class);
            startActivity(intent);
        }
    }

}