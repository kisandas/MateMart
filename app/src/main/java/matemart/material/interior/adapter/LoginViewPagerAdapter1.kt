package matemart.material.interior.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import matemart.material.interior.fragments.LoginFragment
import matemart.material.interior.fragments.RegisterFragment

class LoginViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(
    fm!!
) {
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        if (position == 0) {
            fragment = LoginFragment()
        } else if (position == 1) {
            fragment = RegisterFragment()
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = "If you are in"
        } else if (position == 1) {
            title = "Create New"
        }
        return title
    }
}