package com.loftschool.loftcoin.screens.currencies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.loftschool.loftcoin.App;
import com.loftschool.loftcoin.R;
import com.loftschool.loftcoin.data.db.DataBase;
import com.loftschool.loftcoin.data.db.model.CoinEntity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CurrenciesBottomSheet extends BottomSheetDialogFragment implements CurrenciesAdapterListener {

    public static final String TAG = "CurrenciesBottomSheet";

    @BindView(R.id.recycler)
    RecyclerView recycler;

    private DataBase database;
    private CurrenciesAdapter adapter;
    private CurrenciesBottomSheetListener listener;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = ((App) getActivity().getApplication()).getDataBase();
        adapter = new CurrenciesAdapter();
        adapter.setListener(this);

        database.open();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_currencies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        Disposable disposable = database.getCoins()
                .subscribe(
                        coins -> adapter.setCoins(coins)
                );

        disposables.add(disposable);
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        database.close();
        super.onDestroy();
    }

    public void setListener(CurrenciesBottomSheetListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCurrencyClick(CoinEntity coin) {
        dismiss();

        if (listener != null) {
            listener.onCurrencySelected(coin);
        }
    }
}