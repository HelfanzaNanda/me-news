package com.elfan.mynews.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.elfan.mynews.R
import com.elfan.mynews.ui.login.LoginActivity
import com.elfan.mynews.utils.Constants
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logout()
    }
    private fun logout(){
        btn_logout.setOnClickListener {
            Constants.clearToken(requireActivity())
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
}

