package leandrocurra.flux.controls;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import leandrocurra.flux.fragments.FragmentInfo;
import leandrocurra.flux.fragments.FragmentIdQuery;

/**
 * Created by leand on 31/5/2017.
 */

public class TabsAdapter extends FragmentPagerAdapter {


    private String tabTitulos[] ={"Información Mascota","Busqueda por Id"};
    static int tabs=2;

    public TabsAdapter (FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:fragment= FragmentInfo.newInstance();
                break;
            case 1:fragment= FragmentIdQuery.newInstance();
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabs;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitulos[position];
    }
}
