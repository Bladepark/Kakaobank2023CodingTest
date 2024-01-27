package com.example.kakaobank2023codingtest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.kakaobank2023codingtest.R
import com.example.kakaobank2023codingtest.databinding.ActivityMainBinding
import com.example.kakaobank2023codingtest.fragment.MyStorageFragment
import com.example.kakaobank2023codingtest.fragment.SearchFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val searchFragment by lazy { SearchFragment() }
    private val myStorageFragment by lazy { MyStorageFragment() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.apply {
            btnImageSearchFragment.setOnClickListener {
                // MainActivity -> ImageSearchFragment
                //val dataToSend = "Hello First Fragment\n from Activity"
                //val fragment = ImageSearchFragment.newInstance()
                setFragment(searchFragment)
            }
            btnMyStorageFragment.setOnClickListener {
                // MainActivity -> MyStorageFragment
                //val dataToSend = "Hello Second Fragment\n from Activity"
                //val fragment = MyStorageFragment.newInstance()
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