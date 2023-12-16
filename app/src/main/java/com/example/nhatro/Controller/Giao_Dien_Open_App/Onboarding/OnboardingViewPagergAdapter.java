package com.example.nhatro.Controller.Giao_Dien_Open_App.Onboarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class OnboardingViewPagergAdapter extends FragmentStatePagerAdapter {
    public OnboardingViewPagergAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment_Onboarding_1();
            case 1:
                return new Fragment_Onboarding_2();
            case 2:
                return new Fragment_Onboarding_3();
            default:
                return new Fragment_Onboarding_1();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
