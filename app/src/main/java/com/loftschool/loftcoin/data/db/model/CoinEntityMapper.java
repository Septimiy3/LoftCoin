package com.loftschool.loftcoin.data.db.model;

import com.loftschool.loftcoin.data.api.model.Coin;

import java.util.List;

public interface CoinEntityMapper {
    List<CoinEntity> map(List<Coin> coins);

}
