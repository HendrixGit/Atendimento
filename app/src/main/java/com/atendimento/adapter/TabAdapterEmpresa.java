package com.atendimento.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.atendimento.fragment.EmpresasFragment;

public class TabAdapterEmpresa extends FragmentStatePagerAdapter {//usado para listagem de dados

    private String[] tituloAbas = {"EMPRESAS"};

    public TabAdapterEmpresa(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0: fragment = new EmpresasFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }
}