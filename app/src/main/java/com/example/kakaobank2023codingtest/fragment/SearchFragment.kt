package com.example.kakaobank2023codingtest.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kakaobank2023codingtest.adapter.PostAdapter
import com.example.kakaobank2023codingtest.data.Document
import com.example.kakaobank2023codingtest.data.PostModel
import com.example.kakaobank2023codingtest.databinding.FragmentSearchBinding
import com.example.kakaobank2023codingtest.api.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_PARAM1 = "param1"

class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null

    private val postAdapter by lazy { PostAdapter() }

    private val _searchImages = MutableLiveData<List<Document>>()
    private val searchImages: LiveData<List<PostModel>> = _searchImages.switchMap {
        MutableLiveData(it.toPostModel())
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        setSearchListAdapter()
        setSearchProcess()

        searchImages.observe(viewLifecycleOwner) {
            postAdapter.submitList(it.toList())
        }
    }

    private fun setSearchListAdapter() {
        binding.searchRecyclerView.apply {
            adapter = postAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun setSearchProcess() {
        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            if (query.isNotBlank()) {
                fetchSearchImages(query)
                hideKeyboard()
            }
        }
    }

    private fun fetchSearchImages(query : String) {
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                val documents = getSearchImages(query)
                _searchImages.value = documents
            }.onFailure {
                Log.e("fetchSearchImages", "fetchSearchImages(query) failed! : ${it.message}")
            }
        }
    }

    private suspend fun getSearchImages(query: String) = withContext(Dispatchers.IO) {
        RetrofitInstance.api.getSearchImages(query = query).documents
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun List<Document>.toPostModel() : List<PostModel> {
    return this.map {
        PostModel(
            thumbnailUrl = it.thumbnailUrl.toUri(),
            siteName = it.displaySitename,
            postedTime = it.datetime
        )
    }
}