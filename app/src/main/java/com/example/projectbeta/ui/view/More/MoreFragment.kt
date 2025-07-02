package com.example.projectbeta.ui.view.More

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import com.example.projectbeta.ui.adapter.ListAdapter
import com.example.projectbeta.ui.view.Login.LoginActivity
import com.example.projectbeta.data.model.ListModel
import com.example.projectbeta.R
import com.google.firebase.auth.FirebaseAuth

class MoreFragment : Fragment() {
    private lateinit var listview: ListView
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_more, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        setupListMenu(view)

        return view
    }

    private fun setupListMenu(view: View) {
        val listView = view.findViewById<ListView>(R.id.list_view)
        val menuList = listOf(
            ListModel("About", "", R.drawable.info),
            ListModel("Logout", "", R.drawable.logout)
        )

        val adapter = ListAdapter(requireContext(), menuList)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = menuList[position]

            if (selectedItem.name == "Logout") {
                firebaseAuth.signOut()
                val i = Intent(requireContext(), LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)

                requireActivity().finish()
            } else if (selectedItem.name == "About") {
                if (selectedItem.name == "About") {
                    val navController = findNavController()
                    navController.navigate(R.id.action_moreFragment_to_aboutFragment)
                }

            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}