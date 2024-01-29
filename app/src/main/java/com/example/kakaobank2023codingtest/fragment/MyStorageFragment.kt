package com.example.kakaobank2023codingtest.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kakaobank2023codingtest.activity.MainActivity
import com.example.kakaobank2023codingtest.adapter.PostAdapter
import com.example.kakaobank2023codingtest.data.PostModel
import com.example.kakaobank2023codingtest.databinding.FragmentMyStorageBinding



class MyStorageFragment : Fragment() {

    private var _binding : FragmentMyStorageBinding? = null
    private val binding get() = _binding!!

    private val postAdapter by lazy { PostAdapter() }
    private lateinit var favoriteList: List<PostModel>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyStorageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        setSearchListAdapter()
        (requireActivity() as MainActivity).favoriteListLiveData.observe(viewLifecycleOwner) { favorites ->
            favoriteList = favorites ?: emptyList()
            postAdapter.submitList(favoriteList)
            postAdapter.itemClick = object : PostAdapter.ItemClick {
                override fun onClick(item: PostModel) {
                    val updatedFavorites = favoriteList.toMutableList()
                    updatedFavorites.remove(item)
                    favoriteList = updatedFavorites
                    postAdapter.submitList(updatedFavorites)
                    (requireActivity() as MainActivity).favoriteListLiveData.value = updatedFavorites
                }
            }
        }
    }

    private fun setSearchListAdapter() {
        binding.myStorageRecyclerView.apply {
            adapter = postAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = MyStorageFragment()
    }
}