package com.loftschool.loftcoin.screens.rate;


import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.loftschool.loftcoin.App;
import com.loftschool.loftcoin.R;
import com.loftschool.loftcoin.data.api.Api;
import com.loftschool.loftcoin.data.db.DataBase;
import com.loftschool.loftcoin.data.db.model.CoinEntity;
import com.loftschool.loftcoin.data.db.model.CoinEntityMapper;
import com.loftschool.loftcoin.data.db.model.CoinEntityMapperImpl;
import com.loftschool.loftcoin.data.prefs.Prefs;
import com.loftschool.loftcoin.utils.Fiat;
import com.loftschool.loftcoin.work.WorkHelper;
import com.loftschool.loftcoin.work.WorkHelperImpl;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateFragment extends Fragment implements RateView,
        Toolbar.OnMenuItemClickListener,
        CurrencyDialog.CurrencyDialogListener,
        RateAdapter.RateViewHolder.Listener {


    private static final String LAYOUT_MANAGER_STATE = "layout_manager_state";

    public RateFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.rate_recycler)
    RecyclerView recycler;

    @BindView(R.id.rate_toolbar)
    Toolbar toolbar;

    @BindView(R.id.rate_refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.rate_content)
    ViewGroup content;

    private Parcelable layouManagerState;

    private RatePresenter presenter;
    private RateAdapter adapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();

        if (activity == null) {
            return;
        }

        Api api = ((App) getActivity().getApplication()).getApi();
        Prefs prefs = ((App) getActivity().getApplication()).getPrefs();
        DataBase mainDatabase = (((App) getActivity().getApplication()).getDataBase());
        DataBase workerDatabase = (((App) getActivity().getApplication()).getDataBase());
        CoinEntityMapper coinEntityMapper = new CoinEntityMapperImpl();
        WorkHelper workHelper = new WorkHelperImpl();

        presenter = new RatePresenterImpl(prefs, api, mainDatabase, workerDatabase, coinEntityMapper, workHelper);
        adapter = new RateAdapter(prefs);
        adapter.setListener(this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        toolbar.setTitle(R.string.rate_screen_title);
        toolbar.inflateMenu(R.menu.menu_rate);
        toolbar.setOnMenuItemClickListener(this);

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });

        if (savedInstanceState != null) {
            layouManagerState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
        }
        Fragment fragment = getFragmentManager().findFragmentByTag(CurrencyDialog.TAG);

        if (fragment != null) {
            CurrencyDialog dialog = (CurrencyDialog) fragment;
            dialog.setListener(this);
        }

        presenter.attachView(this);
        presenter.getRate();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(LAYOUT_MANAGER_STATE, recycler.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setCoins(List<CoinEntity> coins) {
        Timber.d("setCoins: ");
        adapter.setItems(coins);

        if (layouManagerState != null) {
            recycler.getLayoutManager().onRestoreInstanceState(layouManagerState);
            layouManagerState = null;
        }
    }

    @Override
    public void setRefreshing(Boolean refreshing) {
        refresh.setRefreshing(refreshing);
    }

    @Override
    public void showCurrencyDialog() {
        CurrencyDialog dialog = new CurrencyDialog();
        dialog.show(getFragmentManager(), CurrencyDialog.TAG);
        dialog.setListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_currency:
                presenter.onMenuItemCurrencyClick();
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onCurrencySelected(Fiat currency) {
        presenter.onFiatCurrencySelected(currency);
    }

    @Override
    public void invalidateRates() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRateLongClick(String symbol) {
        presenter.onRateLongClick(symbol);
    }
}
