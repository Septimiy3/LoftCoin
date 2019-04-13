package com.loftschool.loftcoin.screens.wallets;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loftschool.loftcoin.App;
import com.loftschool.loftcoin.R;
import com.loftschool.loftcoin.data.db.model.CoinEntity;
import com.loftschool.loftcoin.data.prefs.Prefs;
import com.loftschool.loftcoin.screens.currencies.CurrenciesBottomSheet;
import com.loftschool.loftcoin.screens.currencies.CurrenciesBottomSheetListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;


public class WalletsFragment extends Fragment implements CurrenciesBottomSheetListener {

    @BindView(R.id.wallets_toolbar)
    Toolbar toolbar;

    @BindView(R.id.transition_recycler)
    RecyclerView transitionRecycler;

    @BindView(R.id.wallets_pager)
    ViewPager walletsPager;

    @BindView(R.id.new_wallets)
    ViewGroup newWallets;

    private WalletsPagerAdapter walletsPagerAdapter;

    private WalletsViewModel viewModle;


    public WalletsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModle = ViewModelProviders.of(this).get(WalletsViewModelImpl.class);

        Prefs prefs = ((App) getActivity().getApplication()).getPrefs();

        walletsPagerAdapter = new WalletsPagerAdapter(prefs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.wallets_main);
        toolbar.inflateMenu(R.menu.menu_wallets);

        walletsPager.setAdapter(walletsPagerAdapter);


        Fragment bottomSheet = getFragmentManager().findFragmentByTag(CurrenciesBottomSheet.TAG);
        if (bottomSheet != null) {
            ((CurrenciesBottomSheet) bottomSheet).setListener(this);
        }

        viewModle.getWallets();

        initOutputs();
        initInputs();
    }

    private void initOutputs() {
        newWallets.setOnClickListener(v -> {
            viewModle.onNewWalletClick();
        });
        toolbar.getMenu().findItem(R.id.menu_item_add_wallet).setOnMenuItemClickListener(item -> {
            viewModle.onNewWalletClick();
            return true;
        });
    }

    private void initInputs() {
        viewModle.selectCurrency().observe(this, o -> {
            showCurrencyBottomSheet();
        });

        viewModle.newWalletsVisible().observe(this, visible ->
                newWallets.setVisibility(visible ? View.VISIBLE : View.GONE)
        );

        viewModle.walletsVisible().observe(this, visible ->
                walletsPager.setVisibility(visible ? View.VISIBLE : View.GONE)
        );

        viewModle.wallets().observe(this, wallets -> {
            walletsPagerAdapter.setWallets(wallets);
        });
    }

    private void showCurrencyBottomSheet() {
        CurrenciesBottomSheet bottomSheet = new CurrenciesBottomSheet();

        bottomSheet.show(getFragmentManager(), CurrenciesBottomSheet.TAG);
        bottomSheet.setListener(this);
    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {
        viewModle.onCurrencySelected(coin);
    }
}
