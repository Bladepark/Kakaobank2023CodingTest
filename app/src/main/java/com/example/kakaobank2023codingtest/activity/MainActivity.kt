package com.example.kakaobank2023codingtest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import com.example.kakaobank2023codingtest.R
import com.example.kakaobank2023codingtest.adapter.PostAdapter
import com.example.kakaobank2023codingtest.data.PostModel
import com.example.kakaobank2023codingtest.databinding.ActivityMainBinding
import com.example.kakaobank2023codingtest.fragment.MyStorageFragment
import com.example.kakaobank2023codingtest.fragment.SearchFragment

class MainActivity : AppCompatActivity(){

    // 리스너

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val searchFragment by lazy { SearchFragment() }
    private val myStorageFragment by lazy { MyStorageFragment() }

    val favoriteListLiveData: MutableLiveData<List<PostModel>> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.apply {
            btnImageSearchFragment.setOnClickListener {
                setFragment(searchFragment)
            }
            btnMyStorageFragment.setOnClickListener {
                setFragment(myStorageFragment)
            }
        }
        setFragment(searchFragment)
    }

    private fun setFragment(fragment : Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainerView, fragment)
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }

}