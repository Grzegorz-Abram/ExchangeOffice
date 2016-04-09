package com.futureprocessing.webtask.exchangeoffice.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.futureprocessing.webtask.exchangeoffice.model.Wallets;

@Repository
public class WalletsDaoImpl implements WalletsDao {

    @Autowired
    private SessionFactory sessionFactory;

    public WalletsDaoImpl() {
    }

    public WalletsDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Wallets> loadWallet(String username) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Wallets.class);
        criteria.add(Restrictions.eq("username", username));
        return criteria.list();
    }

    @Override
    @Transactional(readOnly = false)
    public void addToWallet(Wallets entry) {
        Session session = sessionFactory.openSession();

        session.save(entry);

        session.flush();
    }

}
